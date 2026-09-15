package com.example.model

import com.example.R

enum class OrbStyle(
    val displayName: String,
    val subtitle: String,
    val drawableResId: Int,
    val primaryColorHex: Long,
    val secondaryColorHex: Long
) {
    CLASSIC(
        "Crimson Vortex",
        "Cyber Reactor",
        R.drawable.img_vortex_3d_core_1789293182205,
        0xFFFF2A55,
        0xFFFF7597
    ),
    QUANTUM_NEURAL(
        "Quantum Neural",
        "Starfield Constellation",
        R.drawable.core_quantum_neural_1789294998434,
        0xFF9333EA,
        0xFF06B6D4
    ),
    CYBER_MATRIX(
        "Cyber Matrix",
        "Laser Vector Grid",
        R.drawable.core_cyber_matrix_1789295010174,
        0xFF00E5FF,
        0xFFFF007F
    ),
    GOLDEN_REACTOR(
        "Golden Reactor",
        "Arc Plasma Wave",
        R.drawable.core_golden_reactor_1789295023659,
        0xFFFFD700,
        0xFFFF0055
    ),
    HARMONIC_RIBBONS(
        "Harmonic Ribbons",
        "Sine Wave HUD",
        R.drawable.core_harmonic_ribbons_1789295036685,
        0xFFFF2A6D,
        0xFF05D9E8
    ),
    NEBULA_PULSAR(
        "Nebula Pulsar",
        "Cosmic Singularity",
        R.drawable.core_nebula_pulsar_1789295068235,
        0xFFC026D3,
        0xFF7C3AED
    ),
    SOLAR_PROMINENCE(
        "Solar Prominence",
        "Fusion Flare",
        R.drawable.core_solar_prominence_1789295081572,
        0xFFFF6B00,
        0xFFFFD600
    ),
    CHRONO_GYRO(
        "Chrono Gyroscope",
        "Temporal Rings",
        R.drawable.core_chrono_gyro_1789295094318,
        0xFFE0A96D,
        0xFF20B2AA
    ),
    CYBERPUNK_GLITCH(
        "Cyberpunk Glitch",
        "Neon Shield",
        R.drawable.core_cyberpunk_glitch_1789295121550,
        0xFFFF0055,
        0xFF00F0FF
    ),
    ASTRAL_LOTUS(
        "Astral Lotus",
        "Sacred Hologram",
        R.drawable.core_astral_lotus_1789295106978,
        0xFF10B981,
        0xFF8B5CF6
    ),
    DARK_MATTER(
        "Dark Matter",
        "Event Horizon",
        R.drawable.core_dark_matter_1789295134596,
        0xFFFF1744,
        0xFF4A000D
    );

    companion object {
        val ENERGY: OrbStyle get() = QUANTUM_NEURAL
        val NEON: OrbStyle get() = CYBER_MATRIX
        val SUPERNOVA: OrbStyle get() = SOLAR_PROMINENCE
        val HOLOGRAM: OrbStyle get() = HARMONIC_RIBBONS
    }
}

enum class AuraTheme(val displayName: String, val primaryHex: Long, val glowHex: Long) {
    DEFAULT("Default", 0xFFFF2A55, 0x88FF2A55),
    AURA_RED("Aura Red", 0xFFFF0033, 0x99FF0033),
    MIDNIGHT("Midnight", 0xFF9D00FF, 0x889D00FF),
    CYBER_BLUE("Cyber Blue", 0xFF00E5FF, 0x8800E5FF)
}

data class OrbSettings(
    val style: OrbStyle = OrbStyle.CLASSIC,
    val sizeScale: Float = 0.85f,
    val colorHue: Float = 200f,
    val auraBorderMode: Boolean = false,
    val voiceVisualizer: Boolean = true,
    val particleSpeed: Float = 1.0f,
    val glowIntensity: Float = 0.9f
)

data class AuraSettings(
    val theme: AuraTheme = AuraTheme.DEFAULT,
    val signatureName: String = "Crimson Aura",
    val language: String = "Auto (Hinglish)",
    val hapticEnabled: Boolean = true,
    val isLifetimeUnlocked: Boolean = false,
    val activeSystemMode: String = "Balanced AI Core"
)

enum class VoiceGender(val displayName: String) {
    ALL("All"),
    FEMALE("Female"),
    MALE("Male")
}

enum class PersonalityMode(val displayName: String) {
    NORMAL("Normal"),
    GF_MODE("GF Mode"),
    FRIEND_MODE("Friend Mode"),
    NAUTANKI_MODE("Nautanki Mode"),
    FUNNY_MODE("Funny Mode"),
    HUNGRY_MODE("Hungry Mode"),
    ROASTER_MODE("Roaster Mode"),
    ANIME_MODE("Anime Mode"),
    PROFESSIONAL("Professional")
}

data class VoiceModelItem(
    val id: String,
    val name: String,
    val description: String,
    val gender: VoiceGender,
    val language: String = "English",
    val isFavorite: Boolean = false,
    val previewSampleText: String = "Hello! I am MJ, your intelligent personal assistant."
)

