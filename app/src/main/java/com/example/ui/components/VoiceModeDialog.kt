package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.model.ElevenLabsVoice
import com.example.model.OrbSettings
import com.example.model.TtsEngine
import com.example.model.VoicePipelineStage
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun VoiceModeDialog(
    viewModel: MyraViewModel,
    orbSettings: OrbSettings,
    onDismiss: () -> Unit,
    onSendCommand: (String) -> Unit = { viewModel.processVoiceInput(it) },
    lastResponse: String = ""
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val isApiKeyConfigured by viewModel.isApiKeyConfigured.collectAsState()
    val lastAssistantReply by viewModel.lastAssistantReply.collectAsState()
    val activeVoiceName by viewModel.selectedVoice.collectAsState()
    val availableVoices by viewModel.voiceModels.collectAsState()

    val currentPipelineStage by viewModel.currentPipelineStage.collectAsState()
    val selectedTtsEngine by viewModel.selectedTtsEngine.collectAsState()
    val elevenLabsVoiceName by viewModel.elevenLabsVoiceName.collectAsState()
    val elevenLabsVoiceId by viewModel.elevenLabsVoiceId.collectAsState()
    val elevenLabsApiKey by viewModel.elevenLabsApiKey.collectAsState()
    val elevenLabsStability by viewModel.elevenLabsStability.collectAsState()
    val elevenLabsSimilarity by viewModel.elevenLabsSimilarity.collectAsState()
    val ttsEngineStatus by viewModel.ttsEngineStatus.collectAsState()
    val allElevenLabsVoices by viewModel.elevenLabsVoicesState.collectAsState()
    var elevenLabsApiKeyInput by remember(elevenLabsApiKey) { mutableStateOf(elevenLabsApiKey) }
    var showPipelineFlow by remember { mutableStateOf(true) }
    var showElevenLabsDrawer by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val isContinuousModeEnabled = true
    var isListening by remember { mutableStateOf(false) }
    var isRecognizerActive by remember { mutableStateOf(false) }
    var isProcessingCommand by remember { mutableStateOf(false) }
    var userSpokenText by remember { mutableStateOf("Boliye... MJ lagatar sun rahi hai") }
    var listeningStatus by remember { mutableStateOf("🟢 Mic Permanent Active (Sun rahi hoon)") }
    var manualInputText by remember { mutableStateOf("") }
    var apiKeyInput by remember { mutableStateOf("") }
    var showVoiceSelector by remember { mutableStateOf(false) }
    var consecutiveErrorCount by remember { mutableIntStateOf(0) }

    val activeCompany by viewModel.activeCompany.collectAsState()
    val activeVoiceProfile by viewModel.activeVoiceProfile.collectAsState()
    var selectedCompanyForInput by remember(activeCompany) { mutableStateOf(activeCompany) }

    val lastSpeechFinishedTimestamp by viewModel.lastSpeechFinishedTimestamp.collectAsState()
    val currentAiResponse = if (lastAssistantReply.isNotBlank()) lastAssistantReply else lastResponse.ifEmpty { "Main sun rahi hoon, boliye!" }

    // Speech Recognizer
    val speechRecognizer = remember(context) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            SpeechRecognizer.createSpeechRecognizer(context)
        } else {
            null
        }
    }

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        }
    }

    val startSpeechListening: () -> Unit = {
        if (speechRecognizer != null && hasAudioPermission && !isSpeaking) {
            try {
                try { speechRecognizer.cancel() } catch (_: Exception) {}
                isRecognizerActive = true
                isListening = true
                isProcessingCommand = false
                listeningStatus = "🟢 MJ sun rahi hai... Boliye"
                if (userSpokenText.startsWith("Listening") || userSpokenText.startsWith("Boliye") || userSpokenText.startsWith("Sun")) {
                    userSpokenText = "Boliye, MJ sun rahi hai..."
                }
                speechRecognizer.startListening(recognizerIntent)
            } catch (e: Exception) {
                isListening = false
                isRecognizerActive = false
                listeningStatus = "Mic restart ho raha hai..."
            }
        }
    }

    val stopSpeechListening: () -> Unit = {
        isListening = false
        isRecognizerActive = false
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.cancel()
        } catch (_: Exception) {}
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
        if (granted) {
            startSpeechListening()
        } else {
            viewModel.showToast("Microphone permission required for voice conversation")
        }
    }

    // Set recognition listener
    DisposableEffect(speechRecognizer) {
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
                isRecognizerActive = true
                consecutiveErrorCount = 0
                if (!isSpeaking) {
                    listeningStatus = "🟢 Mic ON • Boliye MJ sun rahi hai"
                }
            }

            override fun onBeginningOfSpeech() {
                isListening = true
                consecutiveErrorCount = 0
                viewModel.setPipelineStage(VoicePipelineStage.USER_SPEAKING)
                listeningStatus = "🎙️ Aap bol rahe hain... (Sun rahi hoon)"
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                isListening = false
                isRecognizerActive = false
                viewModel.setPipelineStage(VoicePipelineStage.SPEECH_TO_TEXT)
                listeningStatus = "⚡ Samajh rahi hoon..."
            }

            override fun onError(error: Int) {
                isListening = false
                isRecognizerActive = false
                viewModel.setPipelineStage(VoicePipelineStage.IDLE)
                consecutiveErrorCount++
                try {
                    speechRecognizer?.cancel()
                } catch (_: Exception) {}

                // Controlled retry loop with error throttling
                if (!isSpeaking && hasAudioPermission) {
                    if (consecutiveErrorCount > 4) {
                        listeningStatus = "🟢 Mic Standby • Orb tap karke bolein"
                        return
                    }

                    val delayTime = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> 600L
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY, SpeechRecognizer.ERROR_CLIENT -> 1500L
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                            hasAudioPermission = false
                            listeningStatus = "Mic permission required"
                            return
                        }
                        else -> 800L
                    }

                    listeningStatus = "🟢 Sun rahi hoon... Boliye"
                    coroutineScope.launch {
                        delay(delayTime)
                        if (!isSpeaking && hasAudioPermission && consecutiveErrorCount <= 4) {
                            startSpeechListening()
                        }
                    }
                }
            }

            override fun onResults(results: Bundle?) {
                isListening = false
                isRecognizerActive = false
                consecutiveErrorCount = 0
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val spoken = matches?.firstOrNull()?.trim()
                if (!spoken.isNullOrBlank()) {
                    userSpokenText = spoken
                    isProcessingCommand = true
                    listeningStatus = "⚡ Process ho raha hai..."
                    viewModel.performHaptic()
                    viewModel.playActiveThinkingSound()
                    viewModel.processVoiceInput(spoken)
                    onSendCommand(spoken)
                }

                // Immediately re-arm mic for continuous listening
                coroutineScope.launch {
                    delay(350L)
                    if (!isSpeaking && hasAudioPermission) {
                        startSpeechListening()
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val partial = matches?.firstOrNull()?.trim()
                if (!partial.isNullOrBlank()) {
                    userSpokenText = partial
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        // Auto-start continuous listening if audio permission is granted
        if (hasAudioPermission) {
            startSpeechListening()
        }

        onDispose {
            try {
                speechRecognizer?.stopListening()
                speechRecognizer?.cancel()
                speechRecognizer?.destroy()
            } catch (_: Exception) {}
            viewModel.stopSpeaking()
        }
    }

    // Auto-prompt microphone permission on opening voice mode if not yet granted
    LaunchedEffect(Unit) {
        viewModel.playActiveWakeSound()
        if (!hasAudioPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // React to MJ speaking completion: immediately restart listening in permanent mode
    LaunchedEffect(isSpeaking, lastSpeechFinishedTimestamp) {
        if (!isSpeaking) {
            isProcessingCommand = false
            if (hasAudioPermission && !isListening) {
                delay(300L) // Wait briefly to flush speaker audio buffer
                if (!isSpeaking) {
                    startSpeechListening()
                }
            }
        } else {
            // MJ is speaking -> temporarily pause recognizer to prevent echo
            try {
                speechRecognizer?.cancel()
            } catch (_: Exception) {}
            isListening = false
            isRecognizerActive = false
        }
    }

    // Permanent Active Mic Watchdog: Ensures mic NEVER stays dead/silent when MJ is not speaking
    LaunchedEffect(hasAudioPermission, isSpeaking, consecutiveErrorCount) {
        while (true) {
            delay(1500L)
            if (!isSpeaking && !isListening && hasAudioPermission && consecutiveErrorCount <= 4) {
                startSpeechListening()
            }
        }
    }

    // Watchdog to ensure isProcessingCommand never remains stuck and mic restarts reliably
    LaunchedEffect(isProcessingCommand) {
        if (isProcessingCommand) {
            delay(3500L)
            isProcessingCommand = false
            if (!isSpeaking && !isListening && hasAudioPermission) {
                startSpeechListening()
            }
        }
    }

    val quickSampleCommands = listOf(
        "MJ ye kaam karo torch on karo",
        "MJ yah kam karo YouTube kholo",
        "MJ Instagram open karo",
        "MJ WhatsApp kholo",
        "MJ Camera kholo",
        "MJ Volume badhao",
        "MJ Wi-Fi settings open karo",
        "MJ Bluetooth settings kholo",
        "MJ Phone settings open karo",
        "MJ Subah 6 baje alarm lagao",
        "MJ Battery kitni bachi hai?"
    )

    Dialog(
        onDismissRequest = {
            stopSpeechListening()
            viewModel.stopSpeaking()
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF040508),
                            Color(0xFF16060E),
                            Color(0xFF07080B)
                        )
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
                .testTag("voice_mode_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            stopSpeechListening()
                            viewModel.stopSpeaking()
                            onDismiss()
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MyraCardDark)
                            .testTag("voice_dialog_close")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }

                    // Live Status Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MyraCardDark)
                            .border(1.dp, Color(0xFF00E676), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(if (isSpeaking) MyraNeonCyan else Color(0xFF00E676))
                        )
                        Text(
                            text = if (isSpeaking) "MJ BOL RAHI HAI..." else "🟢 MIC PERMANENT ACTIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFB9F6CA),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Voice Model switch icon
                    IconButton(
                        onClick = { showVoiceSelector = !showVoiceSelector },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MyraCardDark)
                            .testTag("voice_dialog_settings")
                    ) {
                        Icon(
                            Icons.Default.GraphicEq,
                            contentDescription = "Voice Style",
                            tint = if (showVoiceSelector) MyraNeonCyan else MyraRedGlow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // API Key & Company Voice Status Banner / Switcher
                val companyList = listOf("Google Gemini", "OpenAI", "Groq", "DeepSeek", "OpenRouter")
                if (isApiKeyConfigured) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1924)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFF00E676)))
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF004D40)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
                                    }
                                    Column {
                                        Text(
                                            text = "✨ $activeCompany Voice Active",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = activeVoiceProfile,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontSize = 11.sp,
                                            color = Color(0xFF80CBC4)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        onDismiss()
                                        viewModel.openDialog("API_SETTINGS")
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Settings, contentDescription = "API Settings", tint = Color(0xFF80D8FF), modifier = Modifier.size(18.dp))
                                }
                            }

                            // Quick Company Switcher Chips
                            Text(
                                text = "Company Voice Switch Karein:",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFB0BEC5),
                                fontSize = 10.sp
                            )
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(companyList) { company ->
                                    val isCurrent = company == activeCompany
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isCurrent) Color(0xFF004D40) else Color(0xFF13222E),
                                        border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E676)) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF263238)),
                                        modifier = Modifier.clickable {
                                            viewModel.selectActiveCompany(company)
                                        }
                                    ) {
                                        Text(
                                            text = if (isCurrent) "✓ $company" else company,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isCurrent) Color(0xFFB9F6CA) else Color(0xFF90A4AE),
                                            fontSize = 10.sp,
                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1410)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF7043)))
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF3E2723)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Add Company API Key for Custom Voice",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD54F)
                                    )
                                    Text(
                                        text = "Jis company ki key add karenge, MJ uski original voice activate karegi.",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = Color(0xFFD7CCC8)
                                    )
                                }
                            }

                            // Company Selector Chips
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(companyList) { company ->
                                    val isSelected = company == selectedCompanyForInput
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) Color(0xFF4E2C11) else Color(0xFF271B16),
                                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB300)) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3E2723)),
                                        modifier = Modifier.clickable {
                                            selectedCompanyForInput = company
                                        }
                                    ) {
                                        Text(
                                            text = company,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) Color(0xFFFFD54F) else Color(0xFFA1887F),
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = apiKeyInput,
                                    onValueChange = {
                                        apiKeyInput = it
                                        val t = it.trim()
                                        if (t.startsWith("sk-") && !t.startsWith("sk-or-")) {
                                            selectedCompanyForInput = "OpenAI"
                                        } else if (t.startsWith("gsk_")) {
                                            selectedCompanyForInput = "Groq"
                                        } else if (t.startsWith("AIza")) {
                                            selectedCompanyForInput = "Google Gemini"
                                        } else if (t.startsWith("sk-or-")) {
                                            selectedCompanyForInput = "OpenRouter"
                                        }
                                    },
                                    placeholder = { Text("$selectedCompanyForInput Key...", color = Color(0xFF8D6E63), fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    trailingIcon = {
                                        TextButton(
                                            onClick = {
                                                val clip = clipboardManager.getText()?.text
                                                if (!clip.isNullOrBlank()) {
                                                    apiKeyInput = clip.trim()
                                                    val t = clip.trim()
                                                    if (t.startsWith("sk-") && !t.startsWith("sk-or-")) {
                                                        selectedCompanyForInput = "OpenAI"
                                                    } else if (t.startsWith("gsk_")) {
                                                        selectedCompanyForInput = "Groq"
                                                    } else if (t.startsWith("AIza")) {
                                                        selectedCompanyForInput = "Google Gemini"
                                                    } else if (t.startsWith("sk-or-")) {
                                                        selectedCompanyForInput = "OpenRouter"
                                                    }
                                                    viewModel.showToast("Clipboard se paste ho gaya!")
                                                }
                                            },
                                            contentPadding = PaddingValues(horizontal = 4.dp)
                                        ) {
                                            Text("PASTE", fontSize = 10.sp, color = Color(0xFFFFB300), fontWeight = FontWeight.Bold)
                                        }
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFB300),
                                        unfocusedBorderColor = Color(0xFF4E342E),
                                        focusedContainerColor = Color(0xFF130A10),
                                        unfocusedContainerColor = Color(0xFF130A10),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    singleLine = true
                                )

                                Button(
                                    onClick = {
                                        if (apiKeyInput.isNotBlank()) {
                                            viewModel.saveQuickApiKey(apiKeyInput, selectedCompanyForInput)
                                        } else {
                                            viewModel.showToast("Kripya API Key paste karein")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00)),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Activate Voice", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                            }
                        }
                    }
                }

                // Permanent Mic Active Badge
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF071F14)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(Color(0xFF00E676), Color(0xFF00E5FF)))
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00E676))
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🟢 Mic Permanent Chalu Hai (Always Listening)",
                                color = Color(0xFFB9F6CA),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Aap jab chahein bolein, MJ turant sunegi aur command execute karegi.",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = Color(0xFF80CBC4)
                            )
                        }
                    }
                }

                // Active Voice Model Label
                Text(
                    text = "Active Voice: $activeVoiceName",
                    style = MaterialTheme.typography.labelSmall,
                    color = MyraTextMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Voice Selector Carousel if toggled
                AnimatedVisibility(visible = showVoiceSelector) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                        shape = RoundedCornerShape(14.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MyraRed, MyraNeonCyan)))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "Select Voice Model",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(availableVoices.take(8)) { voice ->
                                    val isSel = voice.name == activeVoiceName
                                    Surface(
                                        onClick = {
                                            viewModel.selectVoiceModel(voice.id)
                                            viewModel.playVoicePreview(voice)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSel) MyraRed else MyraCardDark,
                                        border = CardDefaults.outlinedCardBorder().copy(
                                            brush = Brush.horizontalGradient(listOf(MyraCardBorder, MyraCardBorderGlow))
                                        )
                                    ) {
                                        Text(
                                            text = voice.name,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2. Central Animated Visualizer Orb
                AnimatedOrb(
                    settings = orbSettings.copy(sizeScale = 1.0f),
                    isListening = isListening,
                    isSpeaking = isSpeaking,
                    onClick = {
                        consecutiveErrorCount = 0
                        if (isSpeaking) {
                            viewModel.playActiveInterruptSound()
                            viewModel.stopSpeaking()
                            startSpeechListening()
                        } else {
                            if (!hasAudioPermission) {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            } else {
                                viewModel.playActiveWakeSound()
                                startSpeechListening()
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3. User Speech & AI Voice Response Cards
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // User Voice Display
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Default.Mic else Icons.Default.Hearing,
                                contentDescription = null,
                                tint = if (isListening) MyraRed else Color(0xFF00E676),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isListening) listeningStatus else "🟢 Mic Permanent Chalu Hai • Boliye",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isListening) MyraRed else Color(0xFF80D8FF)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = userSpokenText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MyraTextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MyraDivider
                        )

                        // MJ Spoken Response Display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.AutoMirrored.Filled.VolumeUp else Icons.Default.VolumeMute,
                                    contentDescription = null,
                                    tint = if (isSpeaking) MyraNeonCyan else MyraRedGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (isSpeaking) "MJ Bol Rahi Hai..." else "MJ Response:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSpeaking) MyraNeonCyan else MyraRedGlow,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Repeat Voice Button
                            IconButton(
                                onClick = {
                                    if (isSpeaking) {
                                        viewModel.playActiveInterruptSound()
                                        viewModel.stopSpeaking()
                                        startSpeechListening()
                                    } else {
                                        viewModel.speak(currentAiResponse)
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.Default.StopCircle else Icons.Default.Replay,
                                    contentDescription = if (isSpeaking) "Stop" else "Replay Voice",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentAiResponse,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 4. Quick Voice Suggestions
                Text(
                    text = "Bolne ke liye suggestions (Tap to Speak)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MyraTextMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quickSampleCommands) { cmd ->
                        Surface(
                            onClick = {
                                userSpokenText = cmd
                                isListening = false
                                viewModel.performHaptic()
                                viewModel.processVoiceInput(cmd)
                                onSendCommand(cmd)
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = MyraCardDark,
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(listOf(MyraCardBorder, MyraCardBorderGlow))
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = MyraRedGlow,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = cmd,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MyraTextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 5. Mic Permission Banner if not granted
                if (!hasAudioPermission) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E0C15))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Microphone Permission Chahiye", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                Text("MJ se aawaz mein baat karne ke liye mic allow karein.", color = MyraTextMuted, style = MaterialTheme.typography.labelSmall)
                            }
                            Button(
                                onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
                                colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Allow", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // 6. Manual Type / Voice fallback input row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = manualInputText,
                        onValueChange = { manualInputText = it },
                        placeholder = { Text("Ya command type karein...", color = MyraTextMuted, fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MyraRed,
                            unfocusedBorderColor = Color(0xFF26263A),
                            focusedContainerColor = Color(0xFF12121D),
                            unfocusedContainerColor = Color(0xFF12121D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (manualInputText.isNotBlank()) {
                                val text = manualInputText
                                userSpokenText = text
                                manualInputText = ""
                                viewModel.performHaptic()
                                viewModel.processVoiceInput(text)
                                onSendCommand(text)
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MyraRed)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Command", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 7. Main Bottom Voice Conversation / Status Button
                Button(
                    onClick = {
                        if (!hasAudioPermission) {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        } else if (isSpeaking) {
                            viewModel.stopSpeaking()
                            startSpeechListening()
                        } else {
                            startSpeechListening()
                            viewModel.showToast("Mic permanent chalu hai! Boliye MJ sun rahi hai.")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("voice_mode_mic_toggle"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSpeaking) Color(0xFF00838F) else Color(0xFF1B5E20)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.StopCircle else Icons.Default.Mic,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = when {
                            !hasAudioPermission -> "Mic Permission Allow Karein"
                            isSpeaking -> "MJ Bol Rahi Hai • Tap to Interrupt"
                            else -> "🟢 Mic Permanent Chalu Hai (MJ Sun Rahi Hai)"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun VoicePipelineVisualizerCard(
    currentStage: VoicePipelineStage,
    selectedTtsEngine: TtsEngine,
    elevenLabsVoiceName: String,
    ttsEngineStatus: String,
    elevenLabsVoices: List<ElevenLabsVoice>,
    selectedElevenLabsVoiceId: String,
    elevenLabsApiKey: String,
    onSelectEngine: (TtsEngine) -> Unit,
    onSelectElevenLabsVoice: (ElevenLabsVoice) -> Unit,
    onSaveApiKey: (String) -> Unit,
    onTestVoice: () -> Unit,
    onTestSpecificVoice: (ElevenLabsVoice) -> Unit = {},
    onSyncLiveVoices: () -> Unit = {},
    showDrawer: Boolean,
    onToggleDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    var inputKey by remember(elevenLabsApiKey) { mutableStateOf(elevenLabsApiKey) }
    var selectedCategoryTab by remember { mutableStateOf("🇮🇳 Hindi") }
    var voiceSearchQuery by remember { mutableStateOf("") }

    val filteredElevenLabsVoices = remember(elevenLabsVoices, selectedCategoryTab, voiceSearchQuery) {
        elevenLabsVoices.filter { voice ->
            val matchesTab = when (selectedCategoryTab) {
                "🇮🇳 Hindi" -> voice.languageGroup == "Hindi" || voice.isHindiOptimized
                "🤖 ChatGPT" -> voice.languageGroup == "ChatGPT" || voice.category.contains("ChatGPT", ignoreCase = true)
                "✨ Gemini AI" -> voice.languageGroup == "Gemini AI" || voice.category.contains("Gemini", ignoreCase = true)
                "🇺🇸 English" -> voice.languageGroup == "English"
                "🌐 All Languages" -> voice.languageGroup == "Global / All"
                else -> true
            }
            val matchesQuery = voiceSearchQuery.isBlank() ||
                    voice.name.contains(voiceSearchQuery, ignoreCase = true) ||
                    voice.language.contains(voiceSearchQuery, ignoreCase = true) ||
                    voice.accent.contains(voiceSearchQuery, ignoreCase = true) ||
                    voice.description.contains(voiceSearchQuery, ignoreCase = true)
            matchesTab && matchesQuery
        }
    }

    // Pulsing alpha for active step
    val infiniteTransition = rememberInfiniteTransition(label = "pipelinePulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("voice_pipeline_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F101A)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (currentStage != VoicePipelineStage.IDLE) {
                    listOf(Color(0xFF00E5FF), Color(0xFFFF1744))
                } else {
                    listOf(Color(0xFF262638), Color(0xFF1E1E2C))
                }
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1. Header: Live Pipeline Stage
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentStage != VoicePipelineStage.IDLE) {
                                    Color(0xFF00E676).copy(alpha = pulseAlpha)
                                } else {
                                    Color(0xFF757575)
                                }
                            )
                    )
                    Text(
                        text = "⚡ REAL-TIME VOICE PIPELINE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }

                // Active stage badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (currentStage) {
                        VoicePipelineStage.IDLE -> Color(0xFF1C1C28)
                        VoicePipelineStage.USER_SPEAKING -> Color(0xFF3E1218)
                        VoicePipelineStage.SPEECH_TO_TEXT -> Color(0xFF122C34)
                        VoicePipelineStage.MYRA_AI_BRAIN -> Color(0xFF2E1236)
                        VoicePipelineStage.AI_ANSWER -> Color(0xFF342812)
                        VoicePipelineStage.ELEVENLABS_TTS -> Color(0xFF0A2B3D)
                        VoicePipelineStage.MYRA_SPEAKING -> Color(0xFF003828)
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (currentStage != VoicePipelineStage.IDLE) Color(0xFF00E5FF).copy(alpha = pulseAlpha) else Color(0xFF333344)
                    )
                ) {
                    Text(
                        text = if (currentStage == VoicePipelineStage.IDLE) {
                            "⏸️ STANDBY"
                        } else {
                            "${currentStage.icon} ${currentStage.titleHindi}"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (currentStage != VoicePipelineStage.IDLE) Color(0xFF80D8FF) else Color(0xFF9E9E9E),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Subtitle with full arrow sequence
            Text(
                text = "आप बोलेंगे → Speech-to-Text → MYRA AI Brain → AI जवाब → ElevenLabs TTS → 🔊 MYRA आवाज़",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                color = Color(0xFF9E9EA8),
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            // 2. Interactive 6-Stage Diagram (Row 1: Stages 1-3, Row 2: Stages 4-6)
            // ROW 1: Stages 1, 2, 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PipelineStageItem(
                    stage = VoicePipelineStage.USER_SPEAKING,
                    isActive = currentStage == VoicePipelineStage.USER_SPEAKING,
                    isPassed = currentStage.stageNumber > VoicePipelineStage.USER_SPEAKING.stageNumber,
                    pulseAlpha = pulseAlpha,
                    modifier = Modifier.weight(1f)
                )

                Text("→", color = Color(0xFF555566), fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 2.dp))

                PipelineStageItem(
                    stage = VoicePipelineStage.SPEECH_TO_TEXT,
                    isActive = currentStage == VoicePipelineStage.SPEECH_TO_TEXT,
                    isPassed = currentStage.stageNumber > VoicePipelineStage.SPEECH_TO_TEXT.stageNumber,
                    pulseAlpha = pulseAlpha,
                    modifier = Modifier.weight(1f)
                )

                Text("→", color = Color(0xFF555566), fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 2.dp))

                PipelineStageItem(
                    stage = VoicePipelineStage.MYRA_AI_BRAIN,
                    isActive = currentStage == VoicePipelineStage.MYRA_AI_BRAIN,
                    isPassed = currentStage.stageNumber > VoicePipelineStage.MYRA_AI_BRAIN.stageNumber,
                    pulseAlpha = pulseAlpha,
                    modifier = Modifier.weight(1f)
                )
            }

            // Central Downward Connector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "↓",
                    color = Color(0xFF00E5FF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 40.dp)
                )
            }

            // ROW 2: Stages 4, 5, 6
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PipelineStageItem(
                    stage = VoicePipelineStage.AI_ANSWER,
                    isActive = currentStage == VoicePipelineStage.AI_ANSWER,
                    isPassed = currentStage.stageNumber > VoicePipelineStage.AI_ANSWER.stageNumber,
                    pulseAlpha = pulseAlpha,
                    modifier = Modifier.weight(1f)
                )

                Text("→", color = Color(0xFF555566), fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 2.dp))

                PipelineStageItem(
                    stage = VoicePipelineStage.ELEVENLABS_TTS,
                    isActive = currentStage == VoicePipelineStage.ELEVENLABS_TTS,
                    isPassed = currentStage.stageNumber > VoicePipelineStage.ELEVENLABS_TTS.stageNumber,
                    pulseAlpha = pulseAlpha,
                    modifier = Modifier.weight(1f)
                )

                Text("→", color = Color(0xFF555566), fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 2.dp))

                PipelineStageItem(
                    stage = VoicePipelineStage.MYRA_SPEAKING,
                    isActive = currentStage == VoicePipelineStage.MYRA_SPEAKING,
                    isPassed = false,
                    pulseAlpha = pulseAlpha,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 3. ElevenLabs & TTS Engine Quick Bar
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF141524),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26273C)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "TTS: ${selectedTtsEngine.displayName}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF004D40)
                            ) {
                                Text(
                                    text = selectedTtsEngine.badge,
                                    fontSize = 9.sp,
                                    color = Color(0xFF69F0AE),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = if (selectedTtsEngine == TtsEngine.ELEVEN_LABS) "Voice: $elevenLabsVoiceName" else ttsEngineStatus,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.sp,
                            color = Color(0xFF80CBC4)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Test Voice Button
                        IconButton(
                            onClick = onTestVoice,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1F1F35))
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Test Voice",
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Toggle Settings Button
                        Surface(
                            onClick = onToggleDrawer,
                            shape = RoundedCornerShape(6.dp),
                            color = if (showDrawer) Color(0xFF004D40) else Color(0xFF212236),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (showDrawer) Color(0xFF00E676) else Color(0xFF33344C)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = if (showDrawer) Color(0xFF00E676) else Color(0xFFB0BEC5),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = if (showDrawer) "Close" else "Voices",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (showDrawer) Color(0xFF00E676) else Color.White
                                )
                            }
                        }
                    }
                }
            }

            // 4. Expandable ElevenLabs Configuration Panel
            AnimatedVisibility(visible = showDrawer) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Engine Switcher
                    Text("Select TTS Engine:", style = MaterialTheme.typography.labelSmall, color = Color(0xFFB0BEC5), fontSize = 10.sp)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(TtsEngine.entries.toTypedArray()) { engine ->
                            val isSel = engine == selectedTtsEngine
                            Surface(
                                onClick = { onSelectEngine(engine) },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) Color(0xFF004D40) else Color(0xFF161726),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) Color(0xFF00E676) else Color(0xFF2A2B40)
                                )
                            ) {
                                Text(
                                    text = if (isSel) "✓ ${engine.displayName}" else engine.displayName,
                                    fontSize = 10.sp,
                                    color = if (isSel) Color(0xFFB9F6CA) else Color(0xFFB0BEC5),
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    // ElevenLabs Voices Library (Hindi + English + Global All Languages)
                    if (selectedTtsEngine == TtsEngine.ELEVEN_LABS) {
                        val categories = listOf("🇮🇳 Hindi", "🤖 ChatGPT", "✨ Gemini AI", "🇺🇸 English", "🌐 All Languages", "⭐ All")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "ElevenLabs Voices (${filteredElevenLabsVoices.size} voices):",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF80CBC4),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (elevenLabsApiKey.isNotBlank()) {
                                TextButton(
                                    onClick = { onSyncLiveVoices() },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Sync My Account", color = Color(0xFF00E5FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Category Filter Tabs
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(categories) { cat ->
                                val count = when (cat) {
                                    "🇮🇳 Hindi" -> elevenLabsVoices.count { it.languageGroup == "Hindi" || it.isHindiOptimized }
                                    "🤖 ChatGPT" -> elevenLabsVoices.count { it.languageGroup == "ChatGPT" || it.category.contains("ChatGPT", ignoreCase = true) }
                                    "✨ Gemini AI" -> elevenLabsVoices.count { it.languageGroup == "Gemini AI" || it.category.contains("Gemini", ignoreCase = true) }
                                    "🇺🇸 English" -> elevenLabsVoices.count { it.languageGroup == "English" }
                                    "🌐 All Languages" -> elevenLabsVoices.count { it.languageGroup == "Global / All" }
                                    else -> elevenLabsVoices.size
                                }
                                val isSelected = selectedCategoryTab == cat
                                Surface(
                                    onClick = { selectedCategoryTab = cat },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) Color(0xFF004D40) else Color(0xFF161726),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF00E676) else Color(0xFF2A2B40)
                                    )
                                ) {
                                    Text(
                                        text = "$cat ($count)",
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color(0xFFB9F6CA) else Color(0xFFB0BEC5),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Search Field
                        OutlinedTextField(
                            value = voiceSearchQuery,
                            onValueChange = { voiceSearchQuery = it },
                            placeholder = { Text("Search voice by name, accent, or language...", color = Color(0xFF6E6E82), fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF80CBC4), modifier = Modifier.size(14.dp))
                            },
                            trailingIcon = {
                                if (voiceSearchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { voiceSearchQuery = "" },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF80CBC4), modifier = Modifier.size(14.dp))
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF00E5FF),
                                unfocusedBorderColor = Color(0xFF2A2B40),
                                focusedContainerColor = Color(0xFF131422),
                                unfocusedContainerColor = Color(0xFF131422),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        // Voices Horizontal Carousel with Rich Cards
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(filteredElevenLabsVoices) { voice ->
                                val isSel = voice.voiceId == selectedElevenLabsVoiceId
                                Card(
                                    modifier = Modifier
                                        .width(170.dp)
                                        .clickable {
                                            onSelectElevenLabsVoice(voice)
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSel) Color(0xFF33101E) else Color(0xFF161728)
                                    ),
                                    border = CardDefaults.outlinedCardBorder().copy(
                                        brush = Brush.horizontalGradient(
                                            if (isSel) listOf(Color(0xFFFF1744), Color(0xFF00E5FF))
                                            else listOf(Color(0xFF282A42), Color(0xFF1E1F30))
                                        )
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(voice.flagEmoji, fontSize = 14.sp)
                                                Text(
                                                    text = voice.name,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSel) Color(0xFFFF8A80) else Color.White,
                                                    maxLines = 1
                                                )
                                            }
                                            if (isSel) {
                                                Text("✓", color = Color(0xFF00E676), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            }
                                        }

                                        Text(
                                            text = "${voice.language} • ${voice.gender.displayName}",
                                            fontSize = 9.sp,
                                            color = Color(0xFF80D8FF),
                                            maxLines = 1
                                        )

                                        Text(
                                            text = voice.accent,
                                            fontSize = 9.sp,
                                            color = Color(0xFFB0BEC5),
                                            maxLines = 1
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                onClick = {
                                                    onSelectElevenLabsVoice(voice)
                                                    onTestSpecificVoice(voice)
                                                },
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color(0xFF242640)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                                ) {
                                                    Icon(Icons.Default.VolumeUp, contentDescription = "Test", tint = Color(0xFF00E5FF), modifier = Modifier.size(10.dp))
                                                    Text("Test", fontSize = 9.sp, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
                                                }
                                            }

                                            Text(
                                                text = if (isSel) "ACTIVE" else "SELECT",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSel) Color(0xFFFF5252) else Color(0xFF78909C)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // ElevenLabs API Key Input Field
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedTextField(
                                value = inputKey,
                                onValueChange = { inputKey = it },
                                placeholder = { Text("ElevenLabs API Key (xi-api-key)...", color = Color(0xFF757585), fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF00E5FF),
                                    unfocusedBorderColor = Color(0xFF2A2B40),
                                    focusedContainerColor = Color(0xFF131422),
                                    unfocusedContainerColor = Color(0xFF131422),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                singleLine = true
                            )

                            // Paste Button
                            IconButton(
                                onClick = {
                                    val clip = clipboardManager.getText()?.text?.trim()
                                    if (!clip.isNullOrBlank()) {
                                        inputKey = clip
                                    }
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1A1B2E))
                            ) {
                                Icon(Icons.Default.ContentPaste, contentDescription = "Paste Key", tint = Color(0xFF80D8FF), modifier = Modifier.size(16.dp))
                            }

                            // Save Button
                            Button(
                                onClick = { onSaveApiKey(inputKey) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("Save", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PipelineStageItem(
    stage: VoicePipelineStage,
    isActive: Boolean,
    isPassed: Boolean,
    pulseAlpha: Float,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 2.dp),
        shape = RoundedCornerShape(8.dp),
        color = when {
            isActive -> Color(0xFF201326)
            isPassed -> Color(0xFF0D1E16)
            else -> Color(0xFF141420)
        },
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                isActive -> Color(0xFF00E5FF).copy(alpha = pulseAlpha)
                isPassed -> Color(0xFF00C853)
                else -> Color(0xFF262638)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stage.icon,
                    fontSize = 13.sp
                )
                if (isPassed) {
                    Text(
                        text = "✓",
                        fontSize = 10.sp,
                        color = Color(0xFF00E676),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = stage.titleHindi,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                color = when {
                    isActive -> Color(0xFF00E5FF)
                    isPassed -> Color(0xFF81C784)
                    else -> Color(0xFF9E9EA8)
                },
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = stage.titleEng,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 8.sp,
                color = if (isActive) Color(0xFF80D8FF) else Color(0xFF6B6B78),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

