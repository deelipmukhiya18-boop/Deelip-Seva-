package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ChatMessage
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

data class ApiProvider(
    val id: String,
    val name: String,
    val domain: String,
    val url: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: MyraViewModel,
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var activeChatView by remember { mutableStateOf<String?>("MYRA") } // null = Chats list, "MYRA" = Myra chat, "COMMUNITY" = Community
    var showApiKeySheet by remember { mutableStateOf(false) }
    var quickSheetApiKey by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val apiProviders = remember {
        listOf(
            ApiProvider("gemini", "Gemini", "Google AI Studio", "https://aistudio.google.com"),
            ApiProvider("groq", "Groq", "console.groq.com", "https://console.groq.com"),
            ApiProvider("openrouter", "OpenRouter", "openrouter.ai", "https://openrouter.ai"),
            ApiProvider("deepseek", "DeepSeek", "platform.deepseek.com", "https://platform.deepseek.com"),
            ApiProvider("tavily", "Tavily (Deep Research)", "app.tavily.com", "https://app.tavily.com"),
            ApiProvider("openai", "OpenAI", "platform.openai.com", "https://platform.openai.com"),
            ApiProvider("anthropic", "Claude / Anthropic", "console.anthropic.com", "https://console.anthropic.com")
        )
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF09090E))
    ) {
        if (activeChatView == null) {
            // --- CHATS HUB SCREEN (Matching Screenshot exactly) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 18.dp)
            ) {
                // 1. Top Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chats",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Key icon (Opens Get an API key tool)
                        IconButton(
                            onClick = {
                                viewModel.performHaptic()
                                showApiKeySheet = true
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = "Get an API key",
                                tint = Color(0xFFD4D4DC),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Globe / Web icon
                        IconButton(
                            onClick = {
                                viewModel.performHaptic()
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"))
                                try { context.startActivity(intent) } catch (_: Exception) {}
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Web Access",
                                tint = Color(0xFFD4D4DC),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Community / Group icon
                        IconButton(
                            onClick = {
                                viewModel.performHaptic()
                                activeChatView = "COMMUNITY"
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = "MYRA Community",
                                tint = Color(0xFFD4D4DC),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Search icon
                        IconButton(
                            onClick = {
                                viewModel.performHaptic()
                                isSearchActive = !isSearchActive
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFFD4D4DC),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                // Search Box if toggled
                if (isSearchActive) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search conversations...", color = MyraTextMuted) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MyraRedGlow) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = null, tint = Color.White)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14141E),
                            unfocusedContainerColor = Color(0xFF14141E),
                            focusedBorderColor = MyraRed,
                            unfocusedBorderColor = Color(0xFF262638),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    // Chat Item 1: MYRA AI Assistant
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.performHaptic()
                                    activeChatView = "MYRA"
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF181024))
                                    .border(1.5.dp, Color(0xFF9333EA).copy(alpha = 0.6f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.myra_ai_avatar_1788284446471),
                                    contentDescription = "MJ Avatar",
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "MJ",
                                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF330B14)
                                    ) {
                                        Text(
                                            text = "AI",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFFF5252),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Your always-on assistant",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF8E8EA2)
                                )
                            }
                        }
                    }

                    // Chat Item 2: MJ Community
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.performHaptic()
                                    activeChatView = "COMMUNITY"
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Community Avatar
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF280B12))
                                    .border(1.dp, Color(0xFF451420), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = "MJ Community",
                                    tint = Color(0xFFFF3355),
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "MJ Community",
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Everyone on MJ, in one chat",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF8E8EA2)
                                )
                            }
                        }
                    }

                    // Empty state footer matching screenshot
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No conversations yet - tap the search icon to find someone.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF68687C),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            }
        } else if (activeChatView == "MYRA") {
            // --- MYRA AI COMPANION CHAT VIEW ---
            MyraConversationView(
                viewModel = viewModel,
                messages = messages,
                onBack = { activeChatView = null },
                onOpenApiKeyTool = { showApiKeySheet = true }
            )
        } else if (activeChatView == "COMMUNITY") {
            // --- MYRA COMMUNITY CHAT VIEW ---
            CommunityChatView(
                onBack = { activeChatView = null },
                onOpenApiKeyTool = { showApiKeySheet = true }
            )
        }

        // --- GET AN API KEY BOTTOM SHEET (Matching Screenshot Exactly) ---
        if (showApiKeySheet) {
            ModalBottomSheet(
                onDismissRequest = { showApiKeySheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFF0D0D14),
                contentColor = Color.White,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 10.dp)
                            .size(width = 44.dp, height = 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF38384A))
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                        .padding(bottom = 32.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Get an API key",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Don't have a key for one of MJ's providers yet? Tap one to open its official page, or paste your key below to unlock voice immediately.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF9E9EB2),
                            lineHeight = 20.sp
                        )
                    }

                    // Direct Key Input to immediately unlock voice
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF191924)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(Color(0xFFFFB300), MyraRed))
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Enter API Key Directly:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            OutlinedTextField(
                                value = quickSheetApiKey,
                                onValueChange = { quickSheetApiKey = it },
                                placeholder = { Text("Paste Gemini or OpenAI key...", color = Color(0xFF7A7A8E), fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                trailingIcon = {
                                    TextButton(
                                        onClick = {
                                            val clip = clipboardManager.getText()?.text
                                            if (!clip.isNullOrBlank()) {
                                                quickSheetApiKey = clip.trim()
                                                viewModel.showToast("Pasted from clipboard")
                                            }
                                        }
                                    ) {
                                        Text("PASTE", fontSize = 11.sp, color = Color(0xFFFFB300), fontWeight = FontWeight.Bold)
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFB300),
                                    unfocusedBorderColor = Color(0xFF2E2E42),
                                    focusedContainerColor = Color(0xFF101018),
                                    unfocusedContainerColor = Color(0xFF101018),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    if (quickSheetApiKey.isNotBlank()) {
                                        viewModel.saveQuickApiKey(quickSheetApiKey)
                                        showApiKeySheet = false
                                        quickSheetApiKey = ""
                                    } else {
                                        viewModel.showToast("Kripya API key enter karein")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Save Key & Unlock Voice", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(apiProviders) { provider ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        viewModel.performHaptic()
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(provider.url))
                                        try {
                                            context.startActivity(intent)
                                        } catch (_: Exception) {}
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Circular Red Key Icon Badge matching screenshot
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF2C070F)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Key,
                                        contentDescription = null,
                                        tint = Color(0xFFFF1E42),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = provider.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = provider.domain,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF88889C)
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

@Composable
private fun MyraConversationView(
    viewModel: MyraViewModel,
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    onOpenApiKeyTool: () -> Unit
) {
    val isApiKeyConfigured by viewModel.isApiKeyConfigured.collectAsState()
    var textInput by remember { mutableStateOf("") }

    val promptSuggestions = listOf(
        "Phone slow kyun hai?",
        "Subah 6 baje alarm laga do",
        "Rahul ko WhatsApp message bhejo",
        "Storage clean kar do",
        "PC pe movie chalao"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1C102A)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.myra_ai_avatar_1788284446471),
                        contentDescription = "MJ",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("MJ", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF330B14)) {
                            Text("AI", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color(0xFFFF5252), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                        }
                    }
                    Text("Always-on neural assistant", style = MaterialTheme.typography.labelSmall, color = MyraNeonGreen)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onOpenApiKeyTool) {
                    Icon(Icons.Default.Key, contentDescription = "API Keys", tint = Color(0xFFFF2A55))
                }
                IconButton(onClick = { viewModel.openDialog("VOICE_MODE") }) {
                    Icon(Icons.Default.GraphicEq, contentDescription = "Voice Mode", tint = MyraRedGlow)
                }
            }
        }

        HorizontalDivider(color = Color(0xFF1E1E2C))

        // API Key Voice Locked Banner
        if (!isApiKeyConfigured) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onOpenApiKeyTool() },
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF241018),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(Color(0xFFFFB300), MyraRed))
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VpnKey,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(20.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "MJ Voice Mode Locked",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "MJ voice mein baat tab karegi jab API Key add karenge. Tap to enter key.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFC0C0D4),
                            fontSize = 11.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MyraRed
                    ) {
                        Text(
                            text = "ADD KEY",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Chat messages stream
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isUser = msg.isUser
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) MyraRedDark else Color(0xFF141420)
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.linearGradient(
                                listOf(if (isUser) MyraRed else Color(0xFF2A2A3E), Color(0xFF181826))
                            )
                        ),
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = msg.text,
                                color = MyraTextPrimary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (!isUser) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (!isApiKeyConfigured) {
                                                viewModel.showToast("Voice mein baat tab hogi jab API Key add karenge!")
                                                onOpenApiKeyTool()
                                            } else {
                                                viewModel.speak(msg.text)
                                            }
                                        },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (!isApiKeyConfigured) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                            contentDescription = "Aawaz sunein",
                                            tint = if (!isApiKeyConfigured) Color(0xFF8E8E9C) else MyraRedGlow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Suggestions pills
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            items(promptSuggestions) { suggestion ->
                Surface(
                    onClick = {
                        viewModel.sendUserMessage(suggestion)
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF141422),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF242436), Color(0xFF1A1A28))))
                ) {
                    Text(
                        text = suggestion,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MyraTextSecondary
                    )
                }
            }
        }

        // Bottom Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Ask MJ anything...", color = MyraTextMuted) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyraRed,
                    unfocusedBorderColor = Color(0xFF26263A),
                    focusedContainerColor = Color(0xFF12121D),
                    unfocusedContainerColor = Color(0xFF12121D),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            IconButton(
                onClick = { viewModel.openDialog("VOICE_MODE") },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (!isApiKeyConfigured) Color(0xFF261217) else Color(0xFF1E1E2E))
            ) {
                Icon(
                    imageVector = if (!isApiKeyConfigured) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Voice Mode",
                    tint = if (!isApiKeyConfigured) Color(0xFFFFB300) else MyraRedGlow
                )
            }

            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        viewModel.sendUserMessage(textInput)
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MyraRed)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
private fun CommunityChatView(
    onBack: () -> Unit,
    onOpenApiKeyTool: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    val communityMessages = remember {
        mutableStateListOf(
            Triple("Aarav", "Has anyone tried the new Mission Mode? Autonomous tasks are crazy fast!", "09:42"),
            Triple("Priya", "Yes! Connected my Gemini key and it refactored an entire Kotlin file in 5 seconds.", "09:45"),
            Triple("Rohan", "The 30 voice models are super natural too. Loving the anime mode voice.", "09:51")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2A0912)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = Color(0xFFFF3355), modifier = Modifier.size(20.dp))
                }

                Column {
                    Text("MJ Community", fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Text("1,420 members online", style = MaterialTheme.typography.labelSmall, color = MyraNeonGreen)
                }
            }

            IconButton(onClick = onOpenApiKeyTool) {
                Icon(Icons.Default.Key, contentDescription = "API Keys", tint = Color(0xFFFF2A55))
            }
        }

        HorizontalDivider(color = Color(0xFF1E1E2C))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
        ) {
            items(communityMessages) { (sender, msg, time) ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF12121E)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFF26263A), Color(0xFF181828)))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(sender, fontWeight = FontWeight.Bold, color = MyraNeonCyan, style = MaterialTheme.typography.labelMedium)
                            Text(time, style = MaterialTheme.typography.labelSmall, color = Color(0xFF707086))
                        }
                        Text(msg, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Message input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Message community...", color = MyraTextMuted) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyraRed,
                    unfocusedBorderColor = Color(0xFF26263A),
                    focusedContainerColor = Color(0xFF12121D),
                    unfocusedContainerColor = Color(0xFF12121D),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        communityMessages.add(Triple("Deelip Mukhiya", textInput, "Now"))
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MyraRed)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}