enum class ConnectorCategory(val displayName: String) {
    ALL("All"),
    CLOUD_SERVICES("Cloud Connectors"),
    AI_MODELS("AI & Models"),
    VOICE_MEDIA("Voice & Media"),
    CREATIVE("Creative"),
    PRODUCTIVITY("Productivity"),
    MEDIA("Media"),
    AUTOMATION("Automation")
}

enum class ConnectorStatus(val label: String) {
    NOT_CONNECTED("Not connected"),
    CONNECTED("Connected"),
    ERROR("Error"),
    NEEDS_AUTH("Needs Auth")
}

data class ConnectorItem(
    val id: String,
    val name: String,
    val description: String,
    val category: ConnectorCategory,
    val status: ConnectorStatus = ConnectorStatus.NOT_CONNECTED,
    val apiKey: String = "",
    val badge: String = "Connector",
    val roleBadge: String = "",
    val isLiveConnector: Boolean = false,
    val authorizedAccount: String? = null,
    val permissionsGranted: List<String> = emptyList(),
    val iconLetter: String = name.firstOrNull()?.toString() ?: "C"
) {
    val isConnected: Boolean get() = status == ConnectorStatus.CONNECTED
}

data class ToolGuideItem(
    val id: String,
    val sectionNumber: Int,
    val sectionTitle: String,
    val toolName: String,
    val kyaKartaHai: String,
    val bolneWalaCommand: String,
    val safetyGuard: String? = null,
    val actionType: String = "SYSTEM"
)

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val actionTag: String? = null,
    val isSpeaking: Boolean = false
)

data class TriggerRule(
    val id: String,
    val title: String,
    val triggerType: String,
    val conditionText: String,
    val actionText: String,
    val isEnabled: Boolean
)

data class MissionItem(
    val id: String,
    val title: String,
    val status: String, // "Active", "Completed", "Paused"
    val progress: Float,
    val currentStep: String,
    val totalSteps: Int,
    val logs: List<String>
)

data class PermissionItemModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val permissions: List<String>,
    val iconName: String,
    val isGranted: Boolean = false,
    val isCrucial: Boolean = true
)

data class VoiceSettings(
    // Voice Speed
    val speed: Float = 1.0f, // 0.5x to 2.0x

    // Voice Pitch
    val pitch: String = "Normal", // "Low", "Normal", "High"

    // Speaking Behavior
    val fastResponseMode: Boolean = true,
    val naturalPauses: Boolean = true,
    val expressiveVoice: Boolean = true,
    val interruptWhileSpeaking: Boolean = true,
    val autoStopWhenUserStartsTalking: Boolean = true,
    val continueSpeakingAfterInterruption: Boolean = true,

    // Voice Detection
    val noiseSuppression: Boolean = true,
    val echoCancellation: Boolean = true,
    val autoMicGain: Boolean = true,
    val voiceActivityDetection: Boolean = true,
    val backgroundNoiseFilter: Boolean = true,

    // Advanced
    val audioQuality: String = "Standard", // "Low", "Standard", "High"
    val sampleRateText: String = "Auto (16kHz in / 24kHz out) - the only rate Gemini Live's API actually supports, so no other value is offered here.",
    val streamingResponse: Boolean = true,
    val latencyMode: String = "Ultra Fast", // "Ultra Fast", "Balanced", "Quality"
    val reconnectAutomatically: Boolean = true,
    val voiceTimeout: String = "30 sec" // "10 sec", "30 sec", "60 sec", "5 min", "Never"
)

data class IncomingCallData(
    val callerName: String,
    val callerNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSpam: Boolean = false,
    val location: String = "Mobile"
)

data class IncomingSmsData(
    val senderName: String,
    val senderNumber: String,
    val messageBody: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isOtp: Boolean = false,
    val otpCode: String? = null
)

enum class MjBackgroundTheme(
    val id: String,
    val displayName: String,
    val hindiName: String,
    val bgPrimaryHex: Long,
    val bgSecondaryHex: Long,
    val surfaceHex: Long,
    val accentGlowHex: Long,
    val borderHex: Long
) {
    CYBER_DARK("cyber_dark", "Cyber Dark", "साइबर डार्क", 0xFF07080B, 0xFF0D0E15, 0xFF141622, 0xFFFF2A55, 0xFF2E1724),
    ROYAL_PURPLE("royal_purple", "Royal Purple (Anime Video)", "रॉयल पर्पल (वीडियो थीम)", 0xFF0E071D, 0xFF190D32, 0xFF231442, 0xFFB026FF, 0xFF3D1863),
    AMETHYST_NEON("amethyst_neon", "Neon Violet", "नियॉन वॉयलेट", 0xFF0A0518, 0xFF15082C, 0xFF1F0D3D, 0xFFD946EF, 0xFF4A1566),
    CRIMSON_BLOOD("crimson_blood", "Crimson Red", "क्रिमसन रेड", 0xFF140306, 0xFF22060A, 0xFF30080F, 0xFFFF0033, 0xFF520E1A),
    CYBER_CYAN("cyber_cyan", "Electric Cyan", "इलेक्ट्रिक सियान", 0xFF02121A, 0xFF051E2B, 0xFF092E40, 0xFF00E5FF, 0xFF0E435C),
    MATRIX_GREEN("matrix_green", "Matrix Emerald", "मैट्रिक्स ग्रीन", 0xFF02140A, 0xFF052111, 0xFF0A301A, 0xFF00E676, 0xFF0E4525),
    GOLDEN_SUNSET("golden_sunset", "Imperial Gold", "शाही गोल्ड", 0xFF140D02, 0xFF211504, 0xFF301E06, 0xFFFFD600, 0xFF54340A),
    AMOLED_PITCH("amoled_pitch", "AMOLED Pitch Black", "AMOLED गहरा काला", 0xFF000000, 0xFF050505, 0xFF0D0D0D, 0xFFFFFFFF, 0xFF222222)
}

