package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ElevenLabsVoice
import com.example.model.PersonalityMode
import com.example.model.TtsEngine
import com.example.model.VoiceGender
import com.example.model.VoiceModelItem
import com.example.ui.components.AiSoundTheme
import com.example.ui.components.AiSoundEffectItem
import com.example.ui.components.CyberSoundManager
import com.example.viewmodel.MyraViewModel

private val VoiceScreenDarkBg = Color(0xFF07070B)
private val VoiceCardDarkBg = Color(0xFF10101A)
private val VoiceCardActiveBg = Color(0xFF1A0E14)
private val VoiceCardBorderDefault = Color(0xFF1C1C2A)
private val VoiceCardBorderActive = Color(0xFFFF1133)
private val VoiceRedAccent = Color(0xFFFF1133)
private val VoiceMutedText = Color(0xFF9E9EA8)
private val VoicePillInactiveBg = Color(0xFF141420)
private val VoicePillInactiveBorder = Color(0xFF232336)

private val FemaleBadgeBg = Color(0xFF33101C)
private val FemaleBadgeText = Color(0xFFFF5277)
private val MaleBadgeBg = Color(0xFF102338)
private val MaleBadgeText = Color(0xFF4FA5FF)
private val LangBadgeBg = Color(0xFF161624)
private val LangBadgeText = Color(0xFFA5A5B5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceModelsScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToVoiceSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val voiceModels by viewModel.voiceModels.collectAsState()
    val activeVoiceId by viewModel.activeVoiceModelId.collectAsState()
    val activePersonality by viewModel.activePersonalityMode.collectAsState()
    val playingVoiceId by viewModel.playingVoiceId.collectAsState()

    val elevenLabsVoices by viewModel.elevenLabsVoicesState.collectAsState()
    val selectedElevenLabsVoiceId by viewModel.elevenLabsVoiceId.collectAsState()
    val selectedTtsEngine by viewModel.selectedTtsEngine.collectAsState()
    val elevenLabsApiKey by viewModel.elevenLabsApiKey.collectAsState()

    val activeSoundTheme by viewModel.activeSoundTheme.collectAsState()
    var selectedProviderTab by remember { mutableStateOf("GEMINI_AI") } // "GEMINI_AI", "GPT4", "ELEVEN_LABS", "HINDI", "SOUNDS", "DEVICE"
    var selectedElevenLabsCategory by remember { mutableStateOf("⭐ All") }
    var selectedGender by remember { mutableStateOf(VoiceGender.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedSoundCategoryFilter by remember { mutableStateOf("ALL") }
    var playingSoundId by remember { mutableStateOf<String?>(null) }

    val allAiSounds = remember { CyberSoundManager.getAllAiSoundEffects() }

    val filteredAiSounds = remember(selectedSoundCategoryFilter, searchQuery) {
        allAiSounds.filter { sound ->
            val matchesCategory = when (selectedSoundCategoryFilter) {
                "GEMINI" -> sound.category.contains("Gemini", ignoreCase = true)
                "GPT4" -> sound.category.contains("GPT", ignoreCase = true) || sound.category.contains("ChatGPT", ignoreCase = true)
                "ELEVEN" -> sound.category.contains("Eleven", ignoreCase = true)
                "CYBER" -> sound.category.contains("Cyber", ignoreCase = true)
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                    sound.name.contains(searchQuery, ignoreCase = true) ||
                    sound.description.contains(searchQuery, ignoreCase = true) ||
                    sound.provider.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val filteredVoices = remember(voiceModels, selectedGender, searchQuery) {
        voiceModels.filter { voice ->
            val matchesGender = when (selectedGender) {
                VoiceGender.ALL -> true
                VoiceGender.FEMALE -> voice.gender == VoiceGender.FEMALE
                VoiceGender.MALE -> voice.gender == VoiceGender.MALE
            }
            val matchesSearch = searchQuery.isBlank() ||
                    voice.name.contains(searchQuery, ignoreCase = true) ||
                    voice.description.contains(searchQuery, ignoreCase = true)

            matchesGender && matchesSearch
        }
    }

    val filteredElevenLabsVoices = remember(elevenLabsVoices, selectedProviderTab, selectedElevenLabsCategory, selectedGender, searchQuery) {
        val baseList = when (selectedProviderTab) {
            "GEMINI_AI" -> elevenLabsVoices.filter { it.languageGroup == "Gemini AI" || it.category.contains("Gemini", ignoreCase = true) }
            "GPT4" -> elevenLabsVoices.filter { it.languageGroup == "ChatGPT" || it.category.contains("ChatGPT", ignoreCase = true) }
            "HINDI" -> elevenLabsVoices.filter { it.languageGroup == "Hindi" || it.isHindiOptimized }
            "ELEVEN_LABS" -> elevenLabsVoices.filter { it.languageGroup == "English" || it.languageGroup == "Global / All" }
            else -> elevenLabsVoices
        }

        baseList.filter { voice ->
            val matchesGender = when (selectedGender) {
                VoiceGender.ALL -> true
                VoiceGender.FEMALE -> voice.gender == VoiceGender.FEMALE
                VoiceGender.MALE -> voice.gender == VoiceGender.MALE
            }
            val matchesSearch = searchQuery.isBlank() ||
                    voice.name.contains(searchQuery, ignoreCase = true) ||
                    voice.language.contains(searchQuery, ignoreCase = true) ||
                    voice.accent.contains(searchQuery, ignoreCase = true) ||
                    voice.description.contains(searchQuery, ignoreCase = true)

            matchesGender && matchesSearch
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = VoiceScreenDarkBg,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.performHaptic()
                                onNavigateBack()
                            },
                            modifier = Modifier.testTag("btn_voice_models_back")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Voice Models",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = VoiceRedAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "Choose the perfect voice for MJ",
                                style = MaterialTheme.typography.bodySmall,
                                color = VoiceMutedText
                            )
                        }
                    }

                    // Voice Settings Action Button
                    Surface(
                        onClick = {
                            viewModel.performHaptic()
                            onNavigateToVoiceSettings()
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF161624),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262638)),
                        modifier = Modifier.testTag("btn_to_voice_settings")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Voice Settings",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Voice Settings",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 36.dp, top = 4.dp)
        ) {
            // 0. AI Voice & Sound Provider Tabs (Gemini, GPT-4, ElevenLabs, Hindi, AI Sounds, Device)
            item {
                val geminiCount = elevenLabsVoices.count { it.languageGroup == "Gemini AI" || it.category.contains("Gemini", ignoreCase = true) }
                val gptCount = elevenLabsVoices.count { it.languageGroup == "ChatGPT" || it.category.contains("ChatGPT", ignoreCase = true) }
                val elevenCount = elevenLabsVoices.count { it.languageGroup == "English" || it.languageGroup == "Global / All" }
                val hindiCount = elevenLabsVoices.count { it.languageGroup == "Hindi" || it.isHindiOptimized }

                val tabs = listOf(
                    Triple("GEMINI_AI", "💎 Gemini AI", geminiCount),
                    Triple("GPT4", "🤖 GPT-4", gptCount),
                    Triple("ELEVEN_LABS", "⚡ ElevenLabs", elevenCount),
                    Triple("HINDI", "🇮🇳 Hindi", hindiCount),
                    Triple("SOUNDS", "🔊 AI Sounds", allAiSounds.size),
                    Triple("DEVICE", "🎙️ Device", voiceModels.size)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(tabs) { (tabKey, title, count) ->
                        val isSelected = selectedProviderTab == tabKey
                        val activeColor = when (tabKey) {
                            "GEMINI_AI" -> Color(0xFF4285F4)
                            "GPT4" -> Color(0xFF10A37F)
                            "ELEVEN_LABS" -> Color(0xFFFF9100)
                            "HINDI" -> Color(0xFFFF6D00)
                            "SOUNDS" -> Color(0xFF00E5FF)
                            else -> VoiceRedAccent
                        }

                        Surface(
                            onClick = {
                                viewModel.performHaptic()
                                selectedProviderTab = tabKey
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) activeColor.copy(alpha = 0.22f) else VoicePillInactiveBg,
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isSelected) activeColor else VoicePillInactiveBorder
                            ),
                            modifier = Modifier
                                .height(44.dp)
                                .testTag("tab_provider_${tabKey.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else VoiceMutedText
                                )
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) activeColor else Color(0xFF1E1E2C)
                                ) {
                                    Text(
                                        text = "$count",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFF8E8EA0),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // === CONDITIONAL CONTENT: AI SOUNDS TAB vs VOICE MODELS ===
            if (selectedProviderTab == "SOUNDS") {
                // 1. Active Soundpack Selector Card
                item {
                    AiSoundThemeSelectorCard(
                        activeTheme = activeSoundTheme,
                        onSelectTheme = { theme ->
                            viewModel.setActiveSoundTheme(theme)
                        }
                    )
                }

                // 2. Sound Filter Pills
                item {
                    val soundFilters = listOf(
                        "ALL" to "⭐ All (${allAiSounds.size})",
                        "GEMINI" to "💎 Gemini (3)",
                        "GPT4" to "🤖 GPT-4 (4)",
                        "ELEVEN" to "⚡ ElevenLabs (2)",
                        "CYBER" to "🔴 Cyber MJ (3)"
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(soundFilters) { (key, label) ->
                            val isSelected = selectedSoundCategoryFilter == key
                            Surface(
                                onClick = {
                                    viewModel.performHaptic()
                                    selectedSoundCategoryFilter = key
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFF00E5FF).copy(alpha = 0.2f) else VoicePillInactiveBg,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(0xFF00E5FF) else VoicePillInactiveBorder
                                ),
                                modifier = Modifier.height(38.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(horizontal = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF00E5FF) else VoiceMutedText
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Search Bar for Sounds
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_search_sound"),
                        placeholder = {
                            Text(
                                text = "Search sound effects (wake, chime, pulse, gong)...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VoiceMutedText
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = VoiceMutedText
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = VoiceMutedText
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VoiceCardDarkBg,
                            unfocusedContainerColor = VoiceCardDarkBg,
                            focusedBorderColor = Color(0xFF00E5FF),
                            unfocusedBorderColor = VoiceCardBorderDefault,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                // 4. Header for Sound Effects
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AI Audio Effects & Cues (${filteredAiSounds.size})",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E5FF)
                        )
                        Text(
                            text = "Tap to play live",
                            style = MaterialTheme.typography.labelSmall,
                            color = VoiceMutedText
                        )
                    }
                }

                // 5. Sound Cards
                items(filteredAiSounds, key = { it.id }) { sound ->
                    val isPlaying = playingSoundId == sound.id
                    AiSoundEffectCard(
                        sound = sound,
                        isPlaying = isPlaying,
                        onPlay = {
                            playingSoundId = sound.id
                            viewModel.playSpecificSound(sound.id)
                        }
                    )
                }
            } else {
                // === VOICE MODELS TAB (GEMINI, GPT-4, ELEVENLABS, HINDI, DEVICE) ===

                // 2. Gender Filter Pills
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(VoiceGender.ALL, VoiceGender.FEMALE, VoiceGender.MALE).forEach { gender ->
                            val isSelected = selectedGender == gender
                            Surface(
                                onClick = {
                                    viewModel.performHaptic()
                                    selectedGender = gender
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) VoiceRedAccent else VoicePillInactiveBg,
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, VoicePillInactiveBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .testTag("filter_gender_${gender.name.lowercase()}")
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = gender.displayName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else VoiceMutedText
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_search_voice"),
                        placeholder = {
                            val providerLabel = when (selectedProviderTab) {
                                "GEMINI_AI" -> "Google Gemini (Puck, Kore, Charon...)"
                                "GPT4" -> "OpenAI ChatGPT (Alloy, Nova, Echo...)"
                                "HINDI" -> "Hindi voice (Aditi, Aryan, Kavya...)"
                                "ELEVEN_LABS" -> "ElevenLabs voice (Rachel, Adam...)"
                                else -> "Device voice..."
                            }
                            Text(
                                text = "Search $providerLabel",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VoiceMutedText
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = VoiceMutedText
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = VoiceMutedText
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VoiceCardDarkBg,
                            unfocusedContainerColor = VoiceCardDarkBg,
                            focusedBorderColor = VoiceRedAccent,
                            unfocusedBorderColor = VoiceCardBorderDefault,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                // 4. Personality Mode Selector (when device provider is selected)
                if (selectedProviderTab == "DEVICE") {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Select Personality Mode",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(PersonalityMode.values()) { mode ->
                                    val isSelected = activePersonality == mode
                                    Surface(
                                        onClick = {
                                            viewModel.setPersonalityMode(mode)
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSelected) VoiceRedAccent else VoicePillInactiveBg,
                                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, VoicePillInactiveBorder),
                                        modifier = Modifier
                                            .height(40.dp)
                                            .testTag("personality_mode_${mode.name.lowercase()}")
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .padding(horizontal = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = mode.displayName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color.White else Color(0xFFC0C0D0)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 5. Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val count = if (selectedProviderTab == "DEVICE") filteredVoices.size else filteredElevenLabsVoices.size
                        val label = when (selectedProviderTab) {
                            "GEMINI_AI" -> "Google Gemini AI Voices ($count)"
                            "GPT4" -> "OpenAI GPT-4 Voices ($count)"
                            "HINDI" -> "Hindi Indian AI Voices ($count)"
                            "ELEVEN_LABS" -> "ElevenLabs Voices ($count)"
                            else -> "Device Models ($count)"
                        }
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = VoiceMutedText
                        )

                        if (selectedProviderTab != "DEVICE" && elevenLabsApiKey.isNotBlank()) {
                            TextButton(
                                onClick = { viewModel.refreshLiveElevenLabsVoices() },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.Sync, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Sync My API Voices", color = Color(0xFF00E5FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // 6. Voice Cards
                if (selectedProviderTab != "DEVICE") {
                    items(filteredElevenLabsVoices, key = { it.voiceId }) { voice ->
                        val isActive = voice.voiceId == selectedElevenLabsVoiceId && selectedTtsEngine == TtsEngine.ELEVEN_LABS
                        ElevenLabsVoiceCard(
                            voice = voice,
                            isActive = isActive,
                            onSelect = {
                                viewModel.selectElevenLabsVoice(voice)
                                viewModel.selectTtsEngine(TtsEngine.ELEVEN_LABS)
                            },
                            onPlayPreview = {
                                viewModel.testElevenLabsVoice(voice)
                            }
                        )
                    }
                } else {
                    items(filteredVoices, key = { it.id }) { voice ->
                        val isActive = voice.id == activeVoiceId && selectedTtsEngine != TtsEngine.ELEVEN_LABS
                        val isPlaying = voice.id == playingVoiceId

                        VoiceModelCard(
                            voice = voice,
                            isActive = isActive,
                            isPlaying = isPlaying,
                            onSelect = {
                                viewModel.selectVoiceModel(voice.id)
                                viewModel.selectTtsEngine(TtsEngine.SYSTEM_TTS)
                            },
                            onToggleFavorite = { viewModel.toggleVoiceFavorite(voice.id) },
                            onPlayPreview = { viewModel.playVoicePreview(voice) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ElevenLabsVoiceCard(
    voice: ElevenLabsVoice,
    isActive: Boolean,
    onSelect: () -> Unit,
    onPlayPreview: () -> Unit
) {
    val animatedBgColor by animateColorAsState(
        targetValue = if (isActive) VoiceCardActiveBg else VoiceCardDarkBg,
        label = "ElevenLabsCardBgAnimation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("card_elevenlabs_voice_${voice.voiceId}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = animatedBgColor),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isActive) 1.5.dp else 1.dp,
            color = if (isActive) VoiceCardBorderActive else VoiceCardBorderDefault
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Play Preview Button
            Surface(
                onClick = onPlayPreview,
                shape = CircleShape,
                color = if (isActive) VoiceRedAccent else Color(0xFF181826),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isActive) VoiceRedAccent else Color(0xFF28283C)
                ),
                modifier = Modifier
                    .size(44.dp)
                    .testTag("btn_play_elevenlabs_${voice.voiceId}")
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Play Preview",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Flag Avatar Thumbnail
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B1B2A))
                    .border(
                        1.dp,
                        if (isActive) VoiceRedAccent.copy(alpha = 0.6f) else Color(0xFF28283C),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = voice.flagEmoji,
                    fontSize = 22.sp
                )
            }

            // Info Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = voice.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (voice.isHindiOptimized) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF3E2723)
                        ) {
                            Text(
                                text = "🇮🇳 HINDI",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFB300),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }

                    if (isActive) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Active Voice",
                            tint = VoiceRedAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = "${voice.accent} • ${voice.description}",
                    style = MaterialTheme.typography.bodySmall,
                    color = VoiceMutedText,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Badges Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isFemale = voice.gender == VoiceGender.FEMALE
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isFemale) FemaleBadgeBg else MaleBadgeBg
                    ) {
                        Text(
                            text = voice.gender.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isFemale) FemaleBadgeText else MaleBadgeText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LangBadgeBg
                    ) {
                        Text(
                            text = voice.language,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = LangBadgeText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF191B33)
                    ) {
                        Text(
                            text = voice.category,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF80D8FF),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (isActive) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = VoiceRedAccent
                        ) {
                            Text(
                                text = "Active",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VoiceModelCard(
    voice: VoiceModelItem,
    isActive: Boolean,
    isPlaying: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit,
    onPlayPreview: () -> Unit
) {
    val animatedBgColor by animateColorAsState(
        targetValue = if (isActive) VoiceCardActiveBg else VoiceCardDarkBg,
        label = "CardBgAnimation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("card_voice_model_${voice.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = animatedBgColor),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isActive) 1.5.dp else 1.dp,
            color = if (isActive) VoiceCardBorderActive else VoiceCardBorderDefault
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Play / Stop Button
            Surface(
                onClick = onPlayPreview,
                shape = CircleShape,
                color = if (isPlaying) VoiceRedAccent else Color(0xFF181826),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isPlaying) VoiceRedAccent else Color(0xFF28283C)
                ),
                modifier = Modifier
                    .size(44.dp)
                    .testTag("btn_play_voice_${voice.id}")
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Stop Preview" else "Play Preview",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Avatar Thumbnail
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B1B2A))
                    .border(
                        1.dp,
                        if (isActive) VoiceRedAccent.copy(alpha = 0.6f) else Color(0xFF28283C),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_myra_avatar),
                    contentDescription = voice.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Info Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = voice.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (isActive) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Active Voice",
                            tint = VoiceRedAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = voice.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = VoiceMutedText,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Badges Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gender Tag
                    val isFemale = voice.gender == VoiceGender.FEMALE
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isFemale) FemaleBadgeBg else MaleBadgeBg
                    ) {
                        Text(
                            text = voice.gender.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isFemale) FemaleBadgeText else MaleBadgeText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Language Tag
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LangBadgeBg
                    ) {
                        Text(
                            text = voice.language,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = LangBadgeText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Active Badge
                    if (isActive) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = VoiceRedAccent
                        ) {
                            Text(
                                text = "Active",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Favorite Button
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.testTag("btn_favorite_voice_${voice.id}")
            ) {
                Icon(
                    imageVector = if (voice.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (voice.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (voice.isFavorite) Color(0xFFFF2A55) else Color(0xFF6E6E80)
                )
            }
        }
    }
}

@Composable
private fun AiSoundThemeSelectorCard(
    activeTheme: AiSoundTheme,
    onSelectTheme: (AiSoundTheme) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0D18)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_ai_sound_theme_selector")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF00E5FF).copy(alpha = 0.18f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "Active AI Sound Theme",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Sound effects for Mic Wake, Thinking & Responses",
                        style = MaterialTheme.typography.bodySmall,
                        color = VoiceMutedText
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AiSoundTheme.values().forEach { theme ->
                    val isSelected = activeTheme == theme
                    val themeColor = when (theme) {
                        AiSoundTheme.GEMINI_AI -> Color(0xFF4285F4)
                        AiSoundTheme.GPT4_VOICE -> Color(0xFF10A37F)
                        AiSoundTheme.ELEVEN_LABS -> Color(0xFFFF9100)
                        AiSoundTheme.CYBER_MJ -> Color(0xFFFF1744)
                    }

                    Surface(
                        onClick = { onSelectTheme(theme) },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) themeColor.copy(alpha = 0.18f) else Color(0xFF141424),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) themeColor else Color(0xFF242438)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("theme_option_${theme.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) themeColor else Color(0xFF1C1C30),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = when (theme) {
                                            AiSoundTheme.GEMINI_AI -> "💎"
                                            AiSoundTheme.GPT4_VOICE -> "🤖"
                                            AiSoundTheme.ELEVEN_LABS -> "⚡"
                                            AiSoundTheme.CYBER_MJ -> "🔴"
                                        },
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = theme.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFFD0D0E0)
                                    )
                                    if (isSelected) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = themeColor
                                        ) {
                                            Text(
                                                text = "ACTIVE",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = theme.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VoiceMutedText,
                                    fontSize = 11.sp
                                )
                            }

                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.PlayCircleOutline,
                                contentDescription = null,
                                tint = if (isSelected) themeColor else Color(0xFF6E6E80),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AiSoundEffectCard(
    sound: AiSoundEffectItem,
    isPlaying: Boolean,
    onPlay: () -> Unit
) {
    val themeColor = when {
        sound.category.contains("Gemini", ignoreCase = true) -> Color(0xFF4285F4)
        sound.category.contains("GPT", ignoreCase = true) || sound.category.contains("ChatGPT", ignoreCase = true) -> Color(0xFF10A37F)
        sound.category.contains("Eleven", ignoreCase = true) -> Color(0xFFFF9100)
        else -> Color(0xFFFF1744)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = VoiceCardDarkBg),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPlaying) themeColor else VoiceCardBorderDefault
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPlay)
            .testTag("card_ai_sound_${sound.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Play button with pulsating animation when active
            Surface(
                onClick = onPlay,
                shape = CircleShape,
                color = if (isPlaying) themeColor else Color(0xFF181828),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isPlaying) themeColor else Color(0xFF282840)
                ),
                modifier = Modifier
                    .size(46.dp)
                    .testTag("btn_play_sound_${sound.id}")
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
                        contentDescription = "Play sound",
                        tint = if (isPlaying) Color.White else themeColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Sound Information
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = sound.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = sound.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = VoiceMutedText,
                    fontSize = 12.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = themeColor.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, themeColor.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = sound.provider,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = themeColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF161626)
                    ) {
                        Text(
                            text = "Synthesized DSP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF8E8EA0),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Quick live play icon
            IconButton(
                onClick = onPlay,
                modifier = Modifier.testTag("btn_trigger_sound_${sound.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "Test Audio",
                    tint = if (isPlaying) themeColor else Color(0xFF6E6E80),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
