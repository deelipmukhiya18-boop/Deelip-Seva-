package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ToolGuideItem
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@Composable
fun ToolsGuideScreen(
    viewModel: MyraViewModel,
    toolItems: List<ToolGuideItem>,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSection by remember { mutableIntStateOf(0) } // 0 means all

    val sections = remember(toolItems) {
        val list = mutableListOf(0 to "All Tools (${toolItems.size})")
        val grouped = toolItems.groupBy { it.sectionNumber }
        grouped.keys.sorted().forEach { secNum ->
            val firstItem = grouped[secNum]?.firstOrNull()
            val title = firstItem?.sectionTitle ?: "Section $secNum"
            val count = grouped[secNum]?.size ?: 0
            list.add(secNum to "$secNum. $title ($count)")
        }
        list
    }

    val filteredList = toolItems.filter { item ->
        val matchesSection = selectedSection == 0 || item.sectionNumber == selectedSection
        val matchesSearch = item.toolName.contains(searchQuery, ignoreCase = true) ||
                item.kyaKartaHai.contains(searchQuery, ignoreCase = true) ||
                item.bolneWalaCommand.contains(searchQuery, ignoreCase = true)
        matchesSection && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MyraDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // App Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "340 Karod+ Universal Tools",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "100% Automatic Execution Engine · ${toolItems.size}+ Preset Voice Tools",
                        style = MaterialTheme.typography.labelSmall,
                        color = MyraRed
                    )
                }
            }
        }

        // 340 Karod+ Universal Autonomous Engine Active Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MyraRed, MyraNeonCyan))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MyraRed.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = MyraRedGlow, modifier = Modifier.size(24.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "⚡ 340 Karod+ Auto-Executor ACTIVE",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Har feature, app launch, hardware toggle, voice command aur AI action bina ruke 100% automatic perform hota hai.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                }
            }
        }

        // Search bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search tools, commands, or kya karta hai...", color = MyraTextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MyraTextMuted) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyraRed,
                    unfocusedBorderColor = MyraCardBorder,
                    focusedContainerColor = MyraCardDarkElevated,
                    unfocusedContainerColor = MyraCardDarkElevated
                )
            )
        }

        // Category scroll bar
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(sections) { (secNum, label) ->
                    val isSelected = selectedSection == secNum
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedSection = secNum },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MyraRed,
                            selectedLabelColor = Color.White,
                            containerColor = MyraCardDarkElevated,
                            labelColor = MyraTextMuted
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) MyraRed else MyraCardBorder
                        )
                    )
                }
            }
        }

        // Tool Items List
        items(filteredList, key = { it.id }) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MyraCardBorderGlow, MyraCardBorder)))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.toolName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MyraRedGlow
                        )
                        if (item.safetyGuard != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MyraRedDark)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.safetyGuard,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = item.kyaKartaHai,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MyraTextPrimary
                    )

                    // Spoken command chip
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MyraDarkBg,
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MyraCardBorder, MyraCardBorderGlow))),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = MyraNeonCyan, modifier = Modifier.size(16.dp))
                                Text(
                                    text = item.bolneWalaCommand,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MyraNeonCyan,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Button(
                                onClick = { viewModel.executeToolCommand(item) },
                                colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("⚡ Auto-Run", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