enum class AppScreen {
    HOME,
    CHAT,
    TOOLS_GUIDE,
    AURA_CONTROL,
    ORB_CUSTOMIZE,
    CONNECTORS,
    TRIGGERS,
    MISSIONS,
    PERMISSIONS,
    SETTINGS,
    VOICE_SETTINGS,
    CHOOSE_TRIGGER_TYPE,
    VOICE_AI_MODELS,
    API_CLOUD_SETTINGS,
    VOICE_AUTH,
    WAKE_WORD,
    INTELLIGENCE_MODES,
    PC_CONNECT,
    LICENSE_ACTIVATION,
    SUBSCRIPTION,
    USER_PROFILE,
    BATCH_UPDATE,
    ACCOUNT,
    DEEP_RESEARCH_SETTINGS,
    AI_IDENTITY,
    FULL_DEVICE_CONTROL,
    CALL_SMS_ANNOUNCER,
    BG_COLOR_CUSTOMIZER,
    MAYA_SUITE,
    FILE_MANAGER,
    CONVERSATION_DATABASE,
    PHONE_LAUNCHER
}

enum class PcPowerAction {
    SHUTDOWN, RESTART, SLEEP, LOCK
}

enum class PcWindowAction {
    MINIMIZE, MAXIMIZE, RESTORE, SHOW_DESKTOP, CLOSE
}

data class PcAppInfo(
    val id: String,
    val name: String,
    val processName: String,
    val iconName: String,
    val isRunning: Boolean,
    val isFocused: Boolean = false,
    val memoryUsageMb: Int = 120
)

data class TerminalLogEntry(
    val id: String,
    val command: String,
    val output: String,
    val isSuccess: Boolean = true,
    val timestamp: String
)

data class ManagedFileItem(
    val id: String,
    val name: String,
    val path: String,
    val sizeString: String,
    val extension: String,
    val modifiedDate: String,
    val isDirectory: Boolean = false,
    val isPcFile: Boolean = false,
    val textPreview: String? = null
)

enum class VoicePipelineStage(
    val stageNumber: Int,
    val titleHindi: String,
    val titleEng: String,
    val subtitle: String,
    val icon: String
) {
    IDLE(0, "स्टैंडबाय", "Standby", "Tap mic or say 'Hey MJ'", "⏸️"),
    USER_SPEAKING(1, "आप बोलेंगे", "You Speak", "Listening to your voice...", "🎙️"),
    SPEECH_TO_TEXT(2, "Speech-to-Text", "Voice Transcription", "Converting speech to text...", "📝"),
    MYRA_AI_BRAIN(3, "MYRA AI Brain", "Neural Processing", "Gemini 3.5 AI thinking...", "🧠"),
    AI_ANSWER(4, "AI जवाब", "AI Response Ready", "Crafting smart answer...", "💡"),
    ELEVENLABS_TTS(5, "ElevenLabs TTS", "Voice Synthesis", "Generating hyper-realistic audio...", "🔊"),
    MYRA_SPEAKING(6, "MYRA आवाज़ में जवाब", "Audio Playback", "Speaking reply in natural voice", "🗣️")
}

data class ElevenLabsVoice(
    val voiceId: String,
    val name: String,
    val gender: VoiceGender,
    val accent: String,
    val category: String,
    val description: String,
    val language: String = "English",
    val languageGroup: String = "English",
    val flagEmoji: String = "🌐",
    val isHindiOptimized: Boolean = false,
    val previewSampleText: String = "Namaste! Main MYRA hoon, ElevenLabs hyper-realistic voice mein baat kar rahi hoon."
)

enum class TtsEngine(val id: String, val displayName: String, val badge: String, val description: String) {
    ELEVEN_LABS("eleven_labs", "ElevenLabs TTS", "Hyper-Realistic", "Studio-grade neural voice synthesis"),
    GEMINI_TTS("gemini_tts", "Google Gemini Ultra-HD", "Ultra-HD", "Google Gemini neural voice engine"),
    OPENAI_TTS("openai_tts", "OpenAI ChatGPT Voice", "Studio", "OpenAI TTS-1 neural voice model"),
    SYSTEM_TTS("system_tts", "Android Neural Voice", "Offline Ready", "On-device low latency TTS engine")
}

