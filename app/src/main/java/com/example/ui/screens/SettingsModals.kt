package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@Composable
fun VoiceAndAiModelsDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val selectedVoice by viewModel.selectedVoice.collectAsState()
    val selectedAiModel by viewModel.selectedAiModel.collectAsState()
    val speechSpeed by viewModel.speechSpeed.collectAsState()
    val speechPitch by viewModel.speechPitch.collectAsState()

    val voiceOptions = listOf(
        "Aria - Cyber AI" to "Ultra-clear neural voice with cybernetic warmth",
        "Nova - Warm Female" to "Empathetic, expressive natural tone",
        "Echo - Deep Resonance" to "Authoritative, calm command center voice",
        "Alloy - Crisp Neutral" to "High-speed analytical assistant tone"
    )

    val aiModelOptions = listOf(
        "Gemini 2.5 Flash" to "Google's ultra-low latency multimodal model",
        "Gemini 1.5 Pro" to "Deep reasoning with 2M context window",
        "GPT-4o Omnichannel" to "OpenAI flagship vision & reasoning",
        "Claude 3.5 Sonnet" to "Anthropic state-of-the-art coding and logic",
        "DeepSeek R1 Distill" to "Specialized mathematical and system reasoning"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .testTag("voice_ai_models_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Voice & AI Models",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Configure neural engine and voice synthesizers",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Voice Selection
                    item {
                        Text("Assistant Voice", fontWeight = FontWeight.Bold, color = MyraRedGlow)
                    }
                    items(voiceOptions) { (name, desc) ->
                        val isSelected = selectedVoice == name
                        Surface(
                            onClick = { viewModel.setVoice(name) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MyraRedDark.copy(alpha = 0.5f) else MyraCardDark,
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        if (isSelected) MyraRedGlow else MyraCardBorder,
                                        MyraCardBorder
                                    )
                                )
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.setVoice(name) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MyraRedGlow,
                                        unselectedColor = MyraTextMuted
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(name, fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Text(desc, style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                                }
                            }
                        }
                    }

                    // Voice Speed & Pitch Sliders
                    item {
                        Text("Speech Speed: ${String.format("%.1f", speechSpeed)}x", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                        Slider(
                            value = speechSpeed,
                            onValueChange = { viewModel.setSpeechSpeed(it) },
                            valueRange = 0.5f..2.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = MyraRedGlow,
                                activeTrackColor = MyraRed
                            )
                        )
                    }

                    // AI Model Selection
                    item {
                        Text("Primary AI Reasoning Model", fontWeight = FontWeight.Bold, color = MyraNeonCyan)
                    }
                    items(aiModelOptions) { (model, desc) ->
                        val isSelected = selectedAiModel == model
                        Surface(
                            onClick = { viewModel.setAiModel(model) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MyraNeonCyan.copy(alpha = 0.15f) else MyraCardDark,
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        if (isSelected) MyraNeonCyan else MyraCardBorder,
                                        MyraCardBorder
                                    )
                                )
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { viewModel.setAiModel(model) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MyraNeonCyan,
                                        unselectedColor = MyraTextMuted
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(model, fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Text(desc, style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save & Apply Configuration", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ApiAndCloudSettingsDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val geminiKey by viewModel.geminiApiKey.collectAsState()
    val openaiKey by viewModel.openaiApiKey.collectAsState()
    val claudeKey by viewModel.claudeApiKey.collectAsState()
    val groqKey by viewModel.groqApiKey.collectAsState()
    val firebaseSync by viewModel.firebaseSyncUrl.collectAsState()

    var tempGemini by remember { mutableStateOf(geminiKey) }
    var tempOpenAi by remember { mutableStateOf(openaiKey) }
    var tempClaude by remember { mutableStateOf(claudeKey) }
    var tempGroq by remember { mutableStateOf(groqKey) }
    var tempFirebase by remember { mutableStateOf(firebaseSync) }
    var showKeys by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .testTag("api_cloud_settings_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "API & Cloud Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Manage cloud endpoints and custom API keys",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Show Key Tokens", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = showKeys,
                                onCheckedChange = { showKeys = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = MyraRed
                                )
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = tempGemini,
                            onValueChange = { tempGemini = it },
                            label = { Text("Google Gemini API Key (Default)") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showKeys) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MyraRedGlow,
                                unfocusedBorderColor = MyraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = tempOpenAi,
                            onValueChange = { tempOpenAi = it },
                            label = { Text("OpenAI API Key (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showKeys) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MyraNeonCyan,
                                unfocusedBorderColor = MyraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            placeholder = { Text("sk-...", color = MyraTextMuted) },
                            singleLine = true
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = tempClaude,
                            onValueChange = { tempClaude = it },
                            label = { Text("Anthropic Claude Key (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showKeys) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MyraGold,
                                unfocusedBorderColor = MyraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            placeholder = { Text("sk-ant-...", color = MyraTextMuted) },
                            singleLine = true
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = tempGroq,
                            onValueChange = { tempGroq = it },
                            label = { Text("Groq / DeepSeek LPU Key (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showKeys) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MyraNeonPurple,
                                unfocusedBorderColor = MyraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            placeholder = { Text("gsk_...", color = MyraTextMuted) },
                            singleLine = true
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = tempFirebase,
                            onValueChange = { tempFirebase = it },
                            label = { Text("Firebase Realtime Cloud URL") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MyraNeonGreen,
                                unfocusedBorderColor = MyraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.updateApiKeys(tempGemini, tempOpenAi, tempClaude, tempGroq, tempFirebase)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save & Validate Keys", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VoiceAuthenticationDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val voiceAuthEnabled by viewModel.voiceAuthEnabled.collectAsState()
    val voiceAuthConfidence by viewModel.voiceAuthConfidence.collectAsState()
    val isVoiceEnrolled by viewModel.isVoiceEnrolled.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("voice_auth_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Voice Authentication",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Secure MJ commands with your biometric voiceprint",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Voiceprint Lock", fontWeight = FontWeight.Bold, color = Color.White)
                            Text(
                                if (voiceAuthEnabled) "MJ responds strictly to your voice frequency" else "Voice authentication disabled",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (voiceAuthEnabled) MyraNeonGreen else MyraTextMuted
                            )
                        }
                        Switch(
                            checked = voiceAuthEnabled,
                            onCheckedChange = { viewModel.toggleVoiceAuth() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MyraRed
                            )
                        )
                    }
                }

                Column {
                    Text(
                        "Recognition Threshold: ${(voiceAuthConfidence * 100).toInt()}%",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value = voiceAuthConfidence,
                        onValueChange = { viewModel.setVoiceAuthConfidence(it) },
                        valueRange = 0.5f..0.99f,
                        colors = SliderDefaults.colors(
                            thumbColor = MyraNeonGreen,
                            activeTrackColor = MyraNeonGreen.copy(alpha = 0.8f)
                        )
                    )
                }

                Button(
                    onClick = { viewModel.enrollVoiceprintSample() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVoiceEnrolled) MyraCardBorder else MyraRed
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isVoiceEnrolled) "Re-enroll Voiceprint (Active)" else "Enroll My Voice Print Now")
                }
            }
        }
    }
}

@Composable
fun WakeWordDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val wakeWordPhrase by viewModel.wakeWordPhrase.collectAsState()
    val wakeWordSensitivity by viewModel.wakeWordSensitivity.collectAsState()
    val backgroundListening by viewModel.backgroundListeningEnabled.collectAsState()

    val options = listOf("Hey MJ", "MJ", "Jarvis", "Computer", "Namaste MJ")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("wake_word_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Wake Word Configuration",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Choose the voice trigger to activate MJ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { phrase ->
                        val isSelected = wakeWordPhrase == phrase
                        Surface(
                            onClick = { viewModel.setWakeWord(phrase) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MyraRedDark.copy(alpha = 0.6f) else MyraCardDark,
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        if (isSelected) MyraRedGlow else MyraCardBorder,
                                        MyraCardBorder
                                    )
                                )
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(phrase, fontWeight = FontWeight.Bold, color = Color.White)
                                if (isSelected) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MyraRedGlow)
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Screen-Off Listening", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Low power wake-on-voice", style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                        }
                        Switch(
                            checked = backgroundListening,
                            onCheckedChange = { viewModel.toggleBackgroundListening() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MyraRed
                            )
                        )
                    }
                }

                Column {
                    Text(
                        "Mic Sensitivity: ${(wakeWordSensitivity * 100).toInt()}%",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value = wakeWordSensitivity,
                        onValueChange = { viewModel.setWakeWordSensitivity(it) },
                        valueRange = 0.2f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = MyraRedGlow,
                            activeTrackColor = MyraRed
                        )
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Done", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun IntelligenceAndModesDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val activeMode by viewModel.activeIntelligenceMode.collectAsState()
    val smartReading by viewModel.smartReadingEnabled.collectAsState()

    val modes = listOf(
        "Autonomous Copilot" to "Executes apps, tools, and multi-step routines with minimal confirmation",
        "Supervised Agent" to "Asks for confirmation before executing device actions or messaging",
        "Stealth Mode" to "Silent background monitor without audio feedback",
        "High Performance" to "Prioritizes speed and maximum token processing power"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("intelligence_modes_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Intelligence & Modes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Automation behaviors and context engine",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    modes.forEach { (mode, desc) ->
                        val isSelected = activeMode == mode
                        Surface(
                            onClick = { viewModel.setIntelligenceMode(mode) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MyraNeonPurple.copy(alpha = 0.25f) else MyraCardDark,
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        if (isSelected) MyraNeonPurple else MyraCardBorder,
                                        MyraCardBorder
                                    )
                                )
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(mode, fontWeight = FontWeight.Bold, color = Color.White)
                                    if (isSelected) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MyraNeonPurple, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Text(desc, style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Smart Screen OCR Reading", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Allow MJ to analyze on-screen text for smart replies", style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                        }
                        Switch(
                            checked = smartReading,
                            onCheckedChange = { viewModel.toggleSmartReading() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MyraNeonPurple
                            )
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply Mode", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PcConnectDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val pcIp by viewModel.pcBridgeIp.collectAsState()
    val isConnected by viewModel.pcBridgeConnected.collectAsState()
    val clipboardSync by viewModel.pcClipboardSync.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("pc_connect_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PC Connect & Bridge",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Remote workstation AI bridge over local Wi-Fi",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(if (isConnected) MyraNeonGreen else MyraRed)
                            )
                            Text(
                                if (isConnected) "Active Desktop Connection" else "Bridge Disconnected",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            "Bridge IP: http://$pcIp",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraNeonCyan
                        )
                        Text(
                            "Open this URL on your PC browser to control your phone, send typing commands, and stream notifications.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MyraTextMuted
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Sync Clipboard with PC", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Automatic 2-way copy paste", style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                        }
                        Switch(
                            checked = clipboardSync,
                            onCheckedChange = { viewModel.togglePcClipboardSync() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MyraNeonCyan
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.togglePcBridge() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isConnected) MyraCardBorder else MyraRed
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isConnected) "Disconnect" else "Connect Now")
                    }

                    OutlinedButton(
                        onClick = { viewModel.copyToClipboard("http://$pcIp") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy URL")
                    }
                }
            }
        }
    }
}

@Composable
fun LicenseActivationDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val currentKey by viewModel.licenseKey.collectAsState()
    val isPro by viewModel.isLicensePro.collectAsState()
    var inputKey by remember { mutableStateOf(currentKey) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("license_activation_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraGold.copy(alpha = 0.5f), MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "License Activation",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Unlock full unmetered AI & automation engine",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = MyraGold)
                        Column {
                            Text(
                                if (isPro) "PRO VIP LICENSE ACTIVE" else "FREE TIER",
                                fontWeight = FontWeight.Bold,
                                color = if (isPro) MyraGold else Color.White
                            )
                            Text(
                                if (isPro) "Lifetime access to all modules" else "Enter a key to activate VIP status",
                                style = MaterialTheme.typography.labelSmall,
                                color = MyraTextMuted
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = inputKey,
                    onValueChange = { inputKey = it },
                    label = { Text("Product License Key") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("MJ-XXXX-XXXX-XXXX") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MyraGold,
                        unfocusedBorderColor = MyraCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Button(
                    onClick = {
                        viewModel.activateLicense(inputKey)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraGold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Activate License", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun SubscriptionDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val tier by viewModel.subscriptionTier.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .wrapContentHeight()
                .testTag("subscription_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraNeonPurple, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Subscription & Plans",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Manage in-app cloud AI tiers",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(MyraNeonPurple, MyraNeonCyan))
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Active Plan", style = MaterialTheme.typography.labelMedium, color = MyraTextMuted)
                            Text("VIP ACTIVE", fontWeight = FontWeight.Bold, color = MyraNeonGreen, style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(tier, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Unlimited multimodal Gemini 2.5 calls\n• Real-time PC Screen Mirroring\n• Custom Neural Voice cloning\n• 24/7 Background automation daemon", style = MaterialTheme.typography.bodySmall, color = MyraTextSecondary)
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraNeonPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Manage Cloud Billing", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun UserProfileDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val currentName by viewModel.userProfileName.collectAsState()
    val currentEmail by viewModel.userProfileEmail.collectAsState()
    val currentBio by viewModel.userBioInstructions.collectAsState()

    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var bio by remember { mutableStateOf(currentBio) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.82f)
                .testTag("user_profile_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "User Profile",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Personalize your identity and AI custom instructions",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Display Name / Call Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MyraRedGlow,
                                unfocusedBorderColor = MyraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Linked Google Account") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MyraRedGlow,
                                unfocusedBorderColor = MyraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("AI Personalization & Custom Instructions") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MyraRedGlow,
                                unfocusedBorderColor = MyraCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            maxLines = 5
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.updateProfile(name, email, bio)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Profile", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BatchUpdateDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val version by viewModel.appVersion.collectAsState()
    val isChecking by viewModel.isCheckingUpdate.collectAsState()
    val autoUpdate by viewModel.autoUpdateEnabled.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("batch_update_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Batch & Updates",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "System OTA patches and batch automation engine",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Current System Build", style = MaterialTheme.typography.labelMedium, color = MyraTextMuted)
                        Text(version, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• Added Full 16-Settings Suite\n• Added All 10 System Permissions\n• Enhanced Cyber Orb Engine", style = MaterialTheme.typography.labelSmall, color = MyraNeonGreen)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Automatic OTA Updates", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Background bug fixes & model sync", style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                        }
                        Switch(
                            checked = autoUpdate,
                            onCheckedChange = { viewModel.toggleAutoUpdate() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MyraRed
                            )
                        )
                    }
                }

                Button(
                    onClick = { viewModel.checkForUpdates() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isChecking
                ) {
                    if (isChecking) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Checking OTA servers...")
                    } else {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Check for Updates Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AccountDialog(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit
) {
    val email by viewModel.userProfileEmail.collectAsState()
    val name by viewModel.userProfileName.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("account_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Account",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Manage session & cloud synchronization",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDark)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MyraRedDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MyraRedGlow)
                        }
                        Column {
                            Text(name, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(email, style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.logoutAccount()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out from Account", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
