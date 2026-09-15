package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.viewmodel.MyraViewModel

val DarkScreenBg = Color(0xFF0C0C12)
val DarkCardBg = Color(0xFF141420)
val DarkBorderColor = Color(0xFF262638)
val DarkInputFieldBg = Color(0xFF10101A)
val PurpleAccent = Color(0xFFA855F7)
val PurpleButtonBg = Color(0xFF9333EA)
val CyanRadioActive = Color(0xFF06B6D4)
val PinkHeader = Color(0xFFF472B6)
val GrayTextMuted = Color(0xFFA1A1AA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiCloudSettingsScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDeepResearch: () -> Unit,
    onNavigateToConnectors: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val openRouterKeysFromVm by viewModel.openRouterApiKeys.collectAsState()
    val primaryModelFromVm by viewModel.primaryLlmModel.collectAsState()
    val groqKeysFromVm by viewModel.groqApiKeys.collectAsState()
    val geminiKeysFromVm by viewModel.geminiApiKeys.collectAsState()
    val deepSeekKeysFromVm by viewModel.deepSeekApiKeys.collectAsState()
    val openAiKeyFromVm by viewModel.openaiApiKey.collectAsState()
    val claudeKeyFromVm by viewModel.claudeApiKey.collectAsState()
    val perplexityKeyFromVm by viewModel.perplexityApiKey.collectAsState()
    val tavilyKeyFromVm by viewModel.tavilyApiKey.collectAsState()
    val elevenLabsKeyFromVm by viewModel.elevenLabsApiKey.collectAsState()
    val replicateKeyFromVm by viewModel.replicateApiKey.collectAsState()
    val huggingFaceKeyFromVm by viewModel.huggingFaceApiKey.collectAsState()
    val activeCompanyFromVm by viewModel.activeCompany.collectAsState()
    val activeVoiceProfileFromVm by viewModel.activeVoiceProfile.collectAsState()

    var openRouterKeys by remember(openRouterKeysFromVm) { mutableStateOf(openRouterKeysFromVm) }
    var selectedModel by remember(primaryModelFromVm) { mutableStateOf(primaryModelFromVm) }
    var groqKeys by remember(groqKeysFromVm) { mutableStateOf(groqKeysFromVm) }
    var geminiKeys by remember(geminiKeysFromVm) { mutableStateOf(geminiKeysFromVm) }
    var deepSeekKeys by remember(deepSeekKeysFromVm) { mutableStateOf(deepSeekKeysFromVm) }
    var openAiKey by remember(openAiKeyFromVm) { mutableStateOf(openAiKeyFromVm) }
    var claudeKey by remember(claudeKeyFromVm) { mutableStateOf(claudeKeyFromVm) }
    var perplexityKey by remember(perplexityKeyFromVm) { mutableStateOf(perplexityKeyFromVm) }
    var tavilyKey by remember(tavilyKeyFromVm) { mutableStateOf(tavilyKeyFromVm) }
    var elevenLabsKey by remember(elevenLabsKeyFromVm) { mutableStateOf(elevenLabsKeyFromVm) }
    var replicateKey by remember(replicateKeyFromVm) { mutableStateOf(replicateKeyFromVm) }
    var huggingFaceKey by remember(huggingFaceKeyFromVm) { mutableStateOf(huggingFaceKeyFromVm) }
    var selectedVoiceCompany by remember(activeCompanyFromVm) { mutableStateOf(activeCompanyFromVm) }

    val modelOptions = listOf(
        "OpenRouter (Auto-fallback)",
        "Groq (Fastest)",
        "Gemini (Best Quality)",
        "DeepSeek (Good Reasoning)"
    )

    val voiceCompanyOptions = listOf(
        "OpenAI",
        "Google Gemini",
        "Groq",
        "DeepSeek",
        "OpenRouter"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkScreenBg,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("api_cloud_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkScreenBg)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .testTag("api_cloud_settings_screen"),
            contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // Screen Header
            item {
                Column {
                    Text(
                        text = "API & Cloud Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Configure your AI models, cloud connectors, and voice media APIs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayTextMuted
                    )
                }
            }

            // ☁️ Live Cloud Connectors Banner (Google, Google Drive, GitHub, Canva)
            item {
                Surface(
                    onClick = { onNavigateToConnectors?.invoke() },
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF0F241C),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E4D3A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_cloud_connectors_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("☁️", fontSize = 20.sp)
                                Text(
                                    text = "Cloud / Connector Services",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF059669)
                            ) {
                                Text(
                                    text = "Authorize in Connectors →",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Text(
                            text = "Live Connectors: Google, Google Drive, GitHub, and Canva. In PDF, these are configured and authorized via Settings → Connectors.",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            color = Color(0xFFA7F3D0)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf("Google Workspace", "Google Drive", "GitHub", "Canva").forEach { svc ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF163E2F)
                                ) {
                                    Text(
                                        text = svc,
                                        color = Color(0xFF6EE7B7),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 1. Groq — Chat के लिए
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Groq API Keys",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF0C4A6E)) {
                            Text(
                                text = "Chat के लिए",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Ultra-fast LPU inference for real-time conversation & quick replies",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayTextMuted
                    )
                    OutlinedTextField(
                        value = groqKeys,
                        onValueChange = { groqKeys = it },
                        placeholder = { Text("gsk_... (comma-separated for multi-key rotation)", color = Color(0xFF52525B)) },
                        modifier = Modifier.fillMaxWidth().testTag("input_groq_keys"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkInputFieldBg,
                            unfocusedContainerColor = DarkInputFieldBg,
                            focusedBorderColor = PurpleAccent,
                            unfocusedBorderColor = DarkBorderColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }

            // 2. AI Backend (OpenAI, Claude, Perplexity)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "AI Backend Services",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF1E3A8A)) {
                            Text(
                                text = "AI backend",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // OpenAI
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("OpenAI API Key (GPT-4o, Audio)", color = Color(0xFF10A37F), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        OutlinedTextField(
                            value = openAiKey,
                            onValueChange = { openAiKey = it },
                            placeholder = { Text("sk-...", color = Color(0xFF52525B)) },
                            modifier = Modifier.fillMaxWidth().testTag("input_openai_key"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = Color(0xFF10A37F),
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    // Claude
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Claude API Key (Anthropic 3.5 Sonnet)", color = Color(0xFFD97706), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        OutlinedTextField(
                            value = claudeKey,
                            onValueChange = { claudeKey = it },
                            placeholder = { Text("sk-ant-...", color = Color(0xFF52525B)) },
                            modifier = Modifier.fillMaxWidth().testTag("input_claude_key"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = Color(0xFFD97706),
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    // Perplexity
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Perplexity API Key (Sonar Reasoning & Search)", color = Color(0xFF38BDF8), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        OutlinedTextField(
                            value = perplexityKey,
                            onValueChange = { perplexityKey = it },
                            placeholder = { Text("pplx-...", color = Color(0xFF52525B)) },
                            modifier = Modifier.fillMaxWidth().testTag("input_perplexity_key"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }
                }
            }

            // 3. generate_project के लिए (DeepSeek & OpenRouter)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Autonomous Project Generation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PinkHeader
                        )
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF4C1D95)) {
                            Text(
                                text = "generate_project के लिए",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Used by MYRA's autonomous coder engine to write architectures, code repositories, and tools.",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayTextMuted
                    )

                    // DeepSeek
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("DeepSeek API Keys (DeepSeek V3 / R1)", color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        OutlinedTextField(
                            value = deepSeekKeys,
                            onValueChange = { deepSeekKeys = it },
                            placeholder = { Text("sk-... (DeepSeek coding engine)", color = Color(0xFF52525B)) },
                            modifier = Modifier.fillMaxWidth().testTag("input_deepseek_keys"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = PurpleAccent,
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    // OpenRouter
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("OpenRouter API Keys (Universal model routing)", color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        OutlinedTextField(
                            value = openRouterKeys,
                            onValueChange = { openRouterKeys = it },
                            placeholder = { Text("sk-or-... (comma-separated keys)", color = Color(0xFF52525B)) },
                            modifier = Modifier.fillMaxWidth().testTag("input_openrouter_keys"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = PurpleAccent,
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = false,
                            maxLines = 3
                        )
                    }
                }
            }

            // 4. Tavily — Deep Research के लिए
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Tavily API Key",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF78350F)) {
                            Text(
                                text = "Deep Research के लिए",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Real-time web search crawler & synthesis engine for Deep Research Mode",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayTextMuted
                    )
                    OutlinedTextField(
                        value = tavilyKey,
                        onValueChange = { tavilyKey = it },
                        placeholder = { Text("tvly-...", color = Color(0xFF52525B)) },
                        modifier = Modifier.fillMaxWidth().testTag("input_tavily_key"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkInputFieldBg,
                            unfocusedContainerColor = DarkInputFieldBg,
                            focusedBorderColor = Color(0xFFF59E0B),
                            unfocusedBorderColor = DarkBorderColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    Surface(
                        onClick = onNavigateToDeepResearch,
                        shape = RoundedCornerShape(12.dp),
                        color = DarkCardBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderColor),
                        modifier = Modifier.fillMaxWidth().testTag("btn_configure_deep_research")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Open Advanced Deep Research Settings",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFF59E0B)
                            )
                            Text("→", color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 5. Voice/Media (ElevenLabs, Replicate, HuggingFace)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Voice & Media Services",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF472B6)
                        )
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF701A75)) {
                            Text(
                                text = "Voice/Media",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // ElevenLabs
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("ElevenLabs API Key (Ultra-realistic Voice Cloning)", color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        OutlinedTextField(
                            value = elevenLabsKey,
                            onValueChange = { elevenLabsKey = it },
                            placeholder = { Text("xi-api-key...", color = Color(0xFF52525B)) },
                            modifier = Modifier.fillMaxWidth().testTag("input_elevenlabs_key"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = Color(0xFFF472B6),
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    // Replicate
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Replicate API Token (Flux, SDXL, Video Gen)", color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        OutlinedTextField(
                            value = replicateKey,
                            onValueChange = { replicateKey = it },
                            placeholder = { Text("r8_...", color = Color(0xFF52525B)) },
                            modifier = Modifier.fillMaxWidth().testTag("input_replicate_key"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = Color(0xFFF472B6),
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    // HuggingFace
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Hugging Face User Access Token (Audio/TTS/Vision)", color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                        OutlinedTextField(
                            value = huggingFaceKey,
                            onValueChange = { huggingFaceKey = it },
                            placeholder = { Text("hf_...", color = Color(0xFF52525B)) },
                            modifier = Modifier.fillMaxWidth().testTag("input_huggingface_key"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = Color(0xFFF472B6),
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }
                }
            }

            // 6. Gemini API Keys
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Gemini API Keys",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Multiple keys can be comma-separated",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayTextMuted
                    )
                    OutlinedTextField(
                        value = geminiKeys,
                        onValueChange = { geminiKeys = it },
                        placeholder = {
                            Text(
                                text = "Enter Gemini API Keys",
                                color = Color(0xFF52525B),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_gemini_keys"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkInputFieldBg,
                            unfocusedContainerColor = DarkInputFieldBg,
                            focusedBorderColor = PurpleAccent,
                            unfocusedBorderColor = DarkBorderColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }

            // 7. Primary LLM Model
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Primary LLM Model",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        modelOptions.forEach { modelName ->
                            val isSelected = selectedModel == modelName
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedModel = modelName }
                                    .testTag("radio_model_${modelName.substringBefore(' ')}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedModel = modelName },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = CyanRadioActive,
                                        unselectedColor = Color(0xFF52525B)
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = modelName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // 8. Active Voice Engine & Provider Company
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Active Voice Engine & AI Provider",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                    Text(
                        text = "Current Voice: $activeVoiceProfileFromVm",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7DD3FC)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        voiceCompanyOptions.forEach { comp ->
                            val isSelected = selectedVoiceCompany == comp
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color(0xFF0C2436) else DarkCardBg)
                                    .clickable { selectedVoiceCompany = comp }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedVoiceCompany = comp },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF38BDF8),
                                        unselectedColor = Color(0xFF52525B)
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = comp,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) Color.White else GrayTextMuted,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // Save Configuration Button & Footer Note
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        viewModel.saveApiAndCloudSettings(
                            openRouterKeys = openRouterKeys,
                            primaryModel = selectedModel,
                            groqKeys = groqKeys,
                            geminiKeys = geminiKeys,
                            deepSeekKeys = deepSeekKeys,
                            openAiKey = openAiKey,
                            companyForVoice = selectedVoiceCompany,
                            claudeKey = claudeKey,
                            perplexityKey = perplexityKey,
                            tavilyKey = tavilyKey,
                            elevenLabsKey = elevenLabsKey,
                            replicateKey = replicateKey,
                            huggingFaceKey = huggingFaceKey
                        )
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("btn_save_api_configuration"),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleButtonBg),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Save Configuration",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Note: Changes will apply immediately to MJ Voice, Autonomous Coder & AI engine",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayTextMuted,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
