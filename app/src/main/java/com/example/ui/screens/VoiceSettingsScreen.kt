package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VoiceSettings
import com.example.viewmodel.MyraViewModel
import java.util.Locale

private val VoiceDarkCanvasBg = Color(0xFF07070B)
private val VoiceDarkCardBg = Color(0xFF10101A)
private val VoiceCardBorder = Color(0xFF1C1C2A)
private val VoiceDarkPillBg = Color(0xFF161622)
private val VoiceVibrantRed = Color(0xFFFF002E)
private val VoiceInactiveTrackColor = Color(0xFFECE6F0)
private val VoiceMutedTextColor = Color(0xFF9E9EA8)
private val VoiceSectionHeaderColor = Color(0xFFFF7A59)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSettingsScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentVoiceSettings by viewModel.voiceSettings.collectAsState()

    var speed by remember(currentVoiceSettings) { mutableFloatStateOf(currentVoiceSettings.speed) }
    var pitch by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.pitch) }

    var fastResponseMode by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.fastResponseMode) }
    var naturalPauses by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.naturalPauses) }
    var expressiveVoice by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.expressiveVoice) }
    var interruptWhileSpeaking by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.interruptWhileSpeaking) }
    var autoStopWhenUserStartsTalking by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.autoStopWhenUserStartsTalking) }
    var continueSpeakingAfterInterruption by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.continueSpeakingAfterInterruption) }

    var noiseSuppression by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.noiseSuppression) }
    var echoCancellation by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.echoCancellation) }
    var autoMicGain by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.autoMicGain) }
    var voiceActivityDetection by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.voiceActivityDetection) }
    var backgroundNoiseFilter by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.backgroundNoiseFilter) }

    var audioQuality by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.audioQuality) }
    var streamingResponse by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.streamingResponse) }
    var latencyMode by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.latencyMode) }
    var reconnectAutomatically by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.reconnectAutomatically) }
    var voiceTimeout by remember(currentVoiceSettings) { mutableStateOf(currentVoiceSettings.voiceTimeout) }

    // Helper to persist whenever a setting changes
    val saveCurrentSettings: () -> Unit = {
        viewModel.updateVoiceSettings(
            VoiceSettings(
                speed = speed,
                pitch = pitch,
                fastResponseMode = fastResponseMode,
                naturalPauses = naturalPauses,
                expressiveVoice = expressiveVoice,
                interruptWhileSpeaking = interruptWhileSpeaking,
                autoStopWhenUserStartsTalking = autoStopWhenUserStartsTalking,
                continueSpeakingAfterInterruption = continueSpeakingAfterInterruption,
                noiseSuppression = noiseSuppression,
                echoCancellation = echoCancellation,
                autoMicGain = autoMicGain,
                voiceActivityDetection = voiceActivityDetection,
                backgroundNoiseFilter = backgroundNoiseFilter,
                audioQuality = audioQuality,
                streamingResponse = streamingResponse,
                latencyMode = latencyMode,
                reconnectAutomatically = reconnectAutomatically,
                voiceTimeout = voiceTimeout
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = VoiceDarkCanvasBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.performHaptic()
                        onNavigateBack()
                    },
                    modifier = Modifier.testTag("btn_voice_settings_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Column {
                    Text(
                        text = "Voice Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Tune how MJ sounds and listens",
                        style = MaterialTheme.typography.bodySmall,
                        color = VoiceMutedTextColor
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 48.dp, top = 8.dp)
        ) {
            // 1. VOICE SPEED
            item {
                SectionHeader(title = "VOICE SPEED")
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_voice_speed"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = VoiceDarkCardBg),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(VoiceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Speed",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = String.format(Locale.US, "%.1fx", speed),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = VoiceSectionHeaderColor
                            )
                        }

                        Text(
                            text = "How fast MJ talks. Applied to speech output (AudioTrack playback speed) - your own speaking/listening speed is unaffected.",
                            style = MaterialTheme.typography.bodySmall,
                            color = VoiceMutedTextColor,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Custom Styled Slider (0.5x to 2.0x)
                        VoiceCustomSlider(
                            value = ((speed - 0.5f) / 1.5f).coerceIn(0f, 1f),
                            onValueChange = { fraction ->
                                val newSpeed = (0.5f + fraction * 1.5f)
                                speed = Math.round(newSpeed * 10f) / 10f
                                saveCurrentSettings()
                            },
                            thumbColor = VoiceVibrantRed,
                            activeColor = VoiceVibrantRed,
                            testTag = "slider_voice_speed"
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "0.5x", style = MaterialTheme.typography.bodySmall, color = VoiceMutedTextColor)
                            Text(text = "2.0x", style = MaterialTheme.typography.bodySmall, color = VoiceMutedTextColor)
                        }
                    }
                }
            }

            // 2. VOICE PITCH
            item {
                SectionHeader(title = "VOICE PITCH")
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_voice_pitch"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = VoiceDarkCardBg),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(VoiceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Only changes MJ's voice - never your microphone input.",
                            style = MaterialTheme.typography.bodySmall,
                            color = VoiceMutedTextColor,
                            lineHeight = 18.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("Low", "Normal", "High").forEach { option ->
                                val isSelected = pitch.equals(option, ignoreCase = true)
                                Surface(
                                    onClick = {
                                        pitch = option
                                        viewModel.performHaptic()
                                        saveCurrentSettings()
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSelected) VoiceVibrantRed else VoiceDarkPillBg,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .testTag("pitch_option_$option")
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = option,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else Color(0xFFD1D1DB),
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. SPEAKING BEHAVIOR
            item {
                SectionHeader(title = "SPEAKING BEHAVIOR")
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_speaking_behavior"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = VoiceDarkCardBg),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(VoiceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        SettingToggleItem(
                            title = "⚡ Fast Response Mode",
                            description = "Starts speaking sooner by splitting replies into smaller pieces instead of waiting for the whole thing.",
                            checked = fastResponseMode,
                            onCheckedChange = {
                                fastResponseMode = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_fast_response_mode"
                        )

                        SettingToggleItem(
                            title = "Natural Pauses",
                            description = "A short beat between sentences instead of running them together.",
                            checked = naturalPauses,
                            onCheckedChange = {
                                naturalPauses = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_natural_pauses"
                        )

                        SettingToggleItem(
                            title = "Expressive Voice",
                            description = "Natural pacing, emphasis and emotion. Off = flatter, more consistent delivery.",
                            checked = expressiveVoice,
                            onCheckedChange = {
                                expressiveVoice = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_expressive_voice"
                        )

                        SettingToggleItem(
                            title = "Interrupt MJ While Speaking",
                            description = "Let you cut in and start talking any time, even mid-sentence.",
                            checked = interruptWhileSpeaking,
                            onCheckedChange = {
                                interruptWhileSpeaking = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_interrupt_while_speaking"
                        )

                        SettingToggleItem(
                            title = "Auto Stop When User Starts Talking",
                            description = "Cuts MJ's audio instantly on interruption. Off = lets the current buffered audio finish draining instead of an abrupt cut.",
                            checked = autoStopWhenUserStartsTalking,
                            onCheckedChange = {
                                autoStopWhenUserStartsTalking = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_auto_stop"
                        )

                        SettingToggleItem(
                            title = "Continue Speaking After Interruption",
                            description = "Adds a brief grace period before honoring an interruption, so a short blip doesn't cut MJ off instantly.",
                            checked = continueSpeakingAfterInterruption,
                            onCheckedChange = {
                                continueSpeakingAfterInterruption = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_continue_speaking"
                        )
                    }
                }
            }

            // 4. VOICE DETECTION
            item {
                SectionHeader(title = "VOICE DETECTION")
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_voice_detection"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = VoiceDarkCardBg),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(VoiceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        SettingToggleItem(
                            title = "Noise Suppression",
                            description = "Reduces background noise from the microphone using the device's real audio effect, where supported.",
                            checked = noiseSuppression,
                            onCheckedChange = {
                                noiseSuppression = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_noise_suppression"
                        )

                        SettingToggleItem(
                            title = "Echo Cancellation",
                            description = "Stops MJ's own voice from being picked up as if you said it - matters most on speakerphone.",
                            checked = echoCancellation,
                            onCheckedChange = {
                                echoCancellation = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_echo_cancellation"
                        )

                        SettingToggleItem(
                            title = "Automatic Microphone Gain",
                            description = "Keeps your voice at a usable volume automatically.",
                            checked = autoMicGain,
                            onCheckedChange = {
                                autoMicGain = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_auto_mic_gain"
                        )

                        SettingToggleItem(
                            title = "Voice Activity Detection",
                            description = "How eagerly MJ decides you've finished talking. This can't be fully disabled - it's how she knows when to reply at all - but this controls its sensitivity.",
                            checked = voiceActivityDetection,
                            onCheckedChange = {
                                voiceActivityDetection = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_vad"
                        )

                        SettingToggleItem(
                            title = "Background Noise Filter",
                            description = "Uses the same noise-suppression effect as above for persistent background sound (fans, traffic).",
                            checked = backgroundNoiseFilter,
                            onCheckedChange = {
                                backgroundNoiseFilter = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_bg_noise_filter"
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Noise suppression, echo cancellation and mic gain take effect the next time MJ starts listening.",
                            style = MaterialTheme.typography.bodySmall,
                            color = VoiceMutedTextColor,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // 5. ADVANCED
            item {
                SectionHeader(title = "ADVANCED")
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_advanced_voice"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = VoiceDarkCardBg),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(VoiceCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        // Audio Quality
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Audio Quality",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "More buffer headroom = fewer glitches, a little more latency. Gemini Live's sample rate itself is fixed by the API and can't be changed here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VoiceMutedTextColor,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                listOf("Low", "Standard", "High").forEach { quality ->
                                    val isSelected = audioQuality.equals(quality, ignoreCase = true)
                                    Surface(
                                        onClick = {
                                            audioQuality = quality
                                            viewModel.performHaptic()
                                            saveCurrentSettings()
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSelected) VoiceVibrantRed else VoiceDarkPillBg,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(46.dp)
                                            .testTag("quality_option_$quality")
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = quality,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color.White else Color(0xFFD1D1DB),
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Sample Rate (Informational item)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Sample Rate",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Auto (16kHz in / 24kHz out) - the only rate Gemini Live's API actually supports, so no other value is offered here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VoiceMutedTextColor,
                                lineHeight = 18.sp
                            )
                        }

                        // Streaming Response
                        SettingToggleItem(
                            title = "Streaming Response",
                            description = "Plays MJ's reply as audio arrives instead of waiting for the whole thing to be ready.",
                            checked = streamingResponse,
                            onCheckedChange = {
                                streamingResponse = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_streaming_response"
                        )

                        // Latency Mode
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Latency Mode",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "A shortcut that sets Streaming/Fast Response/Natural Pauses/Audio Quality together - you can still fine-tune any of them afterwards.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VoiceMutedTextColor,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                listOf("Ultra Fast", "Balanced", "Quality").forEach { mode ->
                                    val isSelected = latencyMode.equals(mode, ignoreCase = true)
                                    Surface(
                                        onClick = {
                                            latencyMode = mode
                                            when (mode) {
                                                "Ultra Fast" -> {
                                                    streamingResponse = true
                                                    fastResponseMode = true
                                                    naturalPauses = false
                                                    audioQuality = "Standard"
                                                }
                                                "Balanced" -> {
                                                    streamingResponse = true
                                                    fastResponseMode = true
                                                    naturalPauses = true
                                                    audioQuality = "Standard"
                                                }
                                                "Quality" -> {
                                                    streamingResponse = true
                                                    fastResponseMode = false
                                                    naturalPauses = true
                                                    audioQuality = "High"
                                                }
                                            }
                                            viewModel.performHaptic()
                                            saveCurrentSettings()
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSelected) VoiceVibrantRed else VoiceDarkPillBg,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(46.dp)
                                            .testTag("latency_mode_$mode")
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = mode,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color.White else Color(0xFFD1D1DB),
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Reconnect Automatically
                        SettingToggleItem(
                            title = "Reconnect Automatically",
                            description = "Reconnects on a dropped connection with backoff (up to 5 tries), reusing the same conversation.",
                            checked = reconnectAutomatically,
                            onCheckedChange = {
                                reconnectAutomatically = it
                                viewModel.performHaptic()
                                saveCurrentSettings()
                            },
                            testTag = "toggle_reconnect_auto"
                        )

                        // Voice Timeout
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Voice Timeout",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Ends an idle voice session after this long with no activity. Never cuts off an active response or task.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VoiceMutedTextColor,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            val timeoutOptions = listOf("10 sec", "30 sec", "60 sec", "5 min", "Never")
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(timeoutOptions) { timeout ->
                                    val isSelected = voiceTimeout.equals(timeout, ignoreCase = true)
                                    Surface(
                                        onClick = {
                                            voiceTimeout = timeout
                                            viewModel.performHaptic()
                                            saveCurrentSettings()
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSelected) VoiceVibrantRed else VoiceDarkPillBg,
                                        modifier = Modifier
                                            .height(44.dp)
                                            .testTag("timeout_option_$timeout")
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp)
                                                .fillMaxHeight(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = timeout,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color.White else Color(0xFFD1D1DB),
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = VoiceSectionHeaderColor,
        letterSpacing = 1.sp
    )
}

@Composable
private fun SettingToggleItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = VoiceMutedTextColor,
                lineHeight = 17.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = VoiceVibrantRed,
                uncheckedThumbColor = Color(0xFFA1A1AA),
                uncheckedTrackColor = Color(0xFF2B2B3D)
            )
        )
    }
}

@Composable
private fun VoiceCustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    thumbColor: Color = VoiceVibrantRed,
    activeColor: Color = VoiceVibrantRed,
    testTag: String = ""
) {
    var widthPx by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .testTag(testTag)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (widthPx > 0) {
                        val fraction = (offset.x / widthPx).coerceIn(0f, 1f)
                        onValueChange(fraction)
                    }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    if (widthPx > 0) {
                        val fraction = (change.position.x / widthPx).coerceIn(0f, 1f)
                        onValueChange(fraction)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
        ) {
            widthPx = size.width
            val trackHeight = 12.dp.toPx()
            val cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f)
            val thumbX = (value * size.width).coerceIn(0f, size.width)

            // Draw full background / Inactive Track (light lilac)
            drawRoundRect(
                color = VoiceInactiveTrackColor,
                topLeft = Offset(0f, (size.height - trackHeight) / 2f),
                size = Size(size.width, trackHeight),
                cornerRadius = cornerRadius
            )

            // Draw Active Track (Solid Red) up to thumb position
            if (thumbX > 0) {
                drawRoundRect(
                    color = activeColor,
                    topLeft = Offset(0f, (size.height - trackHeight) / 2f),
                    size = Size(thumbX, trackHeight),
                    cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f)
                )
            }

            // Draw vertical indicator bar (Thumb)
            val thumbBarWidth = 4.dp.toPx()
            val thumbBarHeight = 22.dp.toPx()
            drawRoundRect(
                color = thumbColor,
                topLeft = Offset(thumbX - thumbBarWidth / 2f, (size.height - thumbBarHeight) / 2f),
                size = Size(thumbBarWidth, thumbBarHeight),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            // Draw small red dot at the end of the inactive track
            val dotRadius = 3.dp.toPx()
            drawCircle(
                color = VoiceVibrantRed,
                radius = dotRadius,
                center = Offset(size.width - trackHeight / 2f, size.height / 2f)
            )
        }
    }
}
