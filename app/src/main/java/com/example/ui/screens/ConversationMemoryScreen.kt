package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ConversationEntity
import com.example.model.AppScreen
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationMemoryScreen(
    viewModel: MyraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val conversations by viewModel.savedConversations.collectAsState()
    val totalCount by viewModel.totalConversationCount.collectAsState()
    val searchQuery by viewModel.conversationSearchQuery.collectAsState()
    val selectedFilter by viewModel.conversationFilterTopic.collectAsState()
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    val filterOptions = listOf("ALL", "VOICE", "CHAT", "Code/PC", "Settings", "Personal")

    val quickRecallPrompts = listOf(
        "PC bridge connect ke baare me kya pucha tha?",
        "Subah ka alarm aur routine yaad dilaao",
        "Wi-Fi aur torch settings wali baat recall karo",
        "Coding agent ke baare me purani baat batao",
        "Gemini aur OpenAI key setup yaad hai?"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF080810))
            .statusBarsPadding()
    ) {
        // Top App Bar
        Surface(
            color = Color(0xFF0D0D18),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF1E1E30), Color(0xFF2E1734))))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        modifier = Modifier.testTag("conversation_db_back")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
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
                                text = "Conversation Database",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF2A153A)
                            ) {
                                Text(
                                    text = "ROOM DB",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD946EF),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "पूरी बातचीत सेव • Search & Recall Engine",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9E9EB2)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { viewModel.prepopulateInitialConversations() },
                        modifier = Modifier.testTag("reload_demo_conversations")
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Sync",
                            tint = Color(0xFF00E5FF)
                        )
                    }
                    IconButton(
                        onClick = { showClearConfirmDialog = true },
                        modifier = Modifier.testTag("clear_db_button")
                    ) {
                        Icon(
                            Icons.Default.DeleteSweep,
                            contentDescription = "Clear All",
                            tint = Color(0xFFFF5252)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Interactive Architecture Flow Banner (Matching User's Request)
            item {
                ArchitectureFlowCard()
            }

            // 2. Metrics & Status Banner
            item {
                DatabaseMetricsCard(
                    totalCount = totalCount,
                    voiceCount = conversations.count { it.mode == "VOICE" },
                    chatCount = conversations.count { it.mode == "CHAT" }
                )
            }

            // 3. Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateConversationSearchQuery(it) },
                    placeholder = {
                        Text(
                            "Search conversation memory (e.g. 'PC bridge', 'alarm', 'API key')...",
                            color = Color(0xFF7A7A8E),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MyraRedGlow)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.updateConversationSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("conversation_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF131320),
                        unfocusedContainerColor = Color(0xFF0F0F1A),
                        focusedBorderColor = MyraRedGlow,
                        unfocusedBorderColor = Color(0xFF262638),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // 4. Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filterOptions) { filter ->
                        val isSelected = selectedFilter == filter
                        Surface(
                            onClick = { viewModel.setConversationFilterTopic(filter) },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) MyraRed else Color(0xFF171726),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(
                                    if (isSelected) listOf(Color(0xFFFF2A55), Color(0xFFFF5E7E))
                                    else listOf(Color(0xFF2E2E42), Color(0xFF202030))
                                )
                            ),
                            modifier = Modifier.testTag("filter_chip_$filter")
                        ) {
                            Text(
                                text = when (filter) {
                                    "ALL" -> "All ($totalCount)"
                                    "VOICE" -> "🎙️ Voice"
                                    "CHAT" -> "💬 Chat"
                                    else -> filter
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFFB5B5C8),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // 5. Quick Recall Prompts
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = "Smart Recall",
                            tint = Color(0xFFFFD600),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Quick Recall Triggers (बोलकर या टैप करके पूछें):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE2E2F0)
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(quickRecallPrompts) { prompt ->
                            Surface(
                                onClick = {
                                    viewModel.recallConversationPrompt(prompt)
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF141424),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = Brush.horizontalGradient(listOf(Color(0xFF2F2040), Color(0xFF1B1B2E)))
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = Color(0xFF00E5FF),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = prompt,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = Color(0xFFD4D4E8),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "Search Results (${conversations.size})" else "Recent Saved Conversations",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Auto-saved in SQLite",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF7A7A8E)
                    )
                }
            }

            // 7. Conversation Items
            if (conversations.isEmpty()) {
                item {
                    EmptyConversationCard(onSeedClick = { viewModel.prepopulateInitialConversations() })
                }
            } else {
                items(conversations, key = { it.id }) { item ->
                    ConversationItemCard(
                        entity = item,
                        onSpeak = {
                            viewModel.speak(item.messageText)
                        },
                        onRecallInChat = {
                            viewModel.recallItemIntoChat(item)
                        },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Conversation", item.messageText))
                            viewModel.showToast("Copied to clipboard!")
                        },
                        onDelete = {
                            viewModel.deleteConversation(item.id)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("Clear Conversation Database?") },
            text = { Text("Kya aap sach me saari saved conversations Room database se delete karna chahte hain?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllConversations()
                        showClearConfirmDialog = false
                    }
                ) {
                    Text("Delete All", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancel", color = Color.White)
                }
            },
            containerColor = Color(0xFF171724),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFB5B5C8)
        )
    }
}

@Composable
private fun ArchitectureFlowCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF100D1F)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFFFF2A55), Color(0xFF00E5FF)))
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF26123D)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Storage,
                        contentDescription = null,
                        tint = Color(0xFFD946EF),
                        modifier = Modifier.size(15.dp)
                    )
                }
                Text(
                    text = "Conversation Memory Pipeline",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Visual diagram flow
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF090714))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FlowStepRow(
                    stepNum = "1",
                    title = "आपकी Voice / Chat",
                    desc = "Har voice query ya chat message intercept hota hai",
                    tint = Color(0xFF00E5FF)
                )
                FlowStepArrow()
                FlowStepRow(
                    stepNum = "2",
                    title = "MYRA Neural Core",
                    desc = "Natural language processing & intent resolution",
                    tint = Color(0xFFFF2A55)
                )
                FlowStepArrow()
                FlowStepRow(
                    stepNum = "3",
                    title = "Conversation Database (Room SQLite)",
                    desc = "पूरी बातचीत SQLite Database me instant persist hoti hai",
                    tint = Color(0xFFD946EF),
                    isHighlighted = true
                )
                FlowStepArrow()
                FlowStepRow(
                    stepNum = "4",
                    title = "Search / Fuzzy Semantic Recall",
                    desc = "Keywords, timestamp & topic indexation query engine",
                    tint = Color(0xFFFFD600)
                )
                FlowStepArrow()
                FlowStepRow(
                    stepNum = "5",
                    title = "MYRA पुराने conversations ढूँढकर जवाब देगी",
                    desc = "Voice ya text me exact context ke sath memory recall",
                    tint = Color(0xFF00E676)
                )
            }
        }
    }
}

