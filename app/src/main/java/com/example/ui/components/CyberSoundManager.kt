package com.example.ui.components

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Procedural sci-fi & voice assistant sound effects engine for MYRA.
 * Synthesizes realistic mechanical servo clicks, voice listening wake chimes, AI response bells,
 * warp launches, digital swooshes, and audio feedback without requiring external sound files.
 */
object CyberSoundManager {
    private var toneGenerator: ToneGenerator? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    var soundEnabled: Boolean = true

    // Pre-synthesized PCM data for instantaneous zero-latency playback
    private val dialTickPcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 24, startFreq = 2400.0, endFreq = 950.0, sampleRate = 22050)
    }

    private val launchPcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 95, startFreq = 550.0, endFreq = 2800.0, sampleRate = 22050)
    }

    // Mic start wake chime (Ascending smooth double chime C5 -> G5)
    private val micStartPcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 523.25, durationMs = 45, decayRate = 3.0),
                ToneNote(freq = 783.99, durationMs = 80, decayRate = 2.5)
            ),
            sampleRate = 22050
        )
    }

    // Mic stop tone (Gentle descending G5 -> C5)
    private val micStopPcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 783.99, durationMs = 40, decayRate = 3.5),
                ToneNote(freq = 523.25, durationMs = 70, decayRate = 3.0)
            ),
            sampleRate = 22050
        )
    }

    // AI response chime (E5 -> A5 -> C6 harmonic triad)
    private val aiResponsePcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 659.25, durationMs = 35, decayRate = 3.2),
                ToneNote(freq = 880.00, durationMs = 35, decayRate = 3.2),
                ToneNote(freq = 1046.50, durationMs = 90, decayRate = 2.2)
            ),
            sampleRate = 22050
        )
    }

    // Message sent digital swoosh / laser chirp
    private val messageSentPcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 50, startFreq = 480.0, endFreq = 1650.0, sampleRate = 22050)
    }

    // Message received notification bell (A5 -> D6 pleasant arrival)
    private val messageReceivedPcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 880.0, durationMs = 50, decayRate = 3.0),
                ToneNote(freq = 1174.66, durationMs = 120, decayRate = 2.0)
            ),
            sampleRate = 22050
        )
    }

    // Voice preview audition chime (Soft crystal chime)
    private val voicePreviewChimePcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 40, startFreq = 1200.0, endFreq = 1760.0, sampleRate = 22050)
    }

    // Voice model switch sweep
    private val voiceSwitchPcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 70, startFreq = 620.0, endFreq = 1950.0, sampleRate = 22050)
    }

    // Tactile button click (sub-millisecond mechanical snap)
    private val buttonClickPcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 15, startFreq = 3000.0, endFreq = 600.0, sampleRate = 22050)
    }

    // Tab switch blip
    private val tabSwitchPcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 20, startFreq = 1400.0, endFreq = 1800.0, sampleRate = 22050)
    }

    // Success chord
    private val successPcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 523.25, durationMs = 40, decayRate = 3.0),
                ToneNote(freq = 659.25, durationMs = 40, decayRate = 3.0),
                ToneNote(freq = 783.99, durationMs = 40, decayRate = 3.0),
                ToneNote(freq = 1046.50, durationMs = 100, decayRate = 2.0)
            ),
            sampleRate = 22050
        )
    }

    // Error tone (dual low pulse)
    private val errorPcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 90, startFreq = 320.0, endFreq = 210.0, sampleRate = 22050)
    }

    // ==========================================
    // 💎 Google Gemini AI Sound Effects
    // ==========================================

    // Gemini Live Wake Chime: Smooth rising harmonic double chime (F5 698.46Hz -> Bb5 932.33Hz)
    private val geminiWakePcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 698.46, durationMs = 65, decayRate = 2.4),
                ToneNote(freq = 932.33, durationMs = 120, decayRate = 2.0)
            ),
            sampleRate = 22050
        )
    }

    // Gemini AI Thinking Pulse: Gentle undulating dual harmonic
    private val geminiThinkingPcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 587.33, durationMs = 45, decayRate = 3.0),
                ToneNote(freq = 783.99, durationMs = 45, decayRate = 3.0),
                ToneNote(freq = 587.33, durationMs = 70, decayRate = 2.5)
            ),
            sampleRate = 22050
        )
    }

    // Gemini AI Response Bell: Warm, uplifting major arrival chord (F5 -> A5 -> C6 -> F6)
    private val geminiResponsePcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 698.46, durationMs = 40, decayRate = 3.0),
                ToneNote(freq = 880.00, durationMs = 45, decayRate = 2.8),
                ToneNote(freq = 1046.50, durationMs = 50, decayRate = 2.5),
                ToneNote(freq = 1396.91, durationMs = 110, decayRate = 2.0)
            ),
            sampleRate = 22050
        )
    }

    // ==========================================
    // 🤖 OpenAI GPT-4 / ChatGPT Sound Effects
    // ==========================================

    // GPT-4 Voice Mode Activation: Warm resonant base swell followed by crisp high drop
    private val gpt4WakePcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 261.63, durationMs = 50, decayRate = 2.2), // C4 swell
                ToneNote(freq = 392.00, durationMs = 50, decayRate = 2.2), // G4
                ToneNote(freq = 523.25, durationMs = 55, decayRate = 2.0), // C5
                ToneNote(freq = 1318.51, durationMs = 90, decayRate = 2.6)  // E6 drop
            ),
            sampleRate = 22050
        )
    }

    // GPT-4 Ambient Listening Ping: Subtle crystal waterdrop
    private val gpt4ListeningPingPcm: ShortArray by lazy {
        generateChirpPcm(durationMs = 30, startFreq = 1567.98, endFreq = 1760.00, sampleRate = 22050)
    }

    // GPT-4 Voice Interruption / Stop Cue: Sharp muted dual blip
    private val gpt4InterruptPcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 880.00, durationMs = 25, decayRate = 5.0),
                ToneNote(freq = 554.37, durationMs = 35, decayRate = 4.5)
            ),
            sampleRate = 22050
        )
    }

    // GPT-4 Realtime Session Connected: Four-note harmonious chord
    private val gpt4ConnectedPcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 523.25, durationMs = 45, decayRate = 2.8),
                ToneNote(freq = 659.25, durationMs = 45, decayRate = 2.8),
                ToneNote(freq = 783.99, durationMs = 50, decayRate = 2.6),
                ToneNote(freq = 987.77, durationMs = 110, decayRate = 2.0)
            ),
            sampleRate = 22050
        )
    }

    // ==========================================
    // ⚡ ElevenLabs Studio Sound Effects
    // ==========================================

    // ElevenLabs Neural Sparkle: Ascending high-frequency crystal arpeggio
    private val elevenLabsSparklePcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 1046.50, durationMs = 30, decayRate = 3.5),
                ToneNote(freq = 1318.51, durationMs = 30, decayRate = 3.2),
                ToneNote(freq = 1567.98, durationMs = 35, decayRate = 2.8),
                ToneNote(freq = 2093.00, durationMs = 95, decayRate = 2.0)
            ),
            sampleRate = 22050
        )
    }

    // ElevenLabs Stream Ready Tone: Clean studio broadcast chime
    private val elevenLabsReadyPcm: ShortArray by lazy {
        generateSequencePcm(
            listOf(
                ToneNote(freq = 880.00, durationMs = 40, decayRate = 3.0),
                ToneNote(freq = 1318.51, durationMs = 95, decayRate = 2.2)
            ),
            sampleRate = 22050
        )
    }

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (_: Throwable) {}
    }

    private data class ToneNote(val freq: Double, val durationMs: Int, val decayRate: Double = 3.0)

    private fun generateChirpPcm(
        durationMs: Int,
        startFreq: Double,
        endFreq: Double,
        sampleRate: Int
    ): ShortArray {
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)
        var phase = 0.0
        for (i in 0 until numSamples) {
            val t = i.toDouble() / numSamples
            val freq = startFreq + (endFreq - startFreq) * t
            val envelope = exp(-3.8 * t) // Fast decay
            phase += 2.0 * PI * freq / sampleRate
            val sample = (sin(phase) * envelope * 24000.0).toInt().coerceIn(-32767, 32767)
            buffer[i] = sample.toShort()
        }
        return buffer
    }

    private fun generateSequencePcm(
        notes: List<ToneNote>,
        sampleRate: Int
    ): ShortArray {
        val totalDurationMs = notes.sumOf { it.durationMs }
        val totalSamples = (totalDurationMs * sampleRate) / 1000
        val buffer = ShortArray(totalSamples)
        var currentIndex = 0

        for (note in notes) {
            val noteSamples = (note.durationMs * sampleRate) / 1000
            var phase = 0.0
            for (i in 0 until noteSamples) {
                if (currentIndex + i >= totalSamples) break
                val t = i.toDouble() / noteSamples
                val envelope = exp(-note.decayRate * t)
                phase += 2.0 * PI * note.freq / sampleRate
                // Add warm harmonic overtone (second harmonic at half amplitude)
                val fundamental = sin(phase)
                val overtone = sin(phase * 2.0) * 0.3
                val sample = ((fundamental + overtone) * envelope * 20000.0).toInt().coerceIn(-32767, 32767)
                buffer[currentIndex + i] = sample.toShort()
            }
            currentIndex += noteSamples
        }
        return buffer
    }

    /**
     * Plays the high-tech sci-fi mechanical click/tick when the rotary wheel rotates.
     */
    fun playDialTick() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(dialTickPcm, 22050)
            } catch (_: Throwable) {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 25)
                } catch (_: Throwable) {}
            }
        }
    }

    /**
     * Plays a futuristic cyber warp sound when an application is launched.
     */
    fun playLaunchSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(launchPcm, 22050)
            } catch (_: Throwable) {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 50)
                } catch (_: Throwable) {}
            }
        }
    }

    /**
     * Plays ascending voice wake chime when mic starts listening.
     */
    fun playMicStart() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(micStartPcm, 22050)
            } catch (_: Throwable) {
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
                } catch (_: Throwable) {}
            }
        }
    }

    /**
     * Plays descending soft tone when voice stops or mic mutes.
     */
    fun playMicStop() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(micStopPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays cheerful arrival chime when AI replies.
     */
    fun playAiResponse() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(aiResponsePcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays digital swoosh when a message or prompt is sent.
     */
    fun playMessageSent() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(messageSentPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays two-tone notification bell when a new message arrives.
     */
    fun playMessageReceived() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(messageReceivedPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays musical crystal chime before previewing a voice.
     */
    fun playVoicePreviewChime() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(voicePreviewChimePcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays sleek cyber sweep when a new voice is selected.
     */
    fun playVoiceSwitch() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(voiceSwitchPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Ultra-crisp mechanical button click.
     */
    fun playButtonClick() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(buttonClickPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Crisp blip on tab switch.
     */
    fun playTabSwitch() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(tabSwitchPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Major chord success chime.
     */
    fun playSuccess() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(successPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Gentle warning buzz.
     */
    fun playError() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(errorPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    // ==========================================
    // 💎 Public Gemini AI Sound Methods
    // ==========================================

    /**
     * Plays the official Google Gemini AI Live rising double wake chime.
     */
    fun playGeminiLiveWake() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(geminiWakePcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays the subtle Gemini AI thinking harmonic pulse.
     */
    fun playGeminiThinkingSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(geminiThinkingPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays the warm uplifting Gemini AI completion bell chord.
     */
    fun playGeminiResponseChime() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(geminiResponsePcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    // ==========================================
    // 🤖 Public OpenAI GPT-4 / ChatGPT Sound Methods
    // ==========================================

    /**
     * Plays the famous ChatGPT Voice Mode circle activation gong/swell sound.
     */
    fun playGpt4WakeSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(gpt4WakePcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays the gentle ambient ChatGPT listening waterdrop ping.
     */
    fun playGpt4ListeningPing() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(gpt4ListeningPingPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays the crisp ChatGPT Voice interruption cue.
     */
    fun playGpt4InterruptSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(gpt4InterruptPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays the OpenAI Realtime session established chord.
     */
    fun playGpt4ConnectedSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(gpt4ConnectedPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    // ==========================================
    // ⚡ Public ElevenLabs Sound Methods
    // ==========================================

    /**
     * Plays the crystal neural synthesis sparkle chime of ElevenLabs.
     */
    fun playElevenLabsChime() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(elevenLabsSparklePcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Plays the ElevenLabs high-fidelity audio stream ready chime.
     */
    fun playElevenLabsReady() {
        if (!soundEnabled) return
        scope.launch {
            try {
                playPcm(elevenLabsReadyPcm, 22050)
            } catch (_: Throwable) {}
        }
    }

    fun getAllAiSoundEffects(): List<AiSoundEffectItem> = listOf(
        AiSoundEffectItem(
            id = "gemini_wake",
            name = "Gemini Live Wake Chime",
            provider = "Google Gemini AI",
            category = "Gemini AI",
            description = "Ascending dual chime (F5 -> Bb5) when Gemini Live starts listening.",
            iconEmoji = "💎",
            playAction = { playGeminiLiveWake() }
        ),
        AiSoundEffectItem(
            id = "gemini_thinking",
            name = "Gemini AI Thinking Pulse",
            provider = "Google Gemini AI",
            category = "Gemini AI",
            description = "Soft undulating harmonic pulse while Gemini ponders reasoning.",
            iconEmoji = "💎",
            playAction = { playGeminiThinkingSound() }
        ),
        AiSoundEffectItem(
            id = "gemini_response",
            name = "Gemini AI Response Bell",
            provider = "Google Gemini AI",
            category = "Gemini AI",
            description = "Warm major triad chord when Gemini AI answers.",
            iconEmoji = "💎",
            playAction = { playGeminiResponseChime() }
        ),
        AiSoundEffectItem(
            id = "gpt4_wake",
            name = "GPT-4 Voice Mode Activation",
            provider = "OpenAI ChatGPT",
            category = "GPT-4 (ChatGPT)",
            description = "Iconic low harmonic swell with high drop when ChatGPT Voice wakes.",
            iconEmoji = "🤖",
            playAction = { playGpt4WakeSound() }
        ),
        AiSoundEffectItem(
            id = "gpt4_connected",
            name = "GPT-4 Realtime Connected",
            provider = "OpenAI ChatGPT",
            category = "GPT-4 (ChatGPT)",
            description = "Four-note chord when ChatGPT Voice establishes realtime channel.",
            iconEmoji = "🤖",
            playAction = { playGpt4ConnectedSound() }
        ),
        AiSoundEffectItem(
            id = "gpt4_ping",
            name = "GPT-4 Listening Waterdrop",
            provider = "OpenAI ChatGPT",
            category = "GPT-4 (ChatGPT)",
            description = "Gentle crystal drop confirming ongoing speech detection.",
            iconEmoji = "🤖",
            playAction = { playGpt4ListeningPing() }
        ),
        AiSoundEffectItem(
            id = "gpt4_interrupt",
            name = "GPT-4 Voice Interrupted",
            provider = "OpenAI ChatGPT",
            category = "GPT-4 (ChatGPT)",
            description = "Clean dual-pulse blip when user interrupts assistant speech.",
            iconEmoji = "🤖",
            playAction = { playGpt4InterruptSound() }
        ),
        AiSoundEffectItem(
            id = "eleven_sparkle",
            name = "ElevenLabs Neural Sparkle",
            provider = "ElevenLabs",
            category = "ElevenLabs",
            description = "Pristine high-treble crystalline arpeggio during voice synthesis.",
            iconEmoji = "⚡",
            playAction = { playElevenLabsChime() }
        ),
        AiSoundEffectItem(
            id = "eleven_ready",
            name = "ElevenLabs Stream Ready",
            provider = "ElevenLabs",
            category = "ElevenLabs",
            description = "Studio broadcast harmonic chime when audio begins streaming.",
            iconEmoji = "⚡",
            playAction = { playElevenLabsReady() }
        ),
        AiSoundEffectItem(
            id = "cyber_start",
            name = "MJ Cyber Assistant Wake",
            provider = "Procedural Synth",
            category = "Cyber MJ",
            description = "Ascending smooth double chime C5 -> G5 for AI core wake.",
            iconEmoji = "🔴",
            playAction = { playMicStart() }
        ),
        AiSoundEffectItem(
            id = "cyber_launch",
            name = "Futuristic Warp Launch",
            provider = "Procedural Synth",
            category = "Cyber MJ",
            description = "Sci-fi frequency warp for rapid task acceleration.",
            iconEmoji = "🚀",
            playAction = { playLaunchSound() }
        ),
        AiSoundEffectItem(
            id = "cyber_tick",
            name = "Mechanical Servo Click",
            provider = "Procedural Synth",
            category = "Cyber MJ",
            description = "Sub-millisecond tactile snap for mechanical feedback.",
            iconEmoji = "⚙️",
            playAction = { playDialTick() }
        )
    )

    private var activeAudioTrack: AudioTrack? = null
    private val audioTrackLock = Any()

    private fun playPcm(pcm: ShortArray, sampleRate: Int) {
        if (!soundEnabled || pcm.isEmpty()) return
        try {
            synchronized(audioTrackLock) {
                try {
                    activeAudioTrack?.stop()
                    activeAudioTrack?.release()
                } catch (_: Throwable) {}
                activeAudioTrack = null

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(pcm.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                if (track.state == AudioTrack.STATE_INITIALIZED) {
                    track.write(pcm, 0, pcm.size)
                    track.play()
                    activeAudioTrack = track

                    scope.launch {
                        kotlinx.coroutines.delay((pcm.size.toLong() * 1000 / sampleRate) + 60)
                        synchronized(audioTrackLock) {
                            if (activeAudioTrack == track) {
                                try {
                                    track.stop()
                                    track.release()
                                } catch (_: Throwable) {}
                                activeAudioTrack = null
                            }
                        }
                    }
                } else {
                    try { track.release() } catch (_: Throwable) {}
                }
            }
        } catch (_: Throwable) {
            // Failsafe: never crash app on audio track allocation issues
        }
    }
}

data class AiSoundEffectItem(
    val id: String,
    val name: String,
    val provider: String,
    val category: String, // "Gemini AI", "GPT-4 (ChatGPT)", "ElevenLabs", "Cyber MJ"
    val description: String,
    val iconEmoji: String,
    val playAction: () -> Unit
)

enum class AiSoundTheme(
    val id: String,
    val title: String,
    val provider: String,
    val description: String,
    val icon: String
) {
    GEMINI_AI(
        id = "GEMINI_AI",
        title = "Google Gemini Live Chimes",
        provider = "Google Gemini AI",
        description = "Iconic dual chime, thinking pulse & warm response bells",
        icon = "💎"
    ),
    GPT4_VOICE(
        id = "GPT4_VOICE",
        title = "OpenAI GPT-4 Voice Mode",
        provider = "OpenAI ChatGPT",
        description = "Voice Mode orb wake, listening ping & interruption cues",
        icon = "🤖"
    ),
    ELEVEN_LABS(
        id = "ELEVEN_LABS",
        title = "ElevenLabs Studio Audio",
        provider = "ElevenLabs Neural",
        description = "Crystal neural sparkle, high-fidelity stream ready chimes",
        icon = "⚡"
    ),
    CYBER_MJ(
        id = "CYBER_MJ",
        title = "MJ Cyber Sci-Fi Tones",
        provider = "Procedural Synth",
        description = "Tactile servo clicks, warp transitions & robotic feedback",
        icon = "🔴"
    )
}

