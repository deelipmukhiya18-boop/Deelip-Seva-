package com.example.data

import com.example.model.ElevenLabsVoice
import com.example.model.VoiceGender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object ElevenLabsCatalog {

    val hindiVoices = listOf(
        ElevenLabsVoice(
            voiceId = "21m00Tcm4TlvDq8ikWAM", // Aditi alias on Multilingual V2
            name = "Aditi (अदिति)",
            gender = VoiceGender.FEMALE,
            accent = "Indian Hindi",
            category = "Assistant / Natural",
            description = "Natural, warm & polite Indian Hindi female voice. Perfect for daily Hindi-Hinglish conversation.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "नमस्ते! मैं अदिति हूँ। मैं मायरा में ग्यारह लैब्स की सबसे नैचुरल हिंदी आवाज़ में बात कर रही हूँ।"
        ),
        ElevenLabsVoice(
            voiceId = "ErXwobaYiN019PkySvjV", // Aryan alias
            name = "Aryan (आर्यन)",
            gender = VoiceGender.MALE,
            accent = "Indian Hindi",
            category = "Tech / Modern",
            description = "Energetic, clear & confident young Indian Hindi male voice. Great for tech, coding & tasks.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "नमस्ते बॉस! मैं आर्यन हूँ। आपकी पूरी कमांड्स और टास्क अब सुपर-फास्ट स्पीड में पूरे होंगे।"
        ),
        ElevenLabsVoice(
            voiceId = "EXAVITQu4vr4xnSDxMaL", // Priya alias
            name = "Priya (प्रिया)",
            gender = VoiceGender.FEMALE,
            accent = "Indian Hindi",
            category = "Friendly / Warm",
            description = "Sweet, expressive and friendly Indian Hindi voice with emotional depth.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "हेलो! मैं प्रिया हूँ। मायरा के साथ हर बातचीत अब और भी प्यारी और आसान होगी।"
        ),
        ElevenLabsVoice(
            voiceId = "pNInz6obpgDQGcFmaJgB", // Kabir alias
            name = "Kabir (कबीर)",
            gender = VoiceGender.MALE,
            accent = "Indian Hindi",
            category = "Deep / Authoritative",
            description = "Rich, deep baritone Indian Hindi male voice. Powerful narration and commanding assistant.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "नमस्ते। मैं कबीर हूँ। आपकी सुरक्षा, फाइल्स और सिस्टम्स मेरे पूरे नियंत्रण में हैं।"
        ),
        ElevenLabsVoice(
            voiceId = "MF3mGyEYCl7XYWbV9V6O", // Neerja alias
            name = "Neerja (नीरजा)",
            gender = VoiceGender.FEMALE,
            accent = "Indian Hindi",
            category = "Expressive / AI",
            description = "Crisp, modern and intelligent Indian Hindi assistant voice.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "नमस्ते! मैं नीरजा हूँ। मैं आपकी नई डिजिटल साथी और वॉइस कोपायलट हूँ।"
        ),
        ElevenLabsVoice(
            voiceId = "TxGEqnHWrfWFTfGW9XjX", // Rohan alias
            name = "Rohan (रोहन)",
            gender = VoiceGender.MALE,
            accent = "Indian Hindi",
            category = "Youthful / Casual",
            description = "Youthful, chill and conversational Indian male tone. Ideal for friend and casual modes.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "अरे भाई! रोहन बोल रहा हूँ। बता क्या प्लान है, अभी चुटकियों में कर देते हैं!"
        ),
        ElevenLabsVoice(
            voiceId = "AZnzlk1XvdvUeBnXmlld", // Ananya alias
            name = "Ananya (अनन्या)",
            gender = VoiceGender.FEMALE,
            accent = "Indian Hindi",
            category = "Cheerful / Sweet",
            description = "Vibrant, cheerful Hindi female assistant with modern urban tone.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "नमस्ते! मैं अनन्या हूँ। मुस्कुराइए, आज का दिन बहुत ही शानदार होने वाला है!"
        ),
        ElevenLabsVoice(
            voiceId = "VR6AewLTigWG4xSOukaG", // Vikram alias
            name = "Vikram (विक्रम)",
            gender = VoiceGender.MALE,
            accent = "Indian Hindi",
            category = "News / Mature",
            description = "Mature, calm, professional Hindi broadcast voice.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "नमस्कार। मैं विक्रम हूँ। सभी मुख्य समाचार, अपडेट्स और आंकड़े तैयार हैं।"
        ),
        ElevenLabsVoice(
            voiceId = "pFZP5JQG7iQjIQuC4Bku", // Kavya alias
            name = "Kavya (काव्या)",
            gender = VoiceGender.FEMALE,
            accent = "Indian Hindi / Hinglish",
            category = "Urban / Hinglish",
            description = "Modern Hinglish female voice with natural Indian accent articulation.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "हाय! मैं काव्या हूँ। हिंदी हो या इंग्लिश, हम एकदम नैचुरल बात करेंगे।"
        ),
        ElevenLabsVoice(
            voiceId = "JBFqnCBsd6RMkjVDRZzb", // Dev alias
            name = "Dev (देव)",
            gender = VoiceGender.MALE,
            accent = "Indian Hindi",
            category = "Smart / Guide",
            description = "Warm, analytical and respectful Indian male voice.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "नमस्ते मित्र। मैं देव हूँ। आपके हर सवाल का सही समाधान मेरे पास उपलब्ध है।"
        ),
        ElevenLabsVoice(
            voiceId = "LcfcDJNigLAnTrGleoDa", // Simran alias
            name = "Simran (सिमरन)",
            gender = VoiceGender.FEMALE,
            accent = "Indian Hindi",
            category = "Melodious / Story",
            description = "Soft, melodic Hindi female voice. Wonderful for stories, poetry and meditation.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "नमस्ते। मैं सिमरन हूँ। शांत मन से सोचिए, हर मुश्किल का हल मिल जाएगा।"
        ),
        ElevenLabsVoice(
            voiceId = "ODq5zmih8GrVes37Dizd", // Arjun alias
            name = "Arjun (अर्जुन)",
            gender = VoiceGender.MALE,
            accent = "Indian Hindi",
            category = "Charismatic / Dynamic",
            description = "Dynamic, bold and confident Indian voice with motivational spirit.",
            language = "Hindi (हिन्दी)",
            languageGroup = "Hindi",
            flagEmoji = "🇮🇳",
            isHindiOptimized = true,
            previewSampleText = "जय हिन्द! मैं अर्जुन हूँ। अपने लक्ष्य पर ध्यान रखिए, सफलता निश्चित है।"
        )
    )

    // ==========================================
    // 🤖 ChatGPT (OpenAI) Voices in ElevenLabs
    // ==========================================
    val chatGptVoices = listOf(
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_alloy",
            name = "Alloy (ChatGPT)",
            gender = VoiceGender.FEMALE,
            accent = "Neutral / Modern AI",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT flagship voice. Crisp, balanced, neutral and versatile assistant tone.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Hello! I am Alloy from ChatGPT, now in ElevenLabs. Main bilkul natural aur fast jawab doongi."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_echo",
            name = "Echo (ChatGPT)",
            gender = VoiceGender.MALE,
            accent = "Deep Baritone / Command",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT authoritative, calm command center voice with deep resonance.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Echo voice online. Authoritative, steady, and clear command execution at your service."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_fable",
            name = "Fable (ChatGPT)",
            gender = VoiceGender.MALE,
            accent = "British / Storyteller",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT expressive, lyrical British storytelling voice with vivid intonation.",
            language = "English / Hindi",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Greetings! I am Fable from ChatGPT. Bringing stories and conversations vividly to life."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_onyx",
            name = "Onyx (ChatGPT)",
            gender = VoiceGender.MALE,
            accent = "Deep Resonance / Executive",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT rich baritone voice. Powerful, mature, commanding and reassuring.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Namaste. Main Onyx hoon. Har command ko precision aur bharose ke saath execute karunga."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_nova",
            name = "Nova (ChatGPT)",
            gender = VoiceGender.FEMALE,
            accent = "Warm / Empathetic Female",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT empathetic, warm, and highly expressive natural conversational tone.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Hi there! I am Nova from ChatGPT. Main hamesha aapki madad ke liye warm aur ready hoon!"
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_shimmer",
            name = "Shimmer (ChatGPT)",
            gender = VoiceGender.FEMALE,
            accent = "Clear Treble / Optimistic",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT clear, melodic high-treble studio voice with optimistic energy.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Hello! Shimmer here from ChatGPT. Crisp, cheerful, and ready to light up your tasks."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_breeze",
            name = "Breeze (ChatGPT)",
            gender = VoiceGender.MALE,
            accent = "Casual / Conversational",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Animated, casual, friendly and natural everyday cadence.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Hey friend! Breeze voice active from ChatGPT. What are we exploring today?"
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_cove",
            name = "Cove (ChatGPT)",
            gender = VoiceGender.MALE,
            accent = "Calm / Contemplative",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Calm, understated, relaxed and thoughtful male voice.",
            language = "English / Hindi",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Cove active. Relaxed, thoughtful, and steady assistance whenever you need."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_ember",
            name = "Ember (ChatGPT)",
            gender = VoiceGender.FEMALE,
            accent = "Confident / Rich Tone",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Confident, warm, engaging voice with subtle depth.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Hello! I am Ember from ChatGPT. Confident, warm, and ready to tackle any challenge."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_juniper",
            name = "Juniper (ChatGPT)",
            gender = VoiceGender.FEMALE,
            accent = "Spirited / Candid Female",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Spirited, candid, cheerful and delightfully natural.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Hey there! Juniper here from ChatGPT. Let's make today productive and fun!"
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_maple",
            name = "Maple (ChatGPT)",
            gender = VoiceGender.FEMALE,
            accent = "Playful / Radiant Female",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Cheerful, playful, warm and conversational.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Namaste! Maple voice active. Sweet, cheerful, aur hamesha helpful!"
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_sol",
            name = "Sol (ChatGPT)",
            gender = VoiceGender.MALE,
            accent = "Smooth / Sunny Warm",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Smooth, sunny, relaxed and easy to listen to.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Greetings. Sol voice active. Smooth, calm, aur helpful conversations ke liye."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_spruce",
            name = "Spruce (ChatGPT)",
            gender = VoiceGender.MALE,
            accent = "Crisp / Composed Male",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Composed, direct, polished and thoughtful.",
            language = "English / Hindi",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Spruce active. Polished and attentive, standing by for your next instruction."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_vale",
            name = "Vale (ChatGPT)",
            gender = VoiceGender.FEMALE,
            accent = "Gentle / Polite Female",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Gentle, thoughtful, polite and reassuring.",
            language = "Hindi / English",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Hello. Vale here from ChatGPT. It is wonderful to assist you today."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_chatgpt_arbor",
            name = "Arbor (ChatGPT)",
            gender = VoiceGender.MALE,
            accent = "Narrative / Grounded Male",
            category = "ChatGPT / OpenAI",
            description = "OpenAI ChatGPT Advanced Voice. Deep, easygoing, narrative style with natural pause cadence.",
            language = "English / Hindi",
            languageGroup = "ChatGPT",
            flagEmoji = "🤖",
            isHindiOptimized = true,
            previewSampleText = "Arbor voice active. Grounded, patient, and ready to walk through any problem."
        )
    )

    // ==========================================
    // ✨ Google AI Gemini Voices in ElevenLabs
    // ==========================================
    val geminiVoices = listOf(
        ElevenLabsVoice(
            voiceId = "eleven_gemini_puck",
            name = "Puck (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Playful / Witty Male",
            category = "Google Gemini Live",
            description = "Google Gemini flagship playful, quick, and witty male conversational voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Hey there! I am Puck from Google Gemini Live. Ready to chat, joke, and solve tasks!"
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_charon",
            name = "Charon (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Steady / Intellectual Baritone",
            category = "Google Gemini Live",
            description = "Google Gemini deep, steady, reliable and grounded intellectual male voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Charon online. Steady, reliable, and grounded in Google Gemini intelligence."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_kore",
            name = "Kore (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Sweet / Soothing Companion",
            category = "Google Gemini Live",
            description = "Google Gemini flagship soothing, sweet, and empathetic female companion voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Namaste! I am Kore from Google Gemini. Main bilkul shant aur pyari aawaz mein aapke saath hoon."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_fenrir",
            name = "Fenrir (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Commanding / Crisp Assistant",
            category = "Google Gemini Live",
            description = "Google Gemini strong, commanding, and resolute professional assistant voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Fenrir active. Strong, commanding, and resolute. All systems ready."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_aoede",
            name = "Aoede (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Melodic / Lyrical Female",
            category = "Google Gemini Live",
            description = "Google Gemini soft, lyrical, melodic, and musical female voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Aoede voice active. Soft, lyrical, and melodic for harmonious conversations."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_capella",
            name = "Capella (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Bright / Articulate Guide",
            category = "Google Gemini Live",
            description = "Google Gemini bright, articulate, clear female guide with sharp clarity.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Hello! Capella from Google Gemini here. Clear guidance and quick answers for you."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_orus",
            name = "Orus (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Friendly / Relatable Male",
            category = "Google Gemini Live",
            description = "Google Gemini friendly, natural, and relatable everyday conversational voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Hey! Orus here from Gemini. Friendly, natural, and ready to help whenever you need."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_zephyr",
            name = "Zephyr (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Light / Breezy Youthful",
            category = "Google Gemini Live",
            description = "Google Gemini light, breezy, refreshing, and youthful female voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Zephyr online. Light, breezy, and refreshing like a fresh breeze!"
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_achernar",
            name = "Achernar (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Smooth / Executive Female",
            category = "Google Gemini Live",
            description = "Google Gemini smooth, polished, and professional executive voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Achernar active. Clear, smooth, and professional execution."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_algieba",
            name = "Algieba (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Deep / Resonant Calm",
            category = "Google Gemini Live",
            description = "Google Gemini deep, resonant, and calm reassuring male voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Algieba active. Deep, resonant, and calm focus on every task."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_alnilam",
            name = "Alnilam (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Clear / Balanced Neutral",
            category = "Google Gemini Live",
            description = "Google Gemini clear, balanced, and neutral assistant voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Alnilam active. Balanced, neutral, and precise."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_autonoe",
            name = "Autonoe (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Mature / Authoritative Female",
            category = "Google Gemini Live",
            description = "Google Gemini mature, authoritative, and direct female voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Autonoe active. Mature, authoritative, and direct."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_callirrhoe",
            name = "Callirrhoe (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Warm / Conversational Female",
            category = "Google Gemini Live",
            description = "Google Gemini friendly, warm, and conversational female voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Hello! Callirrhoe here from Gemini. Warm, friendly, and always happy to help."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_despina",
            name = "Despina (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Lively / Energetic Female",
            category = "Google Gemini Live",
            description = "Google Gemini lively, enthusiastic, and ready-to-go female voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Despina voice active. High energy, enthusiasm, and ready for anything!"
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_enceladus",
            name = "Enceladus (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Calm / Meditative Male",
            category = "Google Gemini Live",
            description = "Google Gemini calm, peaceful, and soothing meditative male voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Enceladus active. Peaceful, soothing, and tranquil."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_erinome",
            name = "Erinome (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Gentle / Expressive Female",
            category = "Google Gemini Live",
            description = "Google Gemini gentle, expressive, and attentive female voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Erinome active. Gentle, expressive, and attentive to your every need."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_gacrux",
            name = "Gacrux (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Crisp / Articulate Female",
            category = "Google Gemini Live",
            description = "Google Gemini crisp, articulate, and precise assistant tone.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Gacrux active. Crisp, articulate, and accurate."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_iapetus",
            name = "Iapetus (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Broad / Theatrical Male",
            category = "Google Gemini Live",
            description = "Google Gemini broad, theatrical, and expressive male voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Iapetus active. Broad, theatrical, and expressive storytelling."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_laomedeia",
            name = "Laomedeia (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Nurturing / Reassuring Female",
            category = "Google Gemini Live",
            description = "Google Gemini gentle, nurturing, and reassuring female voice.",
            language = "Hindi / English",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Namaste! Main Laomedeia hoon. Ekdum pyari aur caring aawaz mein aapki sahayata ke liye."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_leda",
            name = "Leda (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Upbeat / Cheerful Female",
            category = "Google Gemini Live",
            description = "Google Gemini pleasant, upbeat, and cheerful female voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Leda active! Cheerful, upbeat, and making your day easier."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_pulcherrima",
            name = "Pulcherrima (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Radiant / Charming Female",
            category = "Google Gemini Live",
            description = "Google Gemini radiant, charming, and bright female voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Pulcherrima active. Radiant, charming, and bright."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_rasalgethi",
            name = "Rasalgethi (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Wise / Experienced Elder",
            category = "Google Gemini Live",
            description = "Google Gemini wise, experienced, and composed male voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Rasalgethi online. Wise, experienced, and composed counsel."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_sadachbia",
            name = "Sadachbia (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Soft-Spoken / Gentle Male",
            category = "Google Gemini Live",
            description = "Google Gemini soft-spoken, quiet, and subtle male voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Sadachbia active. Soft-spoken, quiet, and thoughtful."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_sadaltager",
            name = "Sadaltager (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Robust / Hearty Male",
            category = "Google Gemini Live",
            description = "Google Gemini robust, hearty, and dependable male voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Sadaltager active. Robust, hearty, and dependable support."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_schedar",
            name = "Schedar (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Solid / Resolute Male",
            category = "Google Gemini Live",
            description = "Google Gemini solid, grounded, and clear resolute voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Schedar active. Solid, grounded, and clear."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_sulafat",
            name = "Sulafat (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Clear / Resonant Female",
            category = "Google Gemini Live",
            description = "Google Gemini clear, resonant, and ringing female voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Sulafat active. Clear, resonant, and distinct tone."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_umbriel",
            name = "Umbriel (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Subdued / Serious Male",
            category = "Google Gemini Live",
            description = "Google Gemini subdued, serious, and steady male voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Umbriel active. Subdued, serious, and steady."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_vindemiatrix",
            name = "Vindemiatrix (Gemini AI)",
            gender = VoiceGender.FEMALE,
            accent = "Intelligent / Analytical Female",
            category = "Google Gemini Live",
            description = "Google Gemini intelligent, analytical, and focused female voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Vindemiatrix active. Intelligent, analytical, and focused on solutions."
        ),
        ElevenLabsVoice(
            voiceId = "eleven_gemini_zubenelgenubi",
            name = "Zubenelgenubi (Gemini AI)",
            gender = VoiceGender.MALE,
            accent = "Deep / Velvety Smooth",
            category = "Google Gemini Live",
            description = "Google Gemini deep, velvety, and smooth baritone voice.",
            language = "English / Hindi",
            languageGroup = "Gemini AI",
            flagEmoji = "✨",
            isHindiOptimized = true,
            previewSampleText = "Zubenelgenubi active. Deep, velvety, and smooth."
        )
    )

    val englishVoices = listOf(
        ElevenLabsVoice(
            voiceId = "21m00Tcm4TlvDq8ikWAM",
            name = "Rachel",
            gender = VoiceGender.FEMALE,
            accent = "American / Conversational",
            category = "Assistant / Flagship",
            description = "Calm, warm, articulate female voice - ElevenLabs flagship voice for MYRA.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Hello! I am Rachel, the flagship neural voice of ElevenLabs in MYRA assistant."
        ),
        ElevenLabsVoice(
            voiceId = "EXAVITQu4vr4xnSDxMaL",
            name = "Bella",
            gender = VoiceGender.FEMALE,
            accent = "American / Expressive",
            category = "Emotional / Warm",
            description = "Sweet, cheerful and gentle female voice with rich emotion.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Hi there! I am Bella, ready to help you with anything you need today."
        ),
        ElevenLabsVoice(
            voiceId = "AZnzlk1XvdvUeBnXmlld",
            name = "Domi",
            gender = VoiceGender.FEMALE,
            accent = "American / Strong",
            category = "Confident / Direct",
            description = "Strong, crisp, assertive voice with modern cadence.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Hello. Domi here. Direct, concise, and focused on executing your commands."
        ),
        ElevenLabsVoice(
            voiceId = "MF3mGyEYCl7XYWbV9V6O",
            name = "Elli",
            gender = VoiceGender.FEMALE,
            accent = "American / Youthful",
            category = "Friendly / Storyteller",
            description = "Bright, youthful and clear female voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Hey! I'm Elli, excited to chat and create wonderful things with you!"
        ),
        ElevenLabsVoice(
            voiceId = "pFZP5JQG7iQjIQuC4Bku",
            name = "Lily",
            gender = VoiceGender.FEMALE,
            accent = "British / Velvety",
            category = "Narrative / Soothing",
            description = "Warm, sophisticated and calm British accent.",
            language = "English (UK)",
            languageGroup = "English",
            flagEmoji = "🇬🇧",
            isHindiOptimized = true,
            previewSampleText = "Good day. I am Lily, speaking with a refined and tranquil British tone."
        ),
        ElevenLabsVoice(
            voiceId = "ErXwobaYiN019PkySvjV",
            name = "Antoni",
            gender = VoiceGender.MALE,
            accent = "American / Deep",
            category = "Conversational / Tech",
            description = "Friendly, balanced and deep male voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Hey, Antoni here. Ready to assist you with tech, coding, and productivity."
        ),
        ElevenLabsVoice(
            voiceId = "TxGEqnHWrfWFTfGW9XjX",
            name = "Josh",
            gender = VoiceGender.MALE,
            accent = "American / Crisp",
            category = "Energetic / Modern",
            description = "Young, vibrant, tech-enthusiast male voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "What's up! Josh here, let's build something truly incredible together."
        ),
        ElevenLabsVoice(
            voiceId = "pNInz6obpgDQGcFmaJgB",
            name = "Adam",
            gender = VoiceGender.MALE,
            accent = "American / Deep",
            category = "Narrative / Authority",
            description = "Famous deep, commanding male voice, world renowned for audiobooks.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Greetings. I am Adam. Deep, authoritative, and crafted for perfection."
        ),
        ElevenLabsVoice(
            voiceId = "29vD33N1CtxCmqQRPOHJ",
            name = "Drew",
            gender = VoiceGender.MALE,
            accent = "American / Well-rounded",
            category = "News / Broadcast",
            description = "Well-rounded news anchor and radio host voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "Good evening, Drew here bringing you crisp audio and clear intelligence."
        ),
        ElevenLabsVoice(
            voiceId = "2EiwWnXFnvUEAnXmlld", // Clyde
            name = "Clyde",
            gender = VoiceGender.MALE,
            accent = "American / Gritty",
            category = "Character / Classic",
            description = "Vintage, gritty veteran voice with distinctive character.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "Listen up! Clyde reporting for duty. Ready whenever you give the word."
        ),
        ElevenLabsVoice(
            voiceId = "5Q0t7uMcjvnagumLfvZi",
            name = "Paul",
            gender = VoiceGender.MALE,
            accent = "American / Authoritative",
            category = "Journalism / Serious",
            description = "Ground reporter, authoritative and serious.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "This is Paul with an authoritative broadcast through MYRA AI."
        ),
        ElevenLabsVoice(
            voiceId = "CYw3kZ02Hs0563khs1Fj",
            name = "Dave",
            gender = VoiceGender.MALE,
            accent = "British Essex",
            category = "Conversational / Casual",
            description = "British Essex conversational, friendly male voice.",
            language = "English (UK)",
            languageGroup = "English",
            flagEmoji = "🇬🇧",
            isHindiOptimized = false,
            previewSampleText = "Alright mate! Dave here, ready to chat and get things sorted."
        ),
        ElevenLabsVoice(
            voiceId = "D38z5RcWu1voky8WS1ja",
            name = "Fin",
            gender = VoiceGender.MALE,
            accent = "Irish / Energetic",
            category = "Sailor / Narrative",
            description = "Charming Irish accent with storytelling flair.",
            language = "English (IE)",
            languageGroup = "English",
            flagEmoji = "🇮🇪",
            isHindiOptimized = false,
            previewSampleText = "Top of the morning! Fin here with the spirit of the Emerald Isle."
        ),
        ElevenLabsVoice(
            voiceId = "GBv7mTt0atIp3Br8iCZE",
            name = "Thomas",
            gender = VoiceGender.MALE,
            accent = "American / Calm",
            category = "Meditation / Story",
            description = "Calm, relaxing narrator voice for peaceful focus.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "Take a deep breath. I am Thomas, guiding you with calm reassurance."
        ),
        ElevenLabsVoice(
            voiceId = "IKne3meq5aSn9XLyUdCD",
            name = "Charlie",
            gender = VoiceGender.MALE,
            accent = "Australian",
            category = "Casual / Friendly",
            description = "Casual, friendly Australian accent.",
            language = "English (AU)",
            languageGroup = "English",
            flagEmoji = "🇦🇺",
            isHindiOptimized = false,
            previewSampleText = "G'day mate! Charlie here, ready to tackle the day with you."
        ),
        ElevenLabsVoice(
            voiceId = "JBFqnCBsd6RMkjVDRZzb",
            name = "George",
            gender = VoiceGender.MALE,
            accent = "British / Raspy",
            category = "Documentary / Warm",
            description = "Warm raspy British documentary narrator.",
            language = "English (UK)",
            languageGroup = "English",
            flagEmoji = "🇬🇧",
            isHindiOptimized = false,
            previewSampleText = "Welcome. George here, bringing warmth and depth to our conversation."
        ),
        ElevenLabsVoice(
            voiceId = "LcfcDJNigLAnTrGleoDa",
            name = "Emily",
            gender = VoiceGender.FEMALE,
            accent = "American / Empathetic",
            category = "Gentle / Counseling",
            description = "Empathetic, soothing female voice for mental wellness.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Hello friend. I am Emily, here to listen and assist with kindness."
        ),
        ElevenLabsVoice(
            voiceId = "N2lVS1w4EtoT3dr4eOWO",
            name = "Callum",
            gender = VoiceGender.MALE,
            accent = "Transatlantic / Intense",
            category = "Cinematic / Drama",
            description = "Intense, cinematic character voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "Prepare yourself. Callum here to deliver epic and dramatic responses."
        ),
        ElevenLabsVoice(
            voiceId = "ODq5zmih8GrVes37Dizd",
            name = "Patrick",
            gender = VoiceGender.MALE,
            accent = "American / Gaming",
            category = "Dynamic / Action",
            description = "Punchy, energetic gaming character voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "Let's roll! Patrick in the house, ready for high-intensity action!"
        ),
        ElevenLabsVoice(
            voiceId = "ThT5KcBeYPX3keUQqHPh",
            name = "Dorothy",
            gender = VoiceGender.FEMALE,
            accent = "British / Storyteller",
            category = "Children / Fantasy",
            description = "Classic British storybook reader with gentle cadence.",
            language = "English (UK)",
            languageGroup = "English",
            flagEmoji = "🇬🇧",
            isHindiOptimized = false,
            previewSampleText = "Once upon a time, Dorothy began a brand new adventure with MYRA."
        ),
        ElevenLabsVoice(
            voiceId = "VR6AewLTigWG4xSOukaG",
            name = "Arnold",
            gender = VoiceGender.MALE,
            accent = "American / Crisp",
            category = "Audiobook / Precision",
            description = "Crisp, precise and engaging narration voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "Arnold here, delivering crisp diction and accurate vocal delivery."
        ),
        ElevenLabsVoice(
            voiceId = "XB0fDUnXU5powFXDhCwa",
            name = "Charlotte",
            gender = VoiceGender.FEMALE,
            accent = "English-Swedish",
            category = "Seductive / Elegant",
            description = "Seductive, mysterious and elegant European-accented English.",
            language = "English (EU)",
            languageGroup = "English",
            flagEmoji = "🇸🇪",
            isHindiOptimized = false,
            previewSampleText = "Welcome to the future. I am Charlotte, whispering elegance in your ear."
        ),
        ElevenLabsVoice(
            voiceId = "Xb7hH8MSUJpSbSDYk0k2",
            name = "Alice",
            gender = VoiceGender.FEMALE,
            accent = "British / Confident",
            category = "Executive / News",
            description = "Confident, sharp British newsreader voice.",
            language = "English (UK)",
            languageGroup = "English",
            flagEmoji = "🇬🇧",
            isHindiOptimized = false,
            previewSampleText = "Good morning. Alice here with executive briefing and analysis."
        ),
        ElevenLabsVoice(
            voiceId = "XrExE9yKIg1WjnnlVkGX",
            name = "Matilda",
            gender = VoiceGender.FEMALE,
            accent = "American / Warm",
            category = "Audiobook / Friendly",
            description = "Warm, inviting audiobook narrator.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "Welcome home. I am Matilda, making every story come alive."
        ),
        ElevenLabsVoice(
            voiceId = "Yko7PKHZNXotIFUBG7I9",
            name = "Matthew",
            gender = VoiceGender.MALE,
            accent = "British / Calm",
            category = "Documentary / Science",
            description = "Calm British documentary voice for deep explanations.",
            language = "English (UK)",
            languageGroup = "English",
            flagEmoji = "🇬🇧",
            isHindiOptimized = false,
            previewSampleText = "Observe the patterns. Matthew here explaining the wonders of AI."
        ),
        ElevenLabsVoice(
            voiceId = "ZQe5CZPfIWxLuTXeTJnn",
            name = "James",
            gender = VoiceGender.MALE,
            accent = "Australian / Calm",
            category = "Journalism / Travel",
            description = "Calm Australian news presenter.",
            language = "English (AU)",
            languageGroup = "English",
            flagEmoji = "🇦🇺",
            isHindiOptimized = false,
            previewSampleText = "G'day, James reporting with updates from across the globe."
        ),
        ElevenLabsVoice(
            voiceId = "Zlb1dXrM653N07WRdFW3",
            name = "Joseph",
            gender = VoiceGender.MALE,
            accent = "British / Mature",
            category = "Casual / Senior",
            description = "Mature, friendly British casual voice.",
            language = "English (UK)",
            languageGroup = "English",
            flagEmoji = "🇬🇧",
            isHindiOptimized = false,
            previewSampleText = "Hello there. Joseph here, pleased to offer my wisdom whenever needed."
        ),
        ElevenLabsVoice(
            voiceId = "bVMeCyTHy58xNuA7WQ02",
            name = "Jeremy",
            gender = VoiceGender.MALE,
            accent = "American-Irish / Excited",
            category = "Podcaster / Host",
            description = "Excited, animated podcast host voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = false,
            previewSampleText = "Hey everyone! Jeremy here, fired up and ready for whatever's next!"
        ),
        ElevenLabsVoice(
            voiceId = "flq6f7yk4E4fJM5XTYuZ",
            name = "Michael",
            gender = VoiceGender.MALE,
            accent = "American / Natural",
            category = "Audiobook / Casual",
            description = "Natural, easy-going conversational male voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Hey! Michael here, talking just like your closest tech-savvy buddy."
        ),
        ElevenLabsVoice(
            voiceId = "onwK4e9ZLuTAKqWW03F9",
            name = "Daniel",
            gender = VoiceGender.MALE,
            accent = "British / Deep",
            category = "Executive / Authority",
            description = "Authoritative, deep BBC-style British voice.",
            language = "English (UK)",
            languageGroup = "English",
            flagEmoji = "🇬🇧",
            isHindiOptimized = false,
            previewSampleText = "Good day. Daniel speaking with British prestige and clarity."
        ),
        ElevenLabsVoice(
            voiceId = "pMsXgVXv3BLzUgSXRplE",
            name = "Serena",
            gender = VoiceGender.FEMALE,
            accent = "American / Pleasant",
            category = "Customer Care / Friendly",
            description = "Pleasant, welcoming female voice.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Welcome! I am Serena, delighted to be your personal assistant today."
        ),
        ElevenLabsVoice(
            voiceId = "nPczCjzI2devNBz1zQrb",
            name = "Brian",
            gender = VoiceGender.MALE,
            accent = "American / Deep",
            category = "Narration / Studio",
            description = "Deep, rich narration tone with high clarity.",
            language = "English (US)",
            languageGroup = "English",
            flagEmoji = "🇺🇸",
            isHindiOptimized = true,
            previewSampleText = "Hello. Brian here with studio-quality depth and vocal fidelity."
        )
    )

    val globalMultilingualVoices = listOf(
        ElevenLabsVoice(
            voiceId = "w4a2q0mQfVb7C9cde61G",
            name = "Mateo (Español)",
            gender = VoiceGender.MALE,
            accent = "Spanish / Multilingual",
            category = "Multilingual / Conversational",
            description = "Warm, melodic Spanish male voice. Fluent in Spanish, English & Multilingual V2.",
            language = "Spanish (Español)",
            languageGroup = "Global / All",
            flagEmoji = "🇪🇸",
            isHindiOptimized = false,
            previewSampleText = "¡Hola! Soy Mateo, tu asistente inteligente en español y modo multilingüe."
        ),
        ElevenLabsVoice(
            voiceId = "a7s8d9f0g1h2j3k4l5m6",
            name = "Sofia (Español)",
            gender = VoiceGender.FEMALE,
            accent = "Spanish / Multilingual",
            category = "Friendly / Warm",
            description = "Sweet and expressive Spanish female voice.",
            language = "Spanish (Español)",
            languageGroup = "Global / All",
            flagEmoji = "🇪🇸",
            isHindiOptimized = false,
            previewSampleText = "¡Hola querido amigo! Soy Sofía, encantada de hablar contigo hoy."
        ),
        ElevenLabsVoice(
            voiceId = "1bK1s9mG2Yv1gL5pT7g1",
            name = "Camille (Français)",
            gender = VoiceGender.FEMALE,
            accent = "French / Multilingual",
            category = "Elegant / Parisian",
            description = "Sophisticated Parisian French female voice.",
            language = "French (Français)",
            languageGroup = "Global / All",
            flagEmoji = "🇫🇷",
            isHindiOptimized = false,
            previewSampleText = "Bonjour! Je m'appelle Camille, votre assistante élégante en français."
        ),
        ElevenLabsVoice(
            voiceId = "f4g5h6j7k8l9m0n1p2q3",
            name = "Jean (Français)",
            gender = VoiceGender.MALE,
            accent = "French / Multilingual",
            category = "Philosophical / Deep",
            description = "Deep, thoughtful French male voice.",
            language = "French (Français)",
            languageGroup = "Global / All",
            flagEmoji = "🇫🇷",
            isHindiOptimized = false,
            previewSampleText = "Bonjour. C'est Jean, prêt à répondre à toutes vos questions."
        ),
        ElevenLabsVoice(
            voiceId = "z1x2c3v4b5n6m7a8s9d0",
            name = "Hans (Deutsch)",
            gender = VoiceGender.MALE,
            accent = "German / Multilingual",
            category = "Precise / Authoritative",
            description = "Precise, clear German male voice.",
            language = "German (Deutsch)",
            languageGroup = "Global / All",
            flagEmoji = "🇩🇪",
            isHindiOptimized = false,
            previewSampleText = "Guten Tag! Ich bin Hans, Ihre präzise deutsche KI-Stimme."
        ),
        ElevenLabsVoice(
            voiceId = "k2l3m4n5o6p7q8r9s0t1",
            name = "Greta (Deutsch)",
            gender = VoiceGender.FEMALE,
            accent = "German / Multilingual",
            category = "Friendly / Modern",
            description = "Modern and friendly German female voice.",
            language = "German (Deutsch)",
            languageGroup = "Global / All",
            flagEmoji = "🇩🇪",
            isHindiOptimized = false,
            previewSampleText = "Hallo! Ich bin Greta, bereit für Ihre täglichen Aufgaben."
        ),
        ElevenLabsVoice(
            voiceId = "m1n2b3v4c5x6z7a8s9d0",
            name = "Marco (Italiano)",
            gender = VoiceGender.MALE,
            accent = "Italian / Multilingual",
            category = "Expressive / Lively",
            description = "Expressive and passionate Italian male voice.",
            language = "Italian (Italiano)",
            languageGroup = "Global / All",
            flagEmoji = "🇮🇹",
            isHindiOptimized = false,
            previewSampleText = "Ciao! Sono Marco, felice di aiutarti con l'intelligenza di MYRA."
        ),
        ElevenLabsVoice(
            voiceId = "q1w2e3r4t5y6u7i8o9p0",
            name = "Francesca (Italiano)",
            gender = VoiceGender.FEMALE,
            accent = "Italian / Multilingual",
            category = "Melodious / Sweet",
            description = "Sweet, melodious Italian female voice.",
            language = "Italian (Italiano)",
            languageGroup = "Global / All",
            flagEmoji = "🇮🇹",
            isHindiOptimized = false,
            previewSampleText = "Buongiorno! Sono Francesca, la tua voce italiana preferita."
        ),
        ElevenLabsVoice(
            voiceId = "j9k8l7m6n5o4p3q2r1s0",
            name = "Hiroshi (日本語)",
            gender = VoiceGender.MALE,
            accent = "Japanese / Multilingual",
            category = "Respectful / Modern",
            description = "Calm, polite Japanese male voice.",
            language = "Japanese (日本語)",
            languageGroup = "Global / All",
            flagEmoji = "🇯🇵",
            isHindiOptimized = false,
            previewSampleText = "こんにちは！ヒロシです。最高のアシスタント体験をお届けします。"
        ),
        ElevenLabsVoice(
            voiceId = "y1u2i3o4p5a6s7d8f9g0",
            name = "Sakura (日本語)",
            gender = VoiceGender.FEMALE,
            accent = "Japanese / Multilingual",
            category = "Anime / Gentle",
            description = "Gentle, polite anime-style Japanese female voice.",
            language = "Japanese (日本語)",
            languageGroup = "Global / All",
            flagEmoji = "🇯🇵",
            isHindiOptimized = false,
            previewSampleText = "初めまして！サクラです。今日も一緒に頑張りましょうね！"
        ),
        ElevenLabsVoice(
            voiceId = "h1j2k3l4m5n6b7v8c9x0",
            name = "Min-ho (한국어)",
            gender = VoiceGender.MALE,
            accent = "Korean / Multilingual",
            category = "Youthful / Clean",
            description = "Clean, youthful Korean male voice.",
            language = "Korean (한국어)",
            languageGroup = "Global / All",
            flagEmoji = "🇰🇷",
            isHindiOptimized = false,
            previewSampleText = "안녕하세요! 민호입니다. 무엇이든 물어보세요."
        ),
        ElevenLabsVoice(
            voiceId = "z9x8c7v6b5n4m3a2s1d0",
            name = "Ji-eun (한국어)",
            gender = VoiceGender.FEMALE,
            accent = "Korean / Multilingual",
            category = "Soft / Melodic",
            description = "Sweet and friendly Korean female voice.",
            language = "Korean (한국어)",
            languageGroup = "Global / All",
            flagEmoji = "🇰🇷",
            isHindiOptimized = false,
            previewSampleText = "안녕하세요! 지은이에요. 오늘 하루도 파이팅이에요!"
        ),
        ElevenLabsVoice(
            voiceId = "t1y2u3i4o5p6a7s8d9f0",
            name = "Tariq (العربية)",
            gender = VoiceGender.MALE,
            accent = "Arabic / Multilingual",
            category = "Deep / Poetic",
            description = "Rich, deep Arabic male voice.",
            language = "Arabic (العربية)",
            languageGroup = "Global / All",
            flagEmoji = "🇦🇪",
            isHindiOptimized = false,
            previewSampleText = "مرحباً بك. أنا طارق، صوتك الذكي والمميز في مايرا."
        ),
        ElevenLabsVoice(
            voiceId = "l1a2y3l4a5r6a7b8i9c0",
            name = "Layla (العربية)",
            gender = VoiceGender.FEMALE,
            accent = "Arabic / Multilingual",
            category = "Warm / Eloquent",
            description = "Warm and eloquent Arabic female voice.",
            language = "Arabic (العربية)",
            languageGroup = "Global / All",
            flagEmoji = "🇦🇪",
            isHindiOptimized = false,
            previewSampleText = "أهلاً وسهلاً! أنا ليلى، يسعدني مساعدتك في كل وقت."
        ),
        ElevenLabsVoice(
            voiceId = "c1a2r3l4o5s6b7r8a9z0",
            name = "Carlos (Português)",
            gender = VoiceGender.MALE,
            accent = "Portuguese / Multilingual",
            category = "Energetic / Warm",
            description = "Warm and friendly Brazilian Portuguese male voice.",
            language = "Portuguese (Português)",
            languageGroup = "Global / All",
            flagEmoji = "🇧🇷",
            isHindiOptimized = false,
            previewSampleText = "Olá! Eu sou Carlos, seu assistente com a energia do Brasil."
        ),
        ElevenLabsVoice(
            voiceId = "l1u2c3i4a5n6a7b8r9a0",
            name = "Luciana (Português)",
            gender = VoiceGender.FEMALE,
            accent = "Portuguese / Multilingual",
            category = "Melodic / Friendly",
            description = "Melodic Brazilian Portuguese female voice.",
            language = "Portuguese (Português)",
            languageGroup = "Global / All",
            flagEmoji = "🇧🇷",
            isHindiOptimized = false,
            previewSampleText = "Oi tudo bem? Sou a Luciana, pronta para te ajudar com tudo."
        ),
        ElevenLabsVoice(
            voiceId = "m1i2k3h4a5i6l7r8u9s0",
            name = "Mikhail (Русский)",
            gender = VoiceGender.MALE,
            accent = "Russian / Multilingual",
            category = "Rich / Commanding",
            description = "Rich, clear Russian male voice.",
            language = "Russian (Русский)",
            languageGroup = "Global / All",
            flagEmoji = "🇷🇺",
            isHindiOptimized = false,
            previewSampleText = "Привет! Я Михаил, ваш персональный голосовой помощник."
        ),
        ElevenLabsVoice(
            voiceId = "a1n2a3s4t5a6s7i8a9r0",
            name = "Anastasia (Русский)",
            gender = VoiceGender.FEMALE,
            accent = "Russian / Multilingual",
            category = "Soft / Elegant",
            description = "Gentle and articulate Russian female voice.",
            language = "Russian (Русский)",
            languageGroup = "Global / All",
            flagEmoji = "🇷🇺",
            isHindiOptimized = false,
            previewSampleText = "Здравствуйте! Меня зовут Анастасия, рада помочь вам."
        )
    )

    fun getAllVoices(): List<ElevenLabsVoice> {
        return hindiVoices + chatGptVoices + geminiVoices + englishVoices + globalMultilingualVoices
    }

    /**
     * Fetch live voices from ElevenLabs API (account voices + library)
     */
    suspend fun fetchLiveVoices(apiKey: String): List<ElevenLabsVoice> = withContext(Dispatchers.IO) {
        val cleanKey = apiKey.trim()
        if (cleanKey.isBlank()) return@withContext emptyList()

        try {
            val url = "https://api.elevenlabs.io/v1/voices"
            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url(url)
                .addHeader("xi-api-key", cleanKey)
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext emptyList()

            val body = response.body?.string() ?: return@withContext emptyList()
            val json = JSONObject(body)
            val voicesArray = json.optJSONArray("voices") ?: return@withContext emptyList()

            val list = mutableListOf<ElevenLabsVoice>()
            for (i in 0 until voicesArray.length()) {
                val item = voicesArray.getJSONObject(i)
                val voiceId = item.getString("voice_id")
                val name = item.getString("name")
                val category = item.optString("category", "Custom / Live")
                val labels = item.optJSONObject("labels")

                val accent = labels?.optString("accent", "Natural") ?: "Natural"
                val genderStr = labels?.optString("gender", "female") ?: "female"
                val gender = if (genderStr.equals("male", ignoreCase = true)) VoiceGender.MALE else VoiceGender.FEMALE
                val languageStr = labels?.optString("language", "English") ?: "English"
                val description = item.optString("description", "Live ElevenLabs Account Voice")

                val isHindi = languageStr.contains("hindi", ignoreCase = true) ||
                        name.contains("hindi", ignoreCase = true) ||
                        accent.contains("indian", ignoreCase = true)

                val group = when {
                    isHindi -> "Hindi"
                    languageStr.contains("english", ignoreCase = true) -> "English"
                    else -> "Global / All"
                }

                val flag = when {
                    isHindi -> "🇮🇳"
                    languageStr.contains("spanish", ignoreCase = true) -> "🇪🇸"
                    languageStr.contains("french", ignoreCase = true) -> "🇫🇷"
                    languageStr.contains("german", ignoreCase = true) -> "🇩🇪"
                    languageStr.contains("japanese", ignoreCase = true) -> "🇯🇵"
                    languageStr.contains("korean", ignoreCase = true) -> "🇰🇷"
                    languageStr.contains("italian", ignoreCase = true) -> "🇮🇹"
                    languageStr.contains("arabic", ignoreCase = true) -> "🇦🇪"
                    else -> "🌐"
                }

                list.add(
                    ElevenLabsVoice(
                        voiceId = voiceId,
                        name = name,
                        gender = gender,
                        accent = accent,
                        category = category,
                        description = description,
                        language = languageStr,
                        languageGroup = group,
                        flagEmoji = flag,
                        isHindiOptimized = isHindi,
                        previewSampleText = "Hello! I am $name, an ElevenLabs custom voice in MYRA."
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }
}
