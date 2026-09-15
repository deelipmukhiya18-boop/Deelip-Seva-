package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyraRepository {

    private val _orbSettings = MutableStateFlow(OrbSettings())
    val orbSettings: StateFlow<OrbSettings> = _orbSettings.asStateFlow()

    private val _auraSettings = MutableStateFlow(AuraSettings())
    val auraSettings: StateFlow<AuraSettings> = _auraSettings.asStateFlow()

    private val _voiceSettings = MutableStateFlow(VoiceSettings())
    val voiceSettings: StateFlow<VoiceSettings> = _voiceSettings.asStateFlow()

    private val _activeVoiceModelId = MutableStateFlow("laomedeia")
    val activeVoiceModelId: StateFlow<String> = _activeVoiceModelId.asStateFlow()

    private val _activePersonalityMode = MutableStateFlow(PersonalityMode.NORMAL)
    val activePersonalityMode: StateFlow<PersonalityMode> = _activePersonalityMode.asStateFlow()

    private val _voiceModels = MutableStateFlow(getInitialVoiceModels())
    val voiceModels: StateFlow<List<VoiceModelItem>> = _voiceModels.asStateFlow()

    private val _connectors = MutableStateFlow(getInitialConnectors())
    val connectors: StateFlow<List<ConnectorItem>> = _connectors.asStateFlow()

    private val _triggers = MutableStateFlow(getInitialTriggers())
    val triggers: StateFlow<List<TriggerRule>> = _triggers.asStateFlow()

    private val _missions = MutableStateFlow(getInitialMissions())
    val missions: StateFlow<List<MissionItem>> = _missions.asStateFlow()

    private val _chatMessages = MutableStateFlow(getInitialChatMessages())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    val toolGuideSections: List<ToolGuideItem> = (getAllToolGuideItems() + ToolsCatalog.getExtended500PlusTools()).distinctBy { it.toolName }

    fun updateOrbSettings(settings: OrbSettings) {
        _orbSettings.value = settings
    }

    fun updateAuraSettings(settings: AuraSettings) {
        _auraSettings.value = settings
    }

    fun setActiveVoiceModel(id: String) {
        _activeVoiceModelId.value = id
    }

    fun setActivePersonalityMode(mode: PersonalityMode) {
        _activePersonalityMode.value = mode
    }

    fun toggleVoiceFavorite(id: String) {
        _voiceModels.update { list ->
            list.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    fun updateVoiceSettings(settings: VoiceSettings) {
        _voiceSettings.value = settings
    }

    fun toggleConnector(id: String, apiKey: String? = null) {
        _connectors.update { list ->
            list.map { item ->
                if (item.id == id) {
                    val nextStatus = when (item.status) {
                        ConnectorStatus.NOT_CONNECTED -> ConnectorStatus.CONNECTED
                        ConnectorStatus.CONNECTED -> ConnectorStatus.NOT_CONNECTED
                        ConnectorStatus.ERROR -> ConnectorStatus.CONNECTED
                        ConnectorStatus.NEEDS_AUTH -> ConnectorStatus.CONNECTED
                    }
                    item.copy(
                        status = nextStatus,
                        apiKey = apiKey ?: if (nextStatus == ConnectorStatus.CONNECTED) "live_key_active" else ""
                    )
                } else item
            }
        }
    }

    fun authorizeConnector(id: String, account: String, token: String = "oauth_token_active") {
        _connectors.update { list ->
            list.map { item ->
                if (item.id == id) {
                    item.copy(
                        status = ConnectorStatus.CONNECTED,
                        apiKey = token,
                        authorizedAccount = account
                    )
                } else item
            }
        }
    }

    fun revokeConnector(id: String) {
        _connectors.update { list ->
            list.map { item ->
                if (item.id == id) {
                    item.copy(
                        status = if (item.isLiveConnector) ConnectorStatus.NEEDS_AUTH else ConnectorStatus.NOT_CONNECTED,
                        apiKey = "",
                        authorizedAccount = null
                    )
                } else item
            }
        }
    }

    fun setConnectorStatus(id: String, status: ConnectorStatus, apiKey: String = "") {
        _connectors.update { list ->
            list.map { item ->
                if (item.id == id) {
                    item.copy(status = status, apiKey = apiKey)
                } else item
            }
        }
    }

    fun toggleTrigger(id: String) {
        _triggers.update { list ->
            list.map { if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it }
        }
    }

    fun addTrigger(trigger: TriggerRule) {
        _triggers.update { it + trigger }
    }

    fun deleteTrigger(id: String) {
        _triggers.update { it.filterNot { item -> item.id == id } }
    }

    fun addChatMessage(message: ChatMessage) {
        _chatMessages.update { it + message }
    }

    fun clearChat() {
        _chatMessages.value = emptyList()
    }

    companion object {
        fun getInitialVoiceModels(): List<VoiceModelItem> = listOf(
            // 🇮🇳 Hindi Voices (Indian AI Models)
            VoiceModelItem(
                id = "hindi_aditi",
                name = "Aditi (Hindi)",
                description = "प्राकृतिक और मधुर भारतीय हिंदी आवाज़",
                gender = VoiceGender.FEMALE,
                language = "Hindi",
                previewSampleText = "नमस्ते! मैं अदिति हूँ। आपकी हर बात को सरलता से समझने और सहायता करने के लिए तैयार।"
            ),
            VoiceModelItem(
                id = "hindi_aarav",
                name = "Aarav (Hindi)",
                description = "गंभीर, आत्मविश्वासी और आधुनिक पुरुष आवाज़",
                gender = VoiceGender.MALE,
                language = "Hindi",
                previewSampleText = "नमस्ते! मैं आरव हूँ। सटीक, स्पष्ट और त्वरित जानकारी के लिए हमेशा तैयार।"
            ),
            VoiceModelItem(
                id = "hindi_kavya",
                name = "Kavya (Hindi)",
                description = "युवा, ऊर्जावान और दोस्ताना महिला आवाज़",
                gender = VoiceGender.FEMALE,
                language = "Hindi",
                previewSampleText = "हेलो! मैं काव्या हूँ। आज आपका दिन कैसे और बेहतर बना सकती हूँ?"
            ),
            VoiceModelItem(
                id = "hindi_rohan",
                name = "Rohan (Hindi)",
                description = "शांत, तकनीकी और भरोसेमंद पुरुष आवाज़",
                gender = VoiceGender.MALE,
                language = "Hindi",
                previewSampleText = "नमस्ते। मैं रोहन हूँ। सभी ऑपरेशन्स और टास्क के लिए तत्पर।"
            ),
            VoiceModelItem(
                id = "hindi_ananya",
                name = "Ananya (Hindi)",
                description = "मधुर और संवेदनशील भारतीय साथी आवाज़",
                gender = VoiceGender.FEMALE,
                language = "Hindi",
                previewSampleText = "नमस्ते! मैं अनन्या हूँ। MYRA के साथ आपकी स्मार्ट और मददगार साथी।"
            ),
            VoiceModelItem(
                id = "hindi_vikram",
                name = "Vikram (Hindi)",
                description = "गहरा बैरिटोन और प्रभावी नेतृत्व आवाज़",
                gender = VoiceGender.MALE,
                language = "Hindi",
                previewSampleText = "नमस्ते। मैं विक्रम हूँ। हर मुश्किल टास्क में आपका पूर्ण सहयोग करूँगा।"
            ),

            // 🤖 ChatGPT (OpenAI) Voices
            VoiceModelItem(
                id = "chatgpt_alloy",
                name = "Alloy (ChatGPT)",
                description = "Balanced, crisp and neutral AI assistant tone",
                gender = VoiceGender.FEMALE,
                language = "Hindi / English",
                previewSampleText = "Hello! I am Alloy from ChatGPT. Ready to help you with anything."
            ),
            VoiceModelItem(
                id = "chatgpt_echo",
                name = "Echo (ChatGPT)",
                description = "Deep baritone, authoritative command tone",
                gender = VoiceGender.MALE,
                language = "Hindi / English",
                previewSampleText = "Echo voice online. Authoritative, steady, and clear command execution."
            ),
            VoiceModelItem(
                id = "chatgpt_fable",
                name = "Fable (ChatGPT)",
                description = "Expressive British storytelling voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Greetings! I am Fable. Bringing conversations and ideas vividly to life."
            ),
            VoiceModelItem(
                id = "chatgpt_onyx",
                name = "Onyx (ChatGPT)",
                description = "Deep resonant baritone executive tone",
                gender = VoiceGender.MALE,
                language = "Hindi / English",
                previewSampleText = "Namaste. Main Onyx hoon. Calm, commanding, and reliable."
            ),
            VoiceModelItem(
                id = "chatgpt_nova",
                name = "Nova (ChatGPT)",
                description = "Warm, empathetic and energetic female voice",
                gender = VoiceGender.FEMALE,
                language = "Hindi / English",
                previewSampleText = "Hi there! I am Nova from ChatGPT. Always here with a warm smile to help!"
            ),
            VoiceModelItem(
                id = "chatgpt_shimmer",
                name = "Shimmer (ChatGPT)",
                description = "Clear melodic high-treble studio voice",
                gender = VoiceGender.FEMALE,
                language = "Hindi / English",
                previewSampleText = "Hello! Shimmer here from ChatGPT. Crisp, cheerful, and bright."
            ),
            VoiceModelItem(
                id = "chatgpt_breeze",
                name = "Breeze (ChatGPT)",
                description = "Animated, casual, and friendly conversational tone",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Hey friend! Breeze voice ready. What are we getting done today?"
            ),
            VoiceModelItem(
                id = "chatgpt_cove",
                name = "Cove (ChatGPT)",
                description = "Calm, contemplative, relaxed male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Cove active. Relaxed, thoughtful, and steady assistance."
            ),
            VoiceModelItem(
                id = "chatgpt_ember",
                name = "Ember (ChatGPT)",
                description = "Confident, rich, and engaging female tone",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Hello! Ember from ChatGPT. Confident, capable, and ready."
            ),
            VoiceModelItem(
                id = "chatgpt_juniper",
                name = "Juniper (ChatGPT)",
                description = "Spirited, candid, and cheerful female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Hey there! Juniper here from ChatGPT. Let's make today great!"
            ),
            VoiceModelItem(
                id = "chatgpt_sol",
                name = "Sol (ChatGPT)",
                description = "Smooth, sunny, and relaxed warmth",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Greetings. Sol voice active. Smooth and sunny conversations."
            ),

            // ✨ Google Gemini AI Core Voices
            VoiceModelItem(
                id = "achernar",
                name = "Achernar",
                description = "Smooth, professional female",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Achernar voice active. Clear, smooth, and professional."
            ),
            VoiceModelItem(
                id = "achird",
                name = "Achird",
                description = "Warm, expressive male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Achird voice active. Warm, expressive, and ready to assist."
            ),
            VoiceModelItem(
                id = "algenib",
                name = "Algenib",
                description = "Bright, youthful male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Algenib voice active. Bright, youthful, and energetic."
            ),
            VoiceModelItem(
                id = "algieba",
                name = "Algieba",
                description = "Deep, resonant male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Algieba voice active. Deep, resonant, and calm."
            ),
            VoiceModelItem(
                id = "alnilam",
                name = "Alnilam",
                description = "Clear, neutral male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Alnilam voice active. Clear, balanced, and neutral."
            ),
            VoiceModelItem(
                id = "aoede",
                name = "Aoede",
                description = "Soft, lyrical female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Aoede voice active. Soft, lyrical, and melodic."
            ),
            VoiceModelItem(
                id = "autonoe",
                name = "Autonoe",
                description = "Mature, authoritative female",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Autonoe voice active. Mature, authoritative, and direct."
            ),
            VoiceModelItem(
                id = "callirrhoe",
                name = "Callirrhoe",
                description = "Friendly, conversational female",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Callirrhoe voice active. Friendly, warm, and conversational."
            ),
            VoiceModelItem(
                id = "charon",
                name = "Charon",
                description = "Steady, reliable male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Charon voice active. Steady, reliable, and grounded."
            ),
            VoiceModelItem(
                id = "despina",
                name = "Despina",
                description = "Lively, energetic female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Despina voice active. Lively, enthusiastic, and ready."
            ),
            VoiceModelItem(
                id = "enceladus",
                name = "Enceladus",
                description = "Calm, soothing male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Enceladus voice active. Calm, peaceful, and soothing."
            ),
            VoiceModelItem(
                id = "erinome",
                name = "Erinome",
                description = "Gentle, expressive female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Erinome voice active. Gentle, expressive, and attentive."
            ),
            VoiceModelItem(
                id = "fenrir",
                name = "Fenrir",
                description = "Strong, commanding male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Fenrir voice active. Strong, commanding, and resolute."
            ),
            VoiceModelItem(
                id = "gacrux",
                name = "Gacrux",
                description = "Crisp, articulate female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Gacrux voice active. Crisp, articulate, and precise."
            ),
            VoiceModelItem(
                id = "iapetus",
                name = "Iapetus",
                description = "Broad, theatrical male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Iapetus voice active. Broad, theatrical, and expressive."
            ),
            VoiceModelItem(
                id = "kore",
                name = "Kore",
                description = "Sweet, innocent female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Kore voice active. Sweet, innocent, and polite."
            ),
            VoiceModelItem(
                id = "laomedeia",
                name = "Laomedeia",
                description = "Gentle, nurturing female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Hello! I am MJ, your personal assistant, speaking with Laomedeia voice."
            ),
            VoiceModelItem(
                id = "leda",
                name = "Leda",
                description = "Pleasant, upbeat female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Leda voice active. Pleasant, upbeat, and cheerful."
            ),
            VoiceModelItem(
                id = "orus",
                name = "Orus",
                description = "Friendly, relatable male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Orus voice active. Friendly, natural, and relatable."
            ),
            VoiceModelItem(
                id = "puck",
                name = "Puck",
                description = "Playful, mischievous male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Puck voice active. Playful, quick, and witty."
            ),
            VoiceModelItem(
                id = "pulcherrima",
                name = "Pulcherrima",
                description = "Radiant, charming female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Pulcherrima voice active. Radiant, charming, and bright."
            ),
            VoiceModelItem(
                id = "rasalgethi",
                name = "Rasalgethi",
                description = "Wise, experienced male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Rasalgethi voice active. Wise, experienced, and composed."
            ),
            VoiceModelItem(
                id = "sadachbia",
                name = "Sadachbia",
                description = "Soft-spoken, shy male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Sadachbia voice active. Soft-spoken, quiet, and subtle."
            ),
            VoiceModelItem(
                id = "sadaltager",
                name = "Sadaltager",
                description = "Robust, hearty male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Sadaltager voice active. Robust, hearty, and dependable."
            ),
            VoiceModelItem(
                id = "schedar",
                name = "Schedar",
                description = "Solid, grounded male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Schedar voice active. Solid, grounded, and clear."
            ),
            VoiceModelItem(
                id = "sulafat",
                name = "Sulafat",
                description = "Clear, ringing female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Sulafat voice active. Clear, resonant, and ringing."
            ),
            VoiceModelItem(
                id = "umbriel",
                name = "Umbriel",
                description = "Subdued, serious male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Umbriel voice active. Subdued, serious, and steady."
            ),
            VoiceModelItem(
                id = "vindemiatrix",
                name = "Vindemiatrix",
                description = "Intelligent, focused female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Vindemiatrix voice active. Intelligent, analytical, and focused."
            ),
            VoiceModelItem(
                id = "zephyr",
                name = "Zephyr",
                description = "Light, breezy female voice",
                gender = VoiceGender.FEMALE,
                language = "English",
                previewSampleText = "Zephyr voice active. Light, breezy, and refreshing."
            ),
            VoiceModelItem(
                id = "zubenelgenubi",
                name = "Zubenelgenubi",
                description = "Deep, velvety male voice",
                gender = VoiceGender.MALE,
                language = "English",
                previewSampleText = "Zubenelgenubi voice active. Deep, velvety, and smooth."
            )
        )

        fun getInitialConnectors(): List<ConnectorItem> = listOf(
            // ☁️ Cloud / Live Connectors (4) - Google, Google Drive, GitHub, Canva
            ConnectorItem(
                id = "google",
                name = "Google",
                description = "Read upcoming Google Calendar, Gmail, contacts & Workspace profile.",
                category = ConnectorCategory.CLOUD_SERVICES,
                status = ConnectorStatus.NEEDS_AUTH,
                badge = "Live Cloud",
                roleBadge = "Cloud Service",
                isLiveConnector = true,
                permissionsGranted = listOf("Google Calendar read/write", "Gmail notification alerts", "Workspace Identity", "Contacts profile"),
                iconLetter = "G"
            ),
            ConnectorItem(
                id = "googledrive",
                name = "Google Drive",
                description = "Access Drive files, documents, sheets & cloud backups seamlessly.",
                category = ConnectorCategory.CLOUD_SERVICES,
                status = ConnectorStatus.NEEDS_AUTH,
                badge = "Live Cloud",
                roleBadge = "Cloud Service",
                isLiveConnector = true,
                permissionsGranted = listOf("Drive files list & search", "Document reading", "Export project backups"),
                iconLetter = "D"
            ),
            ConnectorItem(
                id = "github",
                name = "GitHub",
                description = "Sync repositories, code commits, branch tracking & pull requests.",
                category = ConnectorCategory.CLOUD_SERVICES,
                status = ConnectorStatus.NEEDS_AUTH,
                badge = "Live Cloud",
                roleBadge = "Cloud Service",
                isLiveConnector = true,
                permissionsGranted = listOf("Repository read/write", "Commits & branches sync", "Issue tracker", "Gists"),
                iconLetter = "G"
            ),
            ConnectorItem(
                id = "canva",
                name = "Canva",
                description = "Access Canva designs, visual marketing templates & graphic assets.",
                category = ConnectorCategory.CLOUD_SERVICES,
                status = ConnectorStatus.NEEDS_AUTH,
                badge = "Live Cloud",
                roleBadge = "Cloud Service",
                isLiveConnector = true,
                permissionsGranted = listOf("Canva designs access", "Export PNG/PDF media assets", "Template library"),
                iconLetter = "C"
            ),

            // AI & Models - Dedicated PDF Engine Roles
            ConnectorItem(
                id = "groq",
                name = "Groq",
                description = "Ultra-fast LPU inference (llama-3.3-70b-versatile) for real-time talk.",
                category = ConnectorCategory.AI_MODELS,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Chat",
                roleBadge = "Chat के लिए",
                iconLetter = "G"
            ),
            ConnectorItem(
                id = "openai",
                name = "OpenAI",
                description = "GPT-4o reasoning, cognitive processing & ChatGPT Studio voice backend.",
                category = ConnectorCategory.AI_MODELS,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Backend",
                roleBadge = "AI backend",
                iconLetter = "O"
            ),
            ConnectorItem(
                id = "claude",
                name = "Claude",
                description = "Anthropic Claude 3.5 Sonnet long-context reasoning & analytical backend.",
                category = ConnectorCategory.AI_MODELS,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Backend",
                roleBadge = "AI backend",
                iconLetter = "C"
            ),
            ConnectorItem(
                id = "perplexity",
                name = "Perplexity",
                description = "Web-grounded real-time answers & live citation search backend.",
                category = ConnectorCategory.AI_MODELS,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Backend",
                roleBadge = "AI backend",
                iconLetter = "P"
            ),
            ConnectorItem(
                id = "tavily",
                name = "Tavily",
                description = "Deep autonomous web research, fact-checking & multi-source web search.",
                category = ConnectorCategory.AI_MODELS,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Research",
                roleBadge = "Deep Research के लिए",
                iconLetter = "T"
            ),
            ConnectorItem(
                id = "deepseek",
                name = "DeepSeek",
                description = "DeepSeek Coder / V3 / R1 for autonomous project & code generation.",
                category = ConnectorCategory.AI_MODELS,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Coding",
                roleBadge = "generate_project के लिए",
                iconLetter = "D"
            ),
            ConnectorItem(
                id = "openrouter",
                name = "OpenRouter",
                description = "Multi-model orchestration and resilient fallback for generate_project.",
                category = ConnectorCategory.AI_MODELS,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Coding",
                roleBadge = "generate_project के लिए",
                iconLetter = "O"
            ),
            ConnectorItem(
                id = "gemini",
                name = "Google Gemini",
                description = "Native Gemini 2.5 Flash multimodal assistant engine.",
                category = ConnectorCategory.AI_MODELS,
                status = ConnectorStatus.CONNECTED,
                badge = "Backend",
                roleBadge = "AI backend",
                iconLetter = "G"
            ),

            // Voice & Media (ElevenLabs, Replicate, HuggingFace)
            ConnectorItem(
                id = "elevenlabs",
                name = "ElevenLabs",
                description = "Ultra-realistic neural voice synthesis, emotional prosody & voice cloning.",
                category = ConnectorCategory.VOICE_MEDIA,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Voice",
                roleBadge = "Voice/Media",
                iconLetter = "E"
            ),
            ConnectorItem(
                id = "replicate",
                name = "Replicate",
                description = "Flux.1 Schnell, Stable Diffusion XL & visual media cloud pipelines.",
                category = ConnectorCategory.VOICE_MEDIA,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Media",
                roleBadge = "Voice/Media",
                iconLetter = "R"
            ),
            ConnectorItem(
                id = "huggingface",
                name = "Hugging Face",
                description = "Open AI multimodal models, Whisper audio models & diffusion pipelines.",
                category = ConnectorCategory.VOICE_MEDIA,
                status = ConnectorStatus.NOT_CONNECTED,
                badge = "Media",
                roleBadge = "Voice/Media",
                iconLetter = "H"
            ),

            // Productivity (3)
            ConnectorItem(
                id = "notion",
                name = "Notion",
                description = "Sync personal notes, roadmaps, task boards & documentation.",
                category = ConnectorCategory.PRODUCTIVITY,
                status = ConnectorStatus.NOT_CONNECTED,
                iconLetter = "N"
            ),
            ConnectorItem(
                id = "slack",
                name = "Slack",
                description = "Team workspace channels, direct messages & presence.",
                category = ConnectorCategory.PRODUCTIVITY,
                status = ConnectorStatus.NOT_CONNECTED,
                iconLetter = "S"
            ),
            ConnectorItem(
                id = "microsoft",
                name = "Microsoft",
                description = "Outlook 365, OneDrive cloud storage & Office documents.",
                category = ConnectorCategory.PRODUCTIVITY,
                status = ConnectorStatus.NOT_CONNECTED,
                iconLetter = "M"
            ),

            // Media (2)
            ConnectorItem(
                id = "youtube",
                name = "YouTube",
                description = "Search videos, manage watch history & music playback.",
                category = ConnectorCategory.MEDIA,
                status = ConnectorStatus.NOT_CONNECTED,
                iconLetter = "Y"
            ),
            ConnectorItem(
                id = "spotify",
                name = "Spotify",
                description = "Stream high-definition music playback & playlist queue control.",
                category = ConnectorCategory.MEDIA,
                status = ConnectorStatus.NOT_CONNECTED,
                iconLetter = "S"
            ),

            // Automation (2)
            ConnectorItem(
                id = "telegram",
                name = "Telegram",
                description = "Send & read messages via Telegram Bot API bridge.",
                category = ConnectorCategory.AUTOMATION,
                status = ConnectorStatus.NOT_CONNECTED,
                iconLetter = "T"
            ),
            ConnectorItem(
                id = "homeassistant",
                name = "Home Assistant",
                description = "Smart home control. Format:",
                category = ConnectorCategory.AUTOMATION,
                status = ConnectorStatus.NOT_CONNECTED,
                iconLetter = "H"
            )
        )

        fun getInitialTriggers(): List<TriggerRule> = listOf(
            TriggerRule("t1", "Morning Briefing", "Scheduled Time", "Har subah 8:00 AM", "Aaj ka schedule aur weather padh kar sunao", true),
            TriggerRule("t2", "Driving Auto-Launch", "Screen State", "Car Bluetooth connect hone pe", "Driving mode on karo aur Maps kholo", true),
            TriggerRule("t3", "Low Battery Shield", "Battery Level", "Battery < 15% drop hone pe", "Ultra Power Save activate karo aur alert do", true),
            TriggerRule("t4", "WhatsApp Priority Alert", "Notification se", "Boss / Family se message aane pe", "Immediate voice notification announcement", false),
            TriggerRule("t5", "Night Sleep Routine", "Scheduled Time", "Raat 11:30 PM pe", "DND on, volume 20%, phone lock", true)
        )

        fun getInitialMissions(): List<MissionItem> = listOf(
            MissionItem(
                id = "m1",
                title = "Autonomous Trip Planning: Delhi to Manali",
                status = "Active",
                progress = 0.65f,
                currentStep = "Comparing hotel reviews & checking mountain weather",
                totalSteps = 6,
                logs = listOf(
                    "Step 1: Analyzed travel dates & budget constraints",
                    "Step 2: Scanned routes via Google Maps & traffic heuristics",
                    "Step 3: Finding top 3 certified homestays with power backup",
                    "Step 4: Preparing checklist for cold weather gear"
                )
            ),
            MissionItem(
                id = "m2",
                title = "Optimize Device Storage & Purge Cache",
                status = "Completed",
                progress = 1.0f,
                currentStep = "Cleaned 1.4 GB temp files safely",
                totalSteps = 4,
                logs = listOf(
                    "Scanned 14 application cache nodes",
                    "Identified obsolete WhatsApp media backups",
                    "Cleared thumbnail cache cleanly"
                )
            )
        )

        fun getInitialChatMessages(): List<ChatMessage> = listOf(
            ChatMessage(
                id = "c1",
                text = "Hello! Main MJ hoon — aapki personal AI companion aur smart assistant. Aap bol kar ya type karke koi bhi command de sakte hain. How can I help you today?",
                isUser = false
            )
        )

        fun getAllToolGuideItems(): List<ToolGuideItem> = listOf(
            // 1. Communication Tools
            ToolGuideItem("1_1", 1, "Communication Tools", "send_whatsapp", "Contact ka WhatsApp chat kholta hai, message likhta hai, Send dabata hai aur confirm karta hai.", "\"Rahul ko WhatsApp pe bolo main late hoon\"", actionType = "WHATSAPP"),
            ToolGuideItem("1_2", 1, "Communication Tools", "send_sms", "Kisi contact ya number ko seedha SMS bhejta hai.", "\"Mummy ko SMS karo ki pahuch gaya\"", actionType = "SMS"),
            ToolGuideItem("1_3", 1, "Communication Tools", "open_email_inbox", "Aapka signed-in Gmail/Outlook inbox kholta hai.", "\"Mera inbox kholo\"", actionType = "EMAIL"),
            ToolGuideItem("1_4", 1, "Communication Tools", "compose_email", "Email likhne ka screen kholta hai, To/Subject/Body bhar deta hai.", "\"Boss ko email likho meeting reschedule ho gayi\"", actionType = "EMAIL"),
            ToolGuideItem("1_5", 1, "Communication Tools", "send_emergency_alert", "Aapke trusted contact ko location ke saath SOS alert bhejta hai — sirf emergency ke liye.", "\"SOS\", \"Main khatre mein hoon\", \"Madad chahiye\"", safetyGuard = "EMERGENCY ONLY", actionType = "SOS"),

            // 2. Call Tools
            ToolGuideItem("2_1", 2, "Call Tools", "call_contact", "Saved contact ya number pe call lagata hai.", "\"Papa ko call karo\"", actionType = "CALL"),
            ToolGuideItem("2_2", 2, "Call Tools", "lookup_contact", "Bina call/message kiye kisi contact ka number bata deta hai.", "\"Rahul ka number kya hai\"", actionType = "CONTACT"),
            ToolGuideItem("2_3", 2, "Call Tools", "answer_call", "Aati hui call utha leta hai.", "\"Utha lo\", \"Answer karo\", \"Haan\"", actionType = "CALL"),
            ToolGuideItem("2_4", 2, "Call Tools", "end_call", "Call kaatta/reject karta hai. Ringing call sirf tabhi kaatega jab aapne clearly bola ho.", "\"Call kaat do\", \"Reject karo\"", safetyGuard = "SAFETY GUARD", actionType = "CALL"),

            // 3. Media Tools
            ToolGuideItem("3_1", 3, "Media Tools", "play_music", "Song/album/artist search karke play karta hai.", "\"Arijit Singh ka gaana chalao\"", actionType = "MEDIA"),
            ToolGuideItem("3_2", 3, "Media Tools", "media_control", "Jo bhi chal raha hai usko play/pause/next/previous karta hai.", "\"Agla gaana\", \"Pause karo\"", actionType = "MEDIA"),
            ToolGuideItem("3_3", 3, "Media Tools", "set_volume", "Media volume ko % mein set karta hai.", "\"Volume 50% kar do\"", actionType = "DEVICE"),

            // 4. Device Tools
            ToolGuideItem("4_1", 4, "Device Tools", "set_alarm", "Alarm laga deta hai (24-hr clock), label ke saath optional.", "\"Subah 6 baje alarm laga do\"", actionType = "ALARM"),
            ToolGuideItem("4_2", 4, "Device Tools", "set_timer", "Countdown timer start karta hai.", "\"10 minute ka timer laga do\"", actionType = "TIMER"),
            ToolGuideItem("4_3", 4, "Device Tools", "toggle_flashlight", "Torch on/off karta hai.", "\"Torch on karo\"", actionType = "FLASHLIGHT"),
            ToolGuideItem("4_4", 4, "Device Tools", "get_battery", "Battery level, charging status, power-save status batata hai.", "\"Battery kitni hai\"", actionType = "BATTERY"),
            ToolGuideItem("4_5", 4, "Device Tools", "open_url", "Diya gaya web address browser mein kholta hai.", "\"xyz.com kholo\"", actionType = "BROWSER"),
            ToolGuideItem("4_6", 4, "Device Tools", "set_clipboard", "Text ko clipboard mein copy karta hai.", "\"Ye copy kar lo\"", actionType = "CLIPBOARD"),
            ToolGuideItem("4_7", 4, "Device Tools", "lock_device", "Turant screen lock kar deta hai — urgent safety command, bina poochhe execute hota hai.", "\"Lock kar do\", \"Phone band kar do\"", safetyGuard = "NO CONFIRM (URGENT)", actionType = "LOCK"),
            ToolGuideItem("4_8", 4, "Device Tools", "analyze_storage", "Cache/temp files check karke cleanup breakdown batata hai.", "\"Storage kitni bhari hai\"", actionType = "STORAGE"),
            ToolGuideItem("4_9", 4, "Device Tools", "clean_storage", "App cache/temp files clear karta hai (aapki confirmation ke baad).", "\"Storage clean kar do\"", actionType = "STORAGE"),

            // 5. Files & Photos Tools
            ToolGuideItem("5_1", 5, "Files & Photos", "list_files", "Agent workspace ki files list karta hai.", "\"Files dikhao\"", actionType = "FILES"),
            ToolGuideItem("5_2", 5, "Files & Photos", "search_files", "Naam/type/date se files dhundta hai.", "\"Wo PDF dhundo jo kal banayi thi\"", actionType = "FILES"),
            ToolGuideItem("5_3", 5, "Files & Photos", "delete_file", "Ek named file delete karta hai.", "\"Ye file delete kar do\"", actionType = "FILES"),
            ToolGuideItem("5_4", 5, "Files & Photos", "share_file", "Agent-workspace file share sheet se ya seedha named app pe bhejta hai.", "\"Ye report Rahul ko WhatsApp pe bhej do\"", actionType = "FILES"),
            ToolGuideItem("5_5", 5, "Files & Photos", "delete_photo", "Ek ya zyada photo delete karta hai (default: sabse recent photo).", "\"Last 3 photo delete kar do\"", actionType = "FILES"),
            ToolGuideItem("5_6", 5, "Files & Photos", "file_operation", "Zip / unzip / copy / move files.", "\"Is folder ko zip kar do\"", actionType = "FILES"),
            ToolGuideItem("5_7", 5, "Files & Photos", "open_file", "File ko sahi system app se kholta hai.", "\"Ye file kholo\"", actionType = "FILES"),
            ToolGuideItem("5_8", 5, "Files & Photos", "format_code", "Code file ki basic syntax auto-fix/format karta hai.", "\"Ye code format kar do\"", actionType = "FILES"),
            ToolGuideItem("5_9", 5, "Files & Photos", "manage_file", "Device storage mein kahin bhi file ko rename/copy/move/delete karta hai.", "\"Is file ka naam change kar do\"", actionType = "FILES"),
            ToolGuideItem("5_10", 5, "Files & Photos", "manage_folder", "Device storage mein folder create/delete/rename/list/open karta hai.", "\"Naya folder banao 'Office'\"", actionType = "FILES"),
            ToolGuideItem("5_11", 5, "Files & Photos", "take_photo", "Bina camera UI dikhaye photo kheenchta hai aur gallery mein save karta hai.", "\"Ek photo le lo\"", actionType = "CAMERA"),
            ToolGuideItem("5_12", 5, "Files & Photos", "camera_vision", "Camera viewfinder khol kar real duniya dekhta hai — barcode scan, doc padhna.", "\"Ye barcode scan karo\"", actionType = "VISION"),

            // 6. Smart / Generative Tools
            ToolGuideItem("6_1", 6, "Smart / Generative Tools", "generate_project", "Description se poora website/coding project bana kar browser mein kholta hai.", "\"Ek portfolio website bana do\"", actionType = "GEN_PROJECT"),
            ToolGuideItem("6_2", 6, "Smart / Generative Tools", "generate_image", "DALL·E aur Imagen se ultra HD image banata hai.", "\"Ek sunset ki image banao\"", actionType = "GEN_IMAGE"),
            ToolGuideItem("6_3", 6, "Smart / Generative Tools", "deep_research", "Web research karke summary aur full insight report deta hai.", "\"Electric cars pe research karo\"", actionType = "RESEARCH"),
            ToolGuideItem("6_4", 6, "Smart / Generative Tools", "game_coach", "Real-time gaming coach on/off karta hai — game khelte waqt tactical advice deta hai.", "\"Game coach on kar do\"", actionType = "GAME_COACH"),
            ToolGuideItem("6_5", 6, "Smart / Generative Tools", "system_health", "Phone slow/hot/battery drain kyun hai — RAM, thermal, battery diagnose karta hai.", "\"Phone slow kyun hai\"", actionType = "SYSTEM_HEALTH"),
            ToolGuideItem("6_6", 6, "Smart / Generative Tools", "open_app_settings", "Kisi app ki system settings kholta hai (force-stop/permission change ke liye).", "used automatically after system_health", actionType = "SETTINGS"),
            ToolGuideItem("6_7", 6, "Smart / Generative Tools", "read_captured", "Aapne doosri app se share/copy kiya hua text wapas padhta aur summarize karta hai.", "\"Jo maine abhi capture kiya usko summarise karo\"", actionType = "CLIPBOARD"),

            // 7. Missions (Bade Multi-Step Goals)
            ToolGuideItem("7_1", 7, "Missions", "start_mission", "Complex, multi-step goal shuru karta hai jisme planning + autonomous execution chahiye.", "\"Plan my trip to Delhi\", \"Meri subah ki routine automate kar do\"", actionType = "MISSION"),
            ToolGuideItem("7_2", 7, "Missions", "pause_resume_cancel", "Chal rahe mission ko rokna/wapas shuru karna/cancel karna.", "\"Mission pause karo\"", actionType = "MISSION"),

            // 8. Notification Tools
            ToolGuideItem("8_1", 8, "Notification Tools", "read_notifications", "Recent important notifications padh kar sunata hai.", "\"Notifications padho\"", actionType = "NOTIF"),
            ToolGuideItem("8_2", 8, "Notification Tools", "reply_to_notification", "Kisi notification ka seedha reply bhej deta hai bina app khole.", "\"Rahul ko reply karo 'okay'\"", actionType = "NOTIF"),
            ToolGuideItem("8_3", 8, "Notification Tools", "read_missed_calls", "Recent missed calls padh kar sunata hai.", "\"Missed calls batao\"", actionType = "CALL"),
            ToolGuideItem("8_4", 8, "Notification Tools", "read_otp", "Latest OTP dhund kar padhta hai — kabhi khud se nahi bolta, sirf poochne pe.", "\"OTP batao\"", safetyGuard = "EXPLICIT ONLY", actionType = "NOTIF"),
            ToolGuideItem("8_5", 8, "Notification Tools", "clear_notifications", "Status bar ke saare active notifications clear kar deta hai.", "\"Notifications clear kar do\"", actionType = "NOTIF"),

            // 9. Maps & "My World"
            ToolGuideItem("9_1", 9, "Maps & My World", "open_map", "MJ ka Personal AI Map UI kholta hai.", "\"Map kholo\"", actionType = "MAPS"),
            ToolGuideItem("9_2", 9, "Maps & My World", "navigate_to", "Google Maps mein turn-by-turn navigation shuru karta hai.", "\"Airport ka rasta dikhao\"", actionType = "MAPS"),
            ToolGuideItem("9_3", 9, "Maps & My World", "navigate_to_place", "Saved favorite place (Home/Office) tak navigation.", "\"Ghar chalo\"", actionType = "MAPS"),
            ToolGuideItem("9_4", 9, "Maps & My World", "get_location", "Current address + coordinates batata hai.", "\"Main kahan hoon\"", actionType = "MAPS"),
            ToolGuideItem("9_5", 9, "Maps & My World", "get_distance", "Kisi saved favorite place ki distance batata hai.", "\"Office kitni door hai\"", actionType = "MAPS"),
            ToolGuideItem("9_6", 9, "Maps & My World", "get_parking_location", "Aapne gaadi kahan park ki thi, wo bataata hai.", "\"Gaadi kahan park ki thi\"", actionType = "MAPS"),
            ToolGuideItem("9_7", 9, "Maps & My World", "save_parking", "Current location ko parking spot ke roop mein save karta hai.", "\"Yahan park kiya hai, yaad rakho\"", actionType = "MAPS"),
            ToolGuideItem("9_8", 9, "Maps & My World", "search_nearby", "Aas-paas ATM, hospital, petrol pump waghera dhundta hai.", "\"Nearby ATM dikhao\"", actionType = "MAPS"),
            ToolGuideItem("9_9", 9, "Maps & My World", "set_smart_mode", "Driving, Sleep, Work, ya Game mode on/off karta hai.", "\"Driving mode on kar do\"", actionType = "DEVICE"),

            // 10. PC Connect Tools
            ToolGuideItem("10_1", 10, "PC Connect", "pc_connect", "MJ Companion app chal rahe PC se IP + PIN daal kar connect karta hai.", "Settings → PC Connect mein IP aur PIN daalo", actionType = "PC"),
            ToolGuideItem("10_2", 10, "PC Connect", "pc_command", "Connected PC ko control command bhejta hai.", "\"PC pe ye kar do...\"", actionType = "PC"),
            ToolGuideItem("10_3", 10, "PC Connect", "send_file_to_pc", "Phone se PC pe file bhejta hai.", "\"Ye file PC pe bhej do\"", actionType = "PC"),

            // 11. Search & Browser
            ToolGuideItem("11_1", 11, "Search & Browser", "search_google", "Web search karke results dikhata hai.", "\"Google pe search karo aaj ka mausam\"", actionType = "BROWSER"),
            ToolGuideItem("11_2", 11, "Search & Browser", "open_browser", "Default browser kisi URL ya homepage pe kholta hai.", "\"Browser kholo\"", actionType = "BROWSER"),
            ToolGuideItem("11_3", 11, "Search & Browser", "open_app", "Koi bhi installed app naam se seedha khol deta hai.", "\"WhatsApp kholo\"", actionType = "APP"),

            // 12. Screen Automation Tools
            ToolGuideItem("12_1", 12, "Screen Automation", "start_task", "Complex multi-step kaam ke liye background automation agent ko handoff karta hai.", "\"Zomato se pizza order kar do\"", actionType = "AUTOMATION"),
            ToolGuideItem("12_2", 12, "Screen Automation", "read_screen", "Accessibility tree se current screen padhta hai.", "internal background scanner", actionType = "AUTOMATION"),
            ToolGuideItem("12_3", 12, "Screen Automation", "visual_check", "Screenshot lekar visually confirm karta hai.", "internal validation", actionType = "AUTOMATION"),

            // 13. Connectors
            ToolGuideItem("13_1", 13, "Connectors", "google_calendar", "Google Calendar ke agle events list karta hai.", "\"Mere upcoming events dikhao\"", actionType = "CONNECTORS"),
            ToolGuideItem("13_2", 13, "Connectors", "google_drive", "Drive ki recent files list karta hai.", "\"Drive ki recent files dikhao\"", actionType = "CONNECTORS"),
            ToolGuideItem("13_3", 13, "Connectors", "github_repos", "Recently updated GitHub repos list karta hai.", "\"Mere GitHub repos dikhao\"", actionType = "CONNECTORS"),
            ToolGuideItem("13_4", 13, "Connectors", "canva_designs", "Recent Canva designs list karta hai.", "\"Canva designs dikhao\"", actionType = "CONNECTORS"),

            // 15. Automation & Triggers
            ToolGuideItem("15_1", 15, "Automation & Triggers", "create_trigger", "\"Agar X ho to Y karo\" conditional automation rules create karta hai.", "Settings → Triggers → New Rule", actionType = "TRIGGERS"),

            // 16. Safety Rules
            ToolGuideItem("16_1", 16, "Safety Rules", "otp_privacy", "MJ kabhi khud se OTP ya bank PIN nahi bolti, sirf tab jab aap explicitly poochhein.", "\"OTP batao\"", safetyGuard = "ENFORCED", actionType = "SAFETY"),
            ToolGuideItem("16_2", 16, "Safety Rules", "call_reject_guard", "Ringing call tabhi katega jab ~20 sec ke andar clearly 'reject/kaat do' bola ho.", "\"Reject karo\"", safetyGuard = "ENFORCED", actionType = "SAFETY"),
            ToolGuideItem("16_3", 16, "Safety Rules", "lock_command_instant", "Lock command bina confirmation turant execute hoti hai urgent safety ke liye.", "\"Lock kar do\"", safetyGuard = "ENFORCED", actionType = "SAFETY")
        )
    }
}
