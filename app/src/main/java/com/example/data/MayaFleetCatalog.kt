package com.example.data

import com.example.model.MayaFeatureItem

object MayaFleetCatalog {

    fun getAll32Features(): List<MayaFeatureItem> = listOf(
        MayaFeatureItem(
            id = "maya_1_pc_control",
            number = 1,
            title = "Advanced PC Control",
            hindiTitle = "एडवांस्ड पीसी कंट्रोल (MAYA-Level)",
            statusTag = "🟡 Upgrade",
            category = "PC & System",
            iconName = "Laptop",
            description = "PowerShell/Terminal commands, App open/close, Window minimize/maximize, Desktop toggle, Mouse/keyboard, Hotkeys, Voice typing, Shutdown/Restart/Sleep, Brightness & Volume control, Temp clean, Screen padhna aur error message samjhana.",
            capabilities = listOf(
                "App kholna aur band karna",
                "Window minimize/maximize & Show Desktop",
                "Mouse & keyboard simulation & Hotkeys",
                "Voice typing direct PC cursor par",
                "PowerShell & Terminal raw command execution",
                "PC Shutdown, Restart aur Sleep modes",
                "Brightness aur Volume slider voice control",
                "Temporary & cache files clean karna",
                "PC screen padhna aur error dialogue samjhana"
            ),
            voiceCommands = listOf(
                "MJ PC me Chrome minimize karo",
                "MJ PC par PowerShell kholo",
                "MJ PC restart karo",
                "MJ PC screen padh ke error samjhao",
                "MJ PC ka temp folder clean karo"
            ),
            actionType = "PC_CONTROL"
        ),
        MayaFeatureItem(
            id = "maya_2_file_manager",
            number = 2,
            title = "Advanced File Manager",
            hindiTitle = "एडवांस्ड फाइल मैनेजर",
            statusTag = "🟢 Existing + 🟡 Upgrade",
            category = "PC & System",
            iconName = "Folder",
            description = "Pure PC/Device me deep fuzzy filename search, folder structure listing, batch rename, zip/unzip, extension filter, safe delete aur file content ko voice me bolkar sunana.",
            capabilities = listOf(
                "Deep PC & Phone file search",
                "Fuzzy filename matching (adhura naam dhoondhna)",
                "Folder hierarchy visualization",
                "Batch rename (100+ files ek sath format karna)",
                "ZIP archive banana aur unzip extract karna",
                "Extension sort (.pdf, .docx, .png, .mp4)",
                "Safe delete with recycle bin recovery",
                "File text content voice me padh ke sunana"
            ),
            voiceCommands = listOf(
                "MJ mere PC me invoice.pdf dhoondo",
                "MJ sabhi photos ko date format me batch rename karo",
                "MJ project folder ko zip bana do",
                "MJ is document ka pehla paragraph padho"
            ),
            actionType = "FILE_MANAGER"
        ),
        MayaFeatureItem(
            id = "maya_3_clipboard",
            number = 3,
            title = "Clipboard History Guardian",
            hindiTitle = "क्लिपबोर्ड हिस्ट्री गार्डियन",
            statusTag = "🟡 Upgrade",
            category = "PC & System",
            iconName = "ContentPaste",
            description = "Clipboard monitor, purani copied text/links/images ki full timeline history, search, one-tap voice recall aur security clipboard auto-clear.",
            capabilities = listOf(
                "Realtime clipboard listening",
                "Past 50+ copied items timeline history",
                "Pin favorite clipboard snippets",
                "Sensitive passwords clipboard clear after 60s",
                "Voice se copied text wapas bolna"
            ),
            voiceCommands = listOf(
                "MJ clipboard padh kar sunao",
                "MJ pichla copied link search karo",
                "MJ clipboard history clear kar do"
            ),
            actionType = "CLIPBOARD"
        ),
        MayaFeatureItem(
            id = "maya_4_excel_agent",
            number = 4,
            title = "Excel & Spreadsheet Voice Control",
            hindiTitle = "एक्सेल वॉइस एजेंट",
            statusTag = "🔴 New Major Feature",
            category = "Intelligence & Coding",
            iconName = "TableChart",
            description = "Excel/Sheets ko voice se operate karein: Cells change karna, nayi row/column add karna, SUM/AVERAGE/VLOOKUP formula lagana, sheet analysis aur visual charts create karna.",
            capabilities = listOf(
                "Excel / CSV open aur create karna",
                "Voice se cell value update (e.g. 'A1 me Total likho')",
                "Nayi rows aur columns dynamic insert karna",
                "Formula execution (=SUM, =AVERAGE, =IF, =COUNT)",
                "Sheet data se automatic KPI dashboard generate karna",
                "Pivot summary aur high-level insight voice me batana"
            ),
            voiceCommands = listOf(
                "MJ Excel kholo aur sales sheet banao",
                "MJ cell B2 me 4500 likho aur C2 me formula lagao",
                "MJ is sheet me Total row add karke SUM formula lagao",
                "MJ spreadsheet ka pie chart dashboard dikhao"
            ),
            actionType = "EXCEL_AGENT"
        ),
        MayaFeatureItem(
            id = "maya_5_screen_vision",
            number = 5,
            title = "Screen Vision & Visual Inspector",
            hindiTitle = "स्क्रीन विजन और विजुअल इंस्पेक्टर",
            statusTag = "🟢 Foundation + 🟡 MAYA Upgrade",
            category = "Vision & Media",
            iconName = "Visibility",
            description = "Screen par kya chal raha hai turant dekhna: Error popup padhna, current active app identify karna, page article summary, screenshot OCR aur voice briefing.",
            capabilities = listOf(
                "'Screen par kya likha hai?' instant voice OCR analysis",
                "System error dialogue padh kar solution explain karna",
                "Active running app identify karna",
                "Long web article ya PDF screen ko summarize karna",
                "Instant screenshot capture aur visual bounding box analysis",
                "Screen context ke hisaab se next step suggest karna"
            ),
            voiceCommands = listOf(
                "MJ screen par kya likha hai?",
                "MJ ye error dialog padho aur solution batao",
                "MJ is screen ka summary bana kar batao",
                "MJ screen ka screenshot lekar inspect karo"
            ),
            actionType = "SCREEN_VISION"
        ),
        MayaFeatureItem(
            id = "maya_6_whatsapp_advanced",
            number = 6,
            title = "Advanced WhatsApp Fleet",
            hindiTitle = "एडवांस्ड व्हाट्सएप फ्लीट",
            statusTag = "🟢 Existing + 🟡 MAYA Upgrade",
            category = "Communication & Finance",
            iconName = "Chat",
            description = "Unread messages voice summary, poori conversation context padhna, documents/photos attach karna, WhatsApp audio/video call dial karna aur screenshot bhej dena.",
            capabilities = listOf(
                "Unread chats ki quick voice summary sunana",
                "Puri chat conversation flow padhna",
                "File aur folder directly WhatsApp par attach karna",
                "WhatsApp voice aur video call lagana",
                "Current screen ka screenshot lekar WhatsApp contact ko bhejna",
                "Voice bol kar natural conversational reply type karna"
            ),
            voiceCommands = listOf(
                "MJ WhatsApp ke unread chats padho",
                "MJ Rahul ko WhatsApp par bolo ki file attach kar raha hoon",
                "MJ screen ka screenshot lekar Rohan ko WhatsApp karo",
                "MJ Mummy ko WhatsApp voice call lagao"
            ),
            actionType = "WHATSAPP_ADVANCED"
        ),
        MayaFeatureItem(
            id = "maya_7_advanced_email",
            number = 7,
            title = "Advanced Email Intelligence",
            hindiTitle = "एडवांस्ड ईमेल इंटेलिजेंस",
            statusTag = "🟢 Existing + 🟡 Upgrade",
            category = "Communication & Finance",
            iconName = "Email",
            description = "Gmail aur Outlook inboxes ka unread summary, voice email compose, thread context-aware reply, spam clean aur priority emails ki bullet summary.",
            capabilities = listOf(
                "Unread priority emails ka voice executive briefing",
                "Awaaz se email subject aur body draft karke send karna",
                "Pehle se chal rahi email thread ka accurate context reply",
                "Newsletters aur promotional spam ek click me clean karna",
                "Important client emails ka automatic highlight alert"
            ),
            voiceCommands = listOf(
                "MJ aaj ke unread emails ki summary batao",
                "MJ boss ko email bhejo leave application ke liye",
                "MJ is email thread ka reply draft karo",
                "MJ inbox se promotional spam saaf karo"
            ),
            actionType = "ADVANCED_EMAIL"
        ),
        MayaFeatureItem(
            id = "maya_8_expense_upi",
            number = 8,
            title = "Expense, UPI & Bank Email Intelligence",
            hindiTitle = "खर्च, यूपीआई और बैंक ईमेल इंटेलिजेंस",
            statusTag = "🔴 New Major Feature",
            category = "Communication & Finance",
            iconName = "AccountBalanceWallet",
            description = "Bank transaction SMS aur emails scan karke total spending, UPI transfers, card debits, order receipts, top merchants aur category-wise monthly report banana.",
            capabilities = listOf(
                "Bank transaction SMS & emails auto-parse karna",
                "UPI transfers (GPay, PhonePe, Paytm) track karna",
                "Credit/Debit card swipes aur order receipts detect karna",
                "Total daily, weekly aur monthly spending calculate karna",
                "Top merchants (Swiggy, Amazon, Uber) breakdown",
                "Category-wise pie chart (Food, Travel, Bills, Shopping)",
                "Itemized kharch export in CSV"
            ),
            voiceCommands = listOf(
                "MJ mera is mahine ka total spending kitna hai?",
                "MJ UPI transactions ka hisaab dikhao",
                "MJ Swiggy aur Zomato par kitna kharch hua?",
                "MJ bank emails se latest debits scan karo"
            ),
            actionType = "EXPENSE_UPI"
        ),
        MayaFeatureItem(
            id = "maya_9_android_pc_unified",
            number = 9,
            title = "Unified Android-PC Bridge",
            hindiTitle = "यूनिफाइड एंड्रॉइड-पीसी ब्रिज",
            statusTag = "🟡 Existing Foundation -> Big Upgrade",
            category = "PC & System",
            iconName = "Devices",
            description = "QR pair karke PC aur Android phone ko single entity banana: PC se phone calls/SMS/notifications control, aur Phone se PC par commands chalana.",
            capabilities = listOf(
                "Instant encrypted QR pairing",
                "Phone se PC par software kholna aur band karna",
                "PC par baith kar phone notifications padhna aur reply karna",
                "Phone par incoming calls PC screen par answer karna",
                "Phone settings toggle (Wi-Fi, Bluetooth, Torch) PC se karna",
                "Cross-device clipboard aur universal file push"
            ),
            voiceCommands = listOf(
                "MJ PC aur phone pair karo",
                "MJ PC se phone ka hotspot on kar do",
                "MJ phone ki aayi hui notification PC par padho"
            ),
            actionType = "ANDROID_PC_BRIDGE"
        ),
        MayaFeatureItem(
            id = "maya_10_browser_agent",
            number = 10,
            title = "Autonomous Browser Agent",
            hindiTitle = "ऑटोनॉमस ब्राउज़र एजेंट",
            statusTag = "🟢 Existing Tech + 🟡 Smart Upgrade",
            category = "Intelligence & Coding",
            iconName = "Language",
            description = "Chrome aur web browsers ko autonomously operate karna: Web search, image search, full page fetch, multi-step website tasks, form submit, button click aur persistent session handle karna.",
            capabilities = listOf(
                "Google/DuckDuckGo deep web search execution",
                "Webpage ka raw text aur clean markdown fetch karna",
                "Autonomous multi-step website automation",
                "Web forms me inputs bharna aur buttons click karna",
                "Dropdown select karna aur multi-tabs manage karna",
                "Persistent cookies aur session support"
            ),
            voiceCommands = listOf(
                "MJ Chrome kholo aur IRCTC ticket availability check karo",
                "MJ Wikipedia page fetch karke summary sunao",
                "MJ is form ko auto-fill karo",
                "MJ browser me nayi tab khol kar research karo"
            ),
            actionType = "BROWSER_AGENT"
        ),
        MayaFeatureItem(
            id = "maya_11_document_agent",
            number = 11,
            title = "Document Master Agent",
            hindiTitle = "डॉक्यूमेंट मास्टर एजेंट",
            statusTag = "🔴 New Major Module",
            category = "Intelligence & Coding",
            iconName = "Description",
            description = "Word (DOCX), PDF, Spreadsheets aur professional resumes create karna, pure folder ko index karna, multiple documents me sawal pooch kar jawab dena.",
            capabilities = listOf(
                "Professional Word (.docx) document create karna",
                "PDF format export aur print-ready layout",
                "ATS-friendly modern Resume builder",
                "Folder ke sabhi documents ka semantic index banana",
                "Multi-document Q&A (10 alag PDF files se answer dhundhna)",
                "Background agent queue for long document rendering"
            ),
            voiceCommands = listOf(
                "MJ ek formal project proposal document banao",
                "MJ mera resume banao Android Developer profile ke liye",
                "MJ in 5 PDF files se key points summarize karo",
                "MJ folder ke sabhi documents index karo"
            ),
            actionType = "DOCUMENT_AGENT"
        ),
        MayaFeatureItem(
            id = "maya_12_deep_research",
            number = 12,
            title = "Deep Autonomous Research Agent",
            hindiTitle = "डीप ऑटोनॉमस रिसर्च एजेंट",
            statusTag = "🟢 Existing + 🟡 Major Upgrade",
            category = "Intelligence & Coding",
            iconName = "Biotech",
            description = "Topic milte hi autonomous plan banata hai: 15+ credible web sources visit karna, facts cross-check karna, citations ke saath detailed comprehensive report generate karna.",
            capabilities = listOf(
                "Multi-stage iterative research planner",
                "Tavily, Perplexity & Google search synthesis",
                "Fact checking and source contradiction detection",
                "Academic styled citation report format",
                "Executive bullet briefing aur downloadable full report"
            ),
            voiceCommands = listOf(
                "MJ Quantum Computing 2026 par deep research shuru karo",
                "MJ electric vehicle battery technologies par cited report banao",
                "MJ research plan execute karo aur PDF export do"
            ),
            actionType = "DEEP_RESEARCH"
        ),
        MayaFeatureItem(
            id = "maya_13_whiteboard_study",
            number = 13,
            title = "Whiteboard & Study Mode",
            hindiTitle = "व्हाइटबोर्ड और स्टडी मोड",
            statusTag = "🔴 New Feature",
            category = "Vision & Media",
            iconName = "Draw",
            description = "Interactive drawing whiteboard, mathematical equations solve karna, visual flowcharts draw karna, concepts ko diagram ke sath samjhana aur PNG export karna.",
            capabilities = listOf(
                "Touch & voice reactive canvas whiteboard",
                "Complex flowcharts aur architecture diagrams",
                "Math formulas aur equations visual step-by-step solver",
                "Drawing ke sath real-time voice explanation",
                "High-resolution PNG/SVG export to gallery"
            ),
            voiceCommands = listOf(
                "MJ whiteboard kholo aur binary tree draw karo",
                "MJ Pythagoras theorem diagram bana kar samjhao",
                "MJ microservices architecture ka flowchart draw karo",
                "MJ is whiteboard ko PNG me save karo"
            ),
            actionType = "WHITEBOARD_STUDY"
        ),
        MayaFeatureItem(
            id = "maya_14_screen_recording",
            number = 14,
            title = "Screen Recording Suite",
            hindiTitle = "स्क्रीन रिकॉर्डिंग सूट",
            statusTag = "🔴 New Feature",
            category = "Vision & Media",
            iconName = "Videocam",
            description = "Voice commands se direct screen recording start, pause, resume aur stop karna. High-FPS video capture, microphone audio recording aur status indicator.",
            capabilities = listOf(
                "Instant screen recording start with countdown",
                "Pause aur resume capabilities",
                "System audio + mic voice narration recording",
                "Recording active status bar & duration ticker",
                "Auto save to gallery with instant share"
            ),
            voiceCommands = listOf(
                "MJ screen recording shuru karo",
                "MJ recording pause karo",
                "MJ recording resume karo",
                "MJ screen recording stop kar do"
            ),
            actionType = "SCREEN_RECORDING"
        ),
        MayaFeatureItem(
            id = "maya_15_live_commentary",
            number = 15,
            title = "Live Commentary Engine",
            hindiTitle = "लाइव कमेंट्री इंजन (Creator Special)",
            statusTag = "🔴 New Feature",
            category = "Vision & Media",
            iconName = "MicExternalOn",
            description = "Screen par jo gameplay, coding session ya demo chal raha hai, use continuously inspect karke real-time energetic voice commentary bolna.",
            capabilities = listOf(
                "Realtime visual sampling of gameplay & apps",
                "Dynamic energetic tone selection (Sports, Tech, Funny)",
                "Action recognition (kills, errors, milestone completions)",
                "Zero-lag low latency streaming voice generation"
            ),
            voiceCommands = listOf(
                "MJ live commentary shuru karo",
                "MJ mere gameplay par commentary bolo",
                "MJ coding session par funny commentary do",
                "MJ commentary band karo"
            ),
            actionType = "LIVE_COMMENTARY"
        ),
        MayaFeatureItem(
            id = "maya_16_youtube_creator",
            number = 16,
            title = "YouTube Creator Suite & Analytics",
            hindiTitle = "यूट्यूब क्रिएटर सूट और एनालिटिक्स",
            statusTag = "🔴 New Major Module",
            category = "Communication & Finance",
            iconName = "PlayCircle",
            description = "YouTube search, playback, video upload planner (Title, Description, Viral Tags generator), channel subscriber counter, views tracker, growth curve aur comments sentiment.",
            capabilities = listOf(
                "YouTube search aur hands-free video playback",
                "Viral SEO Title, High-CTR Description aur Tags generation",
                "Channel live subscriber counter aur view analytics",
                "Top audience retention & growth recommendations",
                "Latest comments sentiment analysis (Positive, Doubts, Spam)"
            ),
            voiceCommands = listOf(
                "MJ mere YouTube video ke liye viral title aur tags suggest karo",
                "MJ channel ke subscribers aur views growth batao",
                "MJ comments ka sentiment analyze karo",
                "MJ YouTube par lo-fi hip hop stream chalao"
            ),
            actionType = "YOUTUBE_CREATOR"
        ),
        MayaFeatureItem(
            id = "maya_17_advanced_music",
            number = 17,
            title = "Advanced Music & Taste Intelligence",
            hindiTitle = "एडवांस्ड म्यूजिक इंटेलिजेंस",
            statusTag = "🟢 Existing + 🟡 Upgrade",
            category = "Vision & Media",
            iconName = "MusicNote",
            description = "Play, pause, skip, current playing track metadata, personalized favorites playlist, user musical taste memory aur mood-based recommendation engine.",
            capabilities = listOf(
                "Cross-platform music playback (Spotify, YT Music, Local)",
                "Current playing track details & lyrics sync",
                "Favorites list building aur smart shuffle",
                "User music taste tracking (Lofi, EDM, Bollywood, Classical)",
                "Listening history timeline & recommendations"
            ),
            voiceCommands = listOf(
                "MJ mera favorite gaana play karo",
                "MJ current track kaun sa hai?",
                "MJ agla gaana lagao",
                "MJ mere mood ke hisaab se calm playlist chalao"
            ),
            actionType = "ADVANCED_MUSIC"
        ),
        MayaFeatureItem(
            id = "maya_18_camera_face_recognition",
            number = 18,
            title = "Advanced Camera & Face Recognition",
            hindiTitle = "एडवांस्ड कैमरा और फेस रिकॉग्निशन",
            statusTag = "🟢 Camera + 🟡 MAYA Upgrade",
            category = "Vision & Media",
            iconName = "Face",
            description = "Camera view se scene describe karna, high-res photos click karna, pehle se introduced doston aur family members ke faces ko pehchan kar greet karna.",
            capabilities = listOf(
                "Live camera feed scene description in natural Hindi/English",
                "Hands-free voice photo capture",
                "Known faces memory (e.g. 'Ye Rohit hai')",
                "Face recognize karke customized greeting bolna",
                "Object count aur ambient lighting assessment"
            ),
            voiceCommands = listOf(
                "MJ camera se scene describe karo",
                "MJ photo khicho",
                "MJ samne kaun khada hai pehchano",
                "MJ is dost ka face 'Aman' naam se save karo"
            ),
            actionType = "CAMERA_FACE"
        ),
        MayaFeatureItem(
            id = "maya_19_stock_finance",
            number = 19,
            title = "Stock Market & Finance Agent",
            hindiTitle = "स्टॉक मार्केट और फाइनेंस एजेंट",
            statusTag = "🔴 New Feature",
            category = "Communication & Finance",
            iconName = "TrendingUp",
            description = "Live stock quotes (Nifty 50, Sensex, NASDAQ, Crypto), ticker search, 52-week high/low, intraday price fluctuations aur market trend analysis.",
            capabilities = listOf(
                "Live Indian (NSE/BSE) aur Global stock quotes",
                "Crypto prices (Bitcoin, Ethereum, Solana) tracker",
                "Stock ticker search with price percentage change",
                "52-week high, low aur volume stats",
                "Market sentiment summary voice briefing"
            ),
            voiceCommands = listOf(
                "MJ Nifty aur Sensex ka live status kya hai?",
                "MJ Reliance aur Tata Motors ka stock price batao",
                "MJ Bitcoin ka current rate kya chal raha hai?",
                "MJ stock market ka brief update do"
            ),
            actionType = "STOCK_FINANCE"
        ),
        MayaFeatureItem(
            id = "maya_20_advanced_weather",
            number = 20,
            title = "Advanced Weather & Climate Radar",
            hindiTitle = "एडवांस्ड वेदर और क्लाइमेट राडार",
            statusTag = "🟡 Upgrade",
            category = "PC & System",
            iconName = "WbSunny",
            description = "GPS location based live temperature, humidity, wind speed, AQI pollution score, hourly rainfall radar aur 7-day extended weather forecast.",
            capabilities = listOf(
                "Instant local temperature aur feels-like rating",
                "Air Quality Index (AQI) aur health advisory",
                "Precipitation & rainfall alert percentage",
                "Sunrise, sunset aur UV index",
                "7-day extended forecast with high/low curves"
            ),
            voiceCommands = listOf(
                "MJ aaj ka mausam kaisa hai?",
                "MJ kya aaj baarish hogi?",
                "MJ mere shehar ka AQI pollution level batao",
                "MJ kal ka weather forecast sunao"
            ),
            actionType = "ADVANCED_WEATHER"
        ),
        MayaFeatureItem(
            id = "maya_21_ai_coding_agent",
            number = 21,
            title = "Autonomous AI Coding Agent",
            hindiTitle = "ऑटोनॉमस एआई कोडिंग एजेंट (सबसे महत्वपूर्ण)",
            statusTag = "🔴 New Major Module",
            category = "Intelligence & Coding",
            iconName = "Code",
            description = "Voice se bolo -> Plan banata hai -> Source files banata hai -> Code likhta hai -> Run karta hai -> Compiler error padhta hai -> Khud automatically fix karta hai!",
            capabilities = listOf(
                "Voice-to-Code architecture planner",
                "Multi-file generation (Kotlin, Python, JS/TS, HTML/CSS)",
                "Local project tree modification & syntax check",
                "Error output stacktrace parsing",
                "Self-healing iterative bug repair loop",
                "Terminal run simulator with live execution logs"
            ),
            voiceCommands = listOf(
                "MJ WhatsApp automation ka feature bana do",
                "MJ ek Python web scraper script likho aur test karo",
                "MJ code me error aa raha hai, use fix karo",
                "MJ project ka architecture plan ready karo"
            ),
            actionType = "AI_CODING_AGENT"
        ),
        MayaFeatureItem(
            id = "maya_22_parallel_agents",
            number = 22,
            title = "Parallel Multi-Agent Autonomous Fleet",
            hindiTitle = "पैरेलल मल्टी-एजेंट ऑटोनॉमस फ्लीट",
            statusTag = "🟡 Mission -> 🟢 Multi-Agent Upgrade",
            category = "Autonomous Fleet",
            iconName = "Hub",
            description = "Ek agent coding kare, doosra deep research kare, teesra document draft kare, chautha browser task kare—sab parallel run hote hain jabki main MJ aapse baat karta rahe!",
            capabilities = listOf(
                "Parallel background task execution threads",
                "Sub-Agent 1: Coding & Architecture Specialist",
                "Sub-Agent 2: Deep Web Research & Fact Cross-checker",
                "Sub-Agent 3: Document, Resume & PDF Generator",
                "Sub-Agent 4: Web Browser Scraping & Form Worker",
                "Master Orchestrator telemetry dashboard"
            ),
            voiceCommands = listOf(
                "MJ parallel agents fleet activate karo",
                "MJ ek agent ko research aur doosre ko coding par lagao",
                "MJ running agents ka progress status batao",
                "MJ sabhi parallel tasks complete hone par notify karo"
            ),
            actionType = "PARALLEL_AGENTS"
        ),
        MayaFeatureItem(
            id = "maya_23_self_learning_skills",
            number = 23,
            title = "Self-Learning Skills Engine",
            hindiTitle = "सेल्फ-लर्निंग स्किल्स इंजन",
            statusTag = "🔴 New Major Feature",
            category = "Autonomous Fleet",
            iconName = "Psychology",
            description = "'MJ, ye kaam karna seekho' bolne par assistant naya logic code likhta hai, sandbox me test karta hai, version install karta hai aur zaroorat padne par safe rollback karta hai.",
            capabilities = listOf(
                "'Learn new skill' voice prompt interpreter",
                "Auto code generation for new tool capability",
                "Sandbox verification test before deployment",
                "Semantic skill catalog auto-indexing",
                "Skill versioning, toggle disable aur instant rollback"
            ),
            voiceCommands = listOf(
                "MJ ye kaam karna seekho",
                "MJ naye skill ka test run karo",
                "MJ installed self-learning skills ki list dikhao",
                "MJ pichla skill rollback kar do"
            ),
            actionType = "SELF_LEARNING"
        ),
        MayaFeatureItem(
            id = "maya_24_plugin_system",
            number = 24,
            title = "Universal Plugin & API Marketplace",
            hindiTitle = "यूनिवर्सल प्लगइन और एपीआई सिस्टम",
            statusTag = "🔴 New Architecture Module",
            category = "Autonomous Fleet",
            iconName = "Extension",
            description = "Developer custom plugins, third-party REST APIs, webhooks aur micro-tools install karein jisse unke functions instant natural voice commands ban jate hain.",
            capabilities = listOf(
                "JSON/YAML plugin schema loader",
                "Custom HTTP endpoint & webhook voice bindings",
                "Developer sandbox for creating tools",
                "Instant dynamic voice dispatch mapping",
                "Enable/disable community plugins securely"
            ),
            voiceCommands = listOf(
                "MJ naya plugin install karo",
                "MJ active plugins verify karo",
                "MJ custom webhook execute karo"
            ),
            actionType = "PLUGIN_SYSTEM"
        ),
        MayaFeatureItem(
            id = "maya_25_macro_recorder",
            number = 25,
            title = "Macro Recorder & Auto-Replayer",
            hindiTitle = "मैक्रो रिकॉर्डर और ऑटो-रिप्लेयर",
            statusTag = "🔴 New Feature",
            category = "Autonomous Fleet",
            iconName = "FiberManualRecord",
            description = "Ek baar kaam manually karein: Record -> Kaam karein -> Stop -> MJ repetitive task seekh jata hai. Fir one-word voice command par poora workflow auto-replay karein!",
            capabilities = listOf(
                "One-touch Action sequence recording",
                "Step sequence editor (Tap, Type, Wait, Scroll, Launch)",
                "Single-word voice trigger assignment",
                "Scheduled macro automation (e.g. Roz subah 9 baje)",
                "Export, duplicate aur share macros"
            ),
            voiceCommands = listOf(
                "MJ macro record shuru karo",
                "MJ macro recording stop karo aur 'Morning Routine' naam do",
                "MJ Morning Routine macro chalao",
                "MJ saved macros ki list dikhao"
            ),
            actionType = "MACRO_RECORDER"
        ),
        MayaFeatureItem(
            id = "maya_26_long_term_memory",
            number = 26,
            title = "Persistent Long-Term Memory",
            hindiTitle = "एडवांस्ड लॉन्ग-टर्म मेमोरी",
            statusTag = "🟡 Upgrade",
            category = "Autonomous Fleet",
            iconName = "Memory",
            description = "Aapke personal facts, projects, preferences, session history, routine aur context ko permanently yaad rakhna. Voice se naya fact save karein ya delete karein.",
            capabilities = listOf(
                "Permanent local encrypted key-value fact store",
                "Personal preferences (Diet, Family, Favorite tech, Work hours)",
                "Semantic auto-recall during conversations",
                "View and edit stored memory items",
                "'Forget' command to permanently erase specific facts"
            ),
            voiceCommands = listOf(
                "MJ yaad rakhna ki meri car ka number UP16 AB 1234 hai",
                "MJ meri car ka number kya hai?",
                "MJ mere baare me tum kya jaanti ho?",
                "MJ purani meeting ka context bhool jao"
            ),
            actionType = "LONG_TERM_MEMORY"
        ),
        MayaFeatureItem(
            id = "maya_27_voice_security",
            number = 27,
            title = "Voice Security & Biometric Guardian",
            hindiTitle = "वॉइस सिक्योरिटी और बायोमेट्रिक गार्डियन",
            statusTag = "🟢 Existing -> Advanced Guardian",
            category = "PC & System",
            iconName = "Security",
            description = "User ki biometric voiceprint frequency pehchanna: Guest users ko sensitive actions (Terminal commands, file deletion, bank details) se block karna.",
            capabilities = listOf(
                "Acoustic biometric voiceprint matching",
                "Owner vs Guest speaker identification",
                "Sensitive action lockdown for non-verified voices",
                "Emergency lock voice challenge",
                "Audit log of attempted unauthorized voice commands"
            ),
            voiceCommands = listOf(
                "MJ meri aawaz verify karo",
                "MJ guest mode enable karo",
                "MJ voice security audit logs dikhao"
            ),
            actionType = "VOICE_SECURITY"
        ),
        MayaFeatureItem(
            id = "maya_28_multiple_personas",
            number = 28,
            title = "Multiple AI Personas",
            hindiTitle = "मल्टीपल एआई पर्सोना (MAYA, Friday, Venom, MJ)",
            statusTag = "🟢 Existing -> Advanced Personas",
            category = "Intelligence & Coding",
            iconName = "RecordVoiceOver",
            description = "Single tap ya voice se personality switch karein: MAYA (Tactical & Sweet), Friday (Iron Man Tech Assistant), Venom (Aggressive & Cool), aur MJ (Warm Cyber Companion).",
            capabilities = listOf(
                "Instant persona switching with distinct system prompt tones",
                "MAYA: Advanced Tactical, helpful and super-intelligent",
                "FRIDAY: Ultra-futuristic Tony Stark AI assistant style",
                "VENOM: Deep energetic badass persona with witty sarcasm",
                "MJ: Caring, loyal and deeply intuitive companion",
                "Custom pitch, speed and avatar sync"
            ),
            voiceCommands = listOf(
                "MJ Maya persona par switch karo",
                "MJ Friday mode activate karo",
                "MJ Venom persona chalu karo",
                "MJ wapas MJ mode me aa jao"
            ),
            actionType = "MULTIPLE_PERSONAS"
        ),
        MayaFeatureItem(
            id = "maya_29_sleep_wake",
            number = 29,
            title = "Sleep & Offline Wake Mode",
            hindiTitle = "स्लीप और ऑफलाइन वेक मोड",
            statusTag = "🟡 Upgrade",
            category = "PC & System",
            iconName = "Bedtime",
            description = "'So jao' bolne par assistant low-power sleep mode me chali jati hai, aur 'Hey MJ' ya 'Hey Maya' bolne par 0ms delay me wapas active ho jati hai.",
            capabilities = listOf(
                "'So jao' / 'Sleep mode' instant voice standby",
                "Ultra low-power wake word listener",
                "'Hey MJ' aur 'Hey Maya' simultaneous triggers",
                "Zero battery drain background sleep daemon",
                "Instant wake greeting with system status summary"
            ),
            voiceCommands = listOf(
                "MJ so jao",
                "Hey MJ, uth jao",
                "Hey Maya, wake up",
                "MJ standby mode me chale jao"
            ),
            actionType = "SLEEP_WAKE"
        ),
        MayaFeatureItem(
            id = "maya_30_one_click_updates",
            number = 30,
            title = "One-Click In-App Updates",
            hindiTitle = "वन-क्लिक इन-ऐप अपडेट्स",
            statusTag = "🟢 Existing System",
            category = "PC & System",
            iconName = "Update",
            description = "In-app version checking, delta update packages, automatic integrity check, zero data loss migration aur one-click install.",
            capabilities = listOf(
                "Cloud version comparison & changelog fetch",
                "One-tap in-app download and hot reload",
                "Database schema auto-migration",
                "Beta channel testing opt-in"
            ),
            voiceCommands = listOf(
                "MJ app updates check karo",
                "MJ naya version install karo",
                "MJ changelog padh ke sunao"
            ),
            actionType = "ONE_CLICK_UPDATES"
        ),
        MayaFeatureItem(
            id = "maya_31_smart_home",
            number = 31,
            title = "Smart Home & IoT Commander",
            hindiTitle = "स्मार्ट होम और आईओटी कमांडर",
            statusTag = "🟡 Foundation -> Big Expansion",
            category = "PC & System",
            iconName = "Home",
            description = "Home Assistant, Tuya, Philips Hue aur smart devices control: Lights ON/OFF, Fan speed, AC temperature, Smart plugs aur automated morning/night scenes.",
            capabilities = listOf(
                "Home Assistant local API connector",
                "Smart Light color, brightness aur power control",
                "AC temperature adjustment aur timer",
                "Smart plugs and switches toggle",
                "Good Morning & Sleep time IoT smart scenes"
            ),
            voiceCommands = listOf(
                "MJ room ki light on karo",
                "MJ AC ka temperature 24 degree kar do",
                "MJ fan band karo",
                "MJ Good Night scene activate karo"
            ),
            actionType = "SMART_HOME"
        ),
        MayaFeatureItem(
            id = "maya_32_byom_ai",
            number = 32,
            title = "BYOM — Bring Your Own AI Model",
            hindiTitle = "अपना एआई मॉडल (BYOM Engine)",
            statusTag = "🟢 Strong Foundation + Local LLMs",
            category = "Intelligence & Coding",
            iconName = "Tune",
            description = "OpenAI, Google Gemini, Groq, DeepSeek, Anthropic Claude, OpenRouter, ya local Ollama aur LM Studio models ko seamlessly switch karein.",
            capabilities = listOf(
                "Multi-provider API key management with local encrypted vault",
                "Google Gemini 2.5 Flash & Pro",
                "Groq Ultra-Fast Llama 3 70B & 8B",
                "DeepSeek V3 & R1 Reasoning models",
                "OpenAI GPT-4o & o1",
                "Local Offline LLMs via Ollama & LM Studio (127.0.0.1:11434)"
            ),
            voiceCommands = listOf(
                "MJ AI provider DeepSeek par switch karo",
                "MJ Groq ultra-fast model select karo",
                "MJ local Ollama model connect karo",
                "MJ model parameters adjust karo"
            ),
            actionType = "BYOM_AI"
        )
    )
}