@Composable
private fun FlowStepRow(
    stepNum: String,
    title: String,
    desc: String,
    tint: Color,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isHighlighted) {
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E1030))
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = tint.copy(alpha = 0.2f),
            modifier = Modifier.size(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stepNum,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    fontWeight = FontWeight.Bold,
                    color = tint
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) Color.White else tint
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color(0xFF88889C)
            )
        }
    }
}

@Composable
private fun FlowStepArrow() {
    Row(
        modifier = Modifier.padding(start = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.ArrowDownward,
            contentDescription = null,
            tint = Color(0xFF424258),
            modifier = Modifier.size(12.dp)
        )
    }
}

@Composable
private fun DatabaseMetricsCard(
    totalCount: Int,
    voiceCount: Int,
    chatCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11111E)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(Color(0xFF2B2B40), Color(0xFF1C1C2C)))
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MetricItem(label = "Total Conversations", value = "$totalCount", color = Color(0xFF00E5FF))
            VerticalDivider(modifier = Modifier.height(30.dp), color = Color(0xFF26263A))
            MetricItem(label = "Voice Queries", value = "$voiceCount", color = Color(0xFFFF2A55))
            VerticalDivider(modifier = Modifier.height(30.dp), color = Color(0xFF26263A))
            MetricItem(label = "Text Chats", value = "$chatCount", color = Color(0xFFD946EF))
            VerticalDivider(modifier = Modifier.height(30.dp), color = Color(0xFF26263A))
            MetricItem(label = "Storage Engine", value = "Room SQLite", color = Color(0xFF00E676))
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            color = Color(0xFF88889C)
        )
    }
}

@Composable
private fun ConversationItemCard(
    entity: ConversationEntity,
    onSpeak: () -> Unit,
    onRecallInChat: () -> Unit,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    val isUser = entity.isUser
    val borderColor = if (isUser) Color(0xFF28324E) else Color(0xFF3F1D38)
    val cardBg = if (isUser) Color(0xFF101422) else Color(0xFF160F1F)
    val senderTint = if (isUser) Color(0xFF00E5FF) else Color(0xFFFF2A55)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("conversation_card_${entity.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(borderColor, Color(0xFF181826))))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Sender, Mode, Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(senderTint.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isUser) Icons.Default.Person else Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = senderTint,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    Text(
                        text = if (isUser) "Aap (User)" else "MYRA (AI)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = senderTint
                    )

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (entity.mode == "VOICE") Color(0xFF330B14) else Color(0xFF142434)
                    ) {
                        Text(
                            text = if (entity.mode == "VOICE") "🎙️ VOICE" else "💬 CHAT",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = if (entity.mode == "VOICE") Color(0xFFFF5252) else Color(0xFF00E5FF),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }

                    if (entity.topic.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF1E1E2C)
                        ) {
                            Text(
                                text = entity.topic,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = Color(0xFFB5B5C8),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "${entity.formattedTime} • ${entity.relativeTime}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color(0xFF7A7A8E)
                )
            }

            // Message text
            Text(
                text = entity.messageText,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = Color.White,
                lineHeight = 18.sp
            )

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Listen via TTS
                IconButton(onClick = onSpeak, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Listen",
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Recall into chat
                Button(
                    onClick = onRecallInChat,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF261838)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFFD946EF),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Recall in Chat",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFFD946EF),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Copy
                IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = Color(0xFF9E9EB2),
                        modifier = Modifier.size(15.dp)
                    )
                }

                // Delete
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyConversationCard(onSeedClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131320)),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF262638), Color(0xFF1A1A28))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.HistoryEdu,
                contentDescription = null,
                tint = Color(0xFF7A7A8E),
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Koi conversation nahi mili",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Aap jo bhi voice ya text me baat karenge, vo automatically SQLite Database me save ho jayegi.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9E9EB2),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Button(
                onClick = onSeedClick,
                colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.AddComment, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pre-populate Sample Conversations", fontWeight = FontWeight.Bold)
            }
        }
    }
}
