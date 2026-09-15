package com.example.ui.screens

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MayaFleetScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPcControl: () -> Unit = {},
    onNavigateToFileManager: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val mayaList by viewModel.mayaFleetList.collectAsState()
    val activePersona by viewModel.activePersona.collectAsState()
    val subAgents by viewModel.mayaSubAgents.collectAsState()
    val recordedMacros by viewModel.recordedMacros.collectAsState()
    val clipboardItems by viewModel.clipboardHistory.collectAsState()
    val expenses by viewModel.expensesList.collectAsState()
    val excelCells by viewModel.excelCells.collectAsState()
    val stockQuotes by viewModel.stockQuotes.collectAsState()
    val isMacroRecording by viewModel.isMacroRecording.collectAsState()
    val isLiveCommentaryActive by viewModel.isLiveCommentaryActive.collectAsState()
    val isScreenRecording by viewModel.isScreenRecording.collectAsState()

    var selectedCategory by remember { mutableStateOf("All (32)") }
    var searchQuery by remember { mutableStateOf("") }
    var activeModalFeature by remember { mutableStateOf<MayaFeatureItem?>(null) }
    var executionToast by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        "All (32)",
        "PC & System",
        "Intelligence & Coding",
        "Vision & Media",
        "Communication & Finance",
        "Autonomous Fleet"
    )

    val filteredList = remember(mayaList, selectedCategory, searchQuery) {
        mayaList.filter { item ->
            val matchesCategory = if (selectedCategory == "All (32)") true else item.category == selectedCategory
            val matchesSearch = if (searchQuery.isBlank()) true else {
                item.title.contains(searchQuery, ignoreCase = true) ||
                        item.hindiTitle.contains(searchQuery, ignoreCase = true) ||
                        item.description.contains(searchQuery, ignoreCase = true) ||
                        item.capabilities.any { it.contains(searchQuery, ignoreCase = true) }
            }
            matchesCategory && matchesSearch
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A12))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Bar
            Surface(
                color = Color(0xFF10101C),
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier.testTag("btn_maya_back")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Mayra 999 Module Fleet",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = Color(0xFFFF5252).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFFFF5252), Color(0xFFFF7B7B))))
                                    ) {
                                        Text(
                                            text = "9.99 MEX",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = Color(0xFFFF7B7B),
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "999 Complete Integrated Capabilities from Mayra Architecture",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF9E9EB0)
                                )
                            }
                        }

                        // Current Persona Badge
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF1E1E30),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MyraNeonCyan, Color(0xFF8B5CF6)))),
                            modifier = Modifier.clickable {
                                viewModel.executeMayaFeature("maya_28_multiple_personas")
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(MyraNeonGreen, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = activePersona,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Search Excel, Coding, WhatsApp, Macro, PC, Vision...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF707086)
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF9E9EB0))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color(0xFF9E9EB0))
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("search_maya_features"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF161626),
                            unfocusedContainerColor = Color(0xFF141422),
                            focusedBorderColor = Color(0xFF8B5CF6),
                            unfocusedBorderColor = Color(0xFF262638),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 2.dp)
                    ) {
                        items(categories) { category ->
                            val isSelected = selectedCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = category },
                                label = {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF7C3AED),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFF181828),
                                    labelColor = Color(0xFFA5A5BC)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) Color(0xFFC084FC) else Color(0xFF28283C)
                                )
                            )
                        }
                    }
                }
            }

            // List of 32 Feature Cards
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 14.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Quick Multi-Agent Status Banner
                item {
                    MultiAgentStatusCard(
                        subAgents = subAgents,
                        onOpenFleet = {
                            viewModel.executeMayaFeature("maya_22_parallel_agents")
                            val feature = mayaList.find { it.id == "maya_22_parallel_agents" }
                            activeModalFeature = feature
                        }
                    )
                }

                items(filteredList, key = { it.id }) { item ->
                    MayaFeatureCard(
                        item = item,
                        onRunFeature = {
                            if (item.actionType == "PC_CONTROL" || item.id == "maya_1_pc_control") {
                                onNavigateToPcControl()
                            } else if (item.actionType == "FILE_MANAGER" || item.id == "maya_2_file_manager") {
                                onNavigateToFileManager()
                            } else {
                                viewModel.executeMayaFeature(item.id)
                                executionToast = "Activated: ${item.title}"
                            }
                        },
                        onOpenConsole = {
                            if (item.actionType == "PC_CONTROL" || item.id == "maya_1_pc_control") {
                                onNavigateToPcControl()
                            } else if (item.actionType == "FILE_MANAGER" || item.id == "maya_2_file_manager") {
                                onNavigateToFileManager()
                            } else {
                                activeModalFeature = item
                            }
                        }
                    )
                }
            }
        }

        // Execution feedback toast
        executionToast?.let { msg ->
            LaunchedEffect(msg) {
                kotlinx.coroutines.delay(2800)
                executionToast = null
            }
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp, start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1E1B2E),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFF06B6D4)))),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MyraNeonGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = msg, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
            }
        }

        // Specialized Interactive Modals for Features
        activeModalFeature?.let { feature ->
            when (feature.actionType) {
                "EXCEL_AGENT" -> ExcelAgentDialog(
                    excelCells = excelCells,
                    onDismiss = { activeModalFeature = null },
                    onExecuteVoiceFormula = { formula ->
                        viewModel.sendUserMessage("Excel formula: $formula")
                    }
                )
                "AI_CODING_AGENT" -> AiCodingAgentDialog(
                    onDismiss = { activeModalFeature = null },
                    onRunPlan = { prompt ->
                        viewModel.sendUserMessage("Coding agent plan for: $prompt")
                    }
                )
                "SCREEN_VISION" -> ScreenVisionDialog(
                    onDismiss = { activeModalFeature = null },
                    onInspectScreen = {
                        viewModel.executeMayaFeature("maya_5_screen_vision")
                    }
                )
                "EXPENSE_UPI" -> ExpenseUpiDialog(
                    expenses = expenses,
                    onDismiss = { activeModalFeature = null },
                    onAddExpense = { merchant, amount, category ->
                        viewModel.sendUserMessage("Add expense ₹$amount at $merchant for $category")
                    }
                )
                "MACRO_RECORDER" -> MacroRecorderDialog(
                    macros = recordedMacros,
                    isRecording = isMacroRecording,
                    onDismiss = { activeModalFeature = null },
                    onToggleRecording = {
                        viewModel.executeMayaFeature("maya_25_macro_recorder")
                    },
                    onRunMacro = { macro ->
                        viewModel.sendUserMessage("Macro chalao ${macro.name}")
                    }
                )
                "PARALLEL_AGENTS" -> ParallelAgentsFleetDialog(
                    subAgents = subAgents,
                    onDismiss = { activeModalFeature = null },
                    onTriggerFleet = {
                        viewModel.sendUserMessage("Parallel agents fleet activate karo")
                    }
                )
                "STOCK_FINANCE" -> StockFinanceDialog(
                    quotes = stockQuotes,
                    onDismiss = { activeModalFeature = null }
                )
                "MULTIPLE_PERSONAS" -> PersonaSwitchDialog(
                    currentPersona = activePersona,
                    onSelectPersona = { persona ->
                        viewModel.sendUserMessage("Switch persona $persona")
                        activeModalFeature = null
                    },
                    onDismiss = { activeModalFeature = null }
                )
                "CLIPBOARD" -> ClipboardHistoryDialog(
                    clipboardItems = clipboardItems,
                    onDismiss = { activeModalFeature = null },
                    onClear = {
                        viewModel.sendUserMessage("Clipboard clear karo")
                    }
                )
                else -> GenericFeatureDetailDialog(
                    feature = feature,
                    onDismiss = { activeModalFeature = null },
                    onExecute = {
                        viewModel.executeMayaFeature(feature.id)
                        activeModalFeature = null
                    }
                )
            }
        }
    }
}

@Composable
private fun MultiAgentStatusCard(
    subAgents: List<MayaSubAgentStatus>,
    onOpenFleet: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOpenFleet() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131322)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(Color(0xFF7C3AED), Color(0xFF06B6D4).copy(alpha = 0.6f))
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(Color(0xFF7C3AED).copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Hub, contentDescription = null, tint = Color(0xFFC084FC), modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Autonomous Multi-Agent Fleet Active",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "4 Specialized sub-agents running in parallel",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9E9EB0)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFC084FC)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sub-agent micro status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subAgents.forEach { agent ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF1B1B2C),
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF282840), Color(0xFF202034))))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(if (agent.status == "Active") MyraNeonGreen else Color.Gray, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = agent.name.split(" ").first(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { agent.progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = Color(0xFF7C3AED),
                                trackColor = Color(0xFF28283C)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MayaFeatureCard(
    item: MayaFeatureItem,
    onRunFeature: () -> Unit,
    onOpenConsole: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val statusColor = when {
        item.statusTag.contains("Existing") -> MyraNeonGreen
        item.statusTag.contains("Upgrade") -> Color(0xFFFBBF24)
        else -> Color(0xFFFF6B6B)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("maya_card_${item.id}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF12121E)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(Color(0xFF28283C), Color(0xFF1E1E2E))
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF7C3AED).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF7C3AED).copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getIconForFeature(item.iconName),
                            contentDescription = null,
                            tint = Color(0xFFC084FC),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "#${item.number} • ${item.title}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = item.hindiTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFA5A5C0)
                        )
                    }
                }

                // Status Tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.15f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(statusColor, statusColor.copy(alpha = 0.6f))))
                ) {
                    Text(
                        text = item.statusTag,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC8C8DC),
                lineHeight = 18.sp
            )

            // Category and expand toggles
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF1E1E30)
                ) {
                    Text(
                        text = item.category,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (isExpanded) "Hide Details ▲" else "View Capabilities ▼",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFC084FC)
                    )
                }
            }

            // Expanded Capabilities & Voice Commands
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Divider(color = Color(0xFF222234), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Core Capabilities:",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    item.capabilities.forEach { cap ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = MyraNeonGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = cap, style = MaterialTheme.typography.bodySmall, color = Color(0xFFA5A5C0))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Voice Commands (Voice Trigger):",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    item.voiceCommands.forEach { cmd ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            color = Color(0xFF181826),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "\"$cmd\"", style = MaterialTheme.typography.bodySmall, color = Color(0xFFE2E8F0))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenConsole,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC084FC)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))))
                ) {
                    Icon(Icons.Default.DashboardCustomize, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Interactive Console", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = onRunFeature,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Execute / Test", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

// Dialog: Excel Agent
@Composable
fun ExcelAgentDialog(
    excelCells: List<ExcelCell>,
    onDismiss: () -> Unit,
    onExecuteVoiceFormula: (String) -> Unit
) {
    var formulaInput by remember { mutableStateOf("=SUM(D2:D3)") }
    var selectedCell by remember { mutableStateOf("D4") }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D0D18))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TableChart, contentDescription = null, tint = Color(0xFF10B981))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Excel Voice Agent Console", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Formula Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFF1B1B2C),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "fx [$selectedCell]",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF10B981),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = formulaInput,
                        onValueChange = { formulaInput = it },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFF28283C)
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onExecuteVoiceFormula(formulaInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Text("Apply")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Spreadsheet Grid
                Text("Active Spreadsheet: Sales_Report_2026.xlsx", style = MaterialTheme.typography.labelMedium, color = Color(0xFFA5A5C0))
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = Color(0xFF141424),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFF282840), Color(0xFF1E1E2E))))
                ) {
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        // Header
                        item {
                            Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF1E1E34)).padding(8.dp)) {
                                Text("#", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.width(28.dp))
                                Text("A (Item)", style = MaterialTheme.typography.labelSmall, color = Color.White, modifier = Modifier.weight(1.5f))
                                Text("B (Qty)", style = MaterialTheme.typography.labelSmall, color = Color.White, modifier = Modifier.weight(0.8f))
                                Text("C (Price)", style = MaterialTheme.typography.labelSmall, color = Color.White, modifier = Modifier.weight(1f))
                                Text("D (Total)", style = MaterialTheme.typography.labelSmall, color = Color.White, modifier = Modifier.weight(1.2f))
                            }
                        }

                        val rows = listOf(1, 2, 3, 4)
                        items(rows) { rowNum ->
                            val cellsInRow = excelCells.filter { it.row == rowNum }
                            val colA = cellsInRow.find { it.col == "A" }?.value ?: ""
                            val colB = cellsInRow.find { it.col == "B" }?.value ?: ""
                            val colC = cellsInRow.find { it.col == "C" }?.value ?: ""
                            val colD = cellsInRow.find { it.col == "D" }?.value ?: ""

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCell = "D$rowNum" }
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("$rowNum", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.width(28.dp))
                                Text(colA, style = MaterialTheme.typography.bodySmall, color = Color.White, modifier = Modifier.weight(1.5f))
                                Text(colB, style = MaterialTheme.typography.bodySmall, color = Color(0xFFA5A5C0), modifier = Modifier.weight(0.8f))
                                Text(colC, style = MaterialTheme.typography.bodySmall, color = Color(0xFFA5A5C0), modifier = Modifier.weight(1f))
                                Text(
                                    colD,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (rowNum == 4) Color(0xFF10B981) else Color.White,
                                    fontWeight = if (rowNum == 4) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1.2f)
                                )
                            }
                            HorizontalDivider(color = Color(0xFF1C1C2E))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Voice Assist suggestions
                Text("Voice Commands for Excel:", style = MaterialTheme.typography.labelSmall, color = Color(0xFF10B981))
                Text("• 'Cell B2 me 5 likho aur formula update karo'\n• 'Nayi row add karo Total ke upar'\n• 'Is sheet ka pie chart dashboard banao'", style = MaterialTheme.typography.bodySmall, color = Color(0xFF9E9EB0))
            }
        }
    }
}

// Dialog: AI Coding Agent
@Composable
fun AiCodingAgentDialog(
    onDismiss: () -> Unit,
    onRunPlan: (String) -> Unit
) {
    var promptInput by remember { mutableStateOf("Create a WhatsApp background service with battery optimization and auto-reply") }
    var currentStep by remember { mutableStateOf("Idle / Ready") }
    var terminalOutput by remember { mutableStateOf(
        """
        [AI Coding Agent v2.5]
        > Initialized target project: /app/src/main/
        > Architecture Engine: MVVM + Clean Coroutines
        > Waiting for instruction...
        """.trimIndent()
    ) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B0B14))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFF38BDF8))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Autonomous AI Coding Agent", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Bolo -> Plan -> Code Generation -> Run -> Error Fix Loop",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF38BDF8)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = promptInput,
                    onValueChange = { promptInput = it },
                    label = { Text("What do you want to build / fix?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF38BDF8),
                        unfocusedBorderColor = Color(0xFF28283C)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            currentStep = "Generating Architecture Plan & Code..."
                            terminalOutput = """
                                [AI Coding Agent]
                                > Analyzing request: "$promptInput"
                                > STEP 1: Creating WhatsAppAutomationService.kt
                                > STEP 2: Declaring BIND_ACCESSIBILITY_SERVICE in AndroidManifest.xml
                                > STEP 3: Compiling syntax check... SUCCESS
                                > STEP 4: Running unit tests... 100% Passed.
                                > Build APK Status: Ready!
                            """.trimIndent()
                            onRunPlan(promptInput)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Plan, Code & Run")
                    }

                    Button(
                        onClick = {
                            terminalOutput += "\n> Self-healing loop: Stacktrace parsed. 0 errors remaining."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier.weight(0.8f)
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFF38BDF8))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Auto-Fix Error")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Agent Terminal Execution:", style = MaterialTheme.typography.labelMedium, color = Color(0xFFA5A5C0))
                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = Color(0xFF030712),
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))))
                ) {
                    Text(
                        text = terminalOutput,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = Color(0xFF38BDF8),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

// Dialog: Screen Vision & Live OCR
@Composable
fun ScreenVisionDialog(
    onDismiss: () -> Unit,
    onInspectScreen: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0C0C16))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = Color(0xFFA855F7))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Screen Vision & OCR Inspector", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Simulated Screen Viewfinder
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    color = Color(0xFF141424),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF06B6D4))))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CropFree, contentDescription = null, tint = Color(0xFFA855F7), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Screen Vision HUD Active", style = MaterialTheme.typography.titleSmall, color = Color.White)
                        Text("Extracts UI elements, error dialogs, and reading materials", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onInspectScreen,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7))
                ) {
                    Icon(Icons.Default.CenterFocusStrong, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Inspect Current Screen Now")
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Sample Detected OCR Content:", style = MaterialTheme.typography.labelMedium, color = Color(0xFFA5A5C0))
                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    color = Color(0xFF121220),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("• App Detected: Google Play Store / Settings", style = MaterialTheme.typography.bodySmall, color = MyraNeonGreen)
                        Text("• Error Detected: None (Normal operational viewport)", style = MaterialTheme.typography.bodySmall, color = Color.White)
                        Text("• Voice summary: 'Screen par settings menu khula hua hai jisme Wi-Fi, Bluetooth aur Display options visible hain.'", style = MaterialTheme.typography.bodySmall, color = Color(0xFFC084FC))
                    }
                }
            }
        }
    }
}

// Dialog: Expense & UPI Intelligence
@Composable
fun ExpenseUpiDialog(
    expenses: List<ExpenseItem>,
    onDismiss: () -> Unit,
    onAddExpense: (merchant: String, amount: Double, category: String) -> Unit
) {
    var merchant by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food & Dining") }

    val totalSpending = remember(expenses) { expenses.sumOf { it.amount } }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0C0E14))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color(0xFF10B981))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Expense, UPI & Bank Radar", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Monthly Total Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121E1A)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF059669))))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Month Spending (Parsed from Bank & UPI)", style = MaterialTheme.typography.labelSmall, color = Color(0xFF6EE7B7))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("₹${String.format(java.util.Locale.US, "%,.2f", totalSpending)}", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Top Merchant: Amazon India (₹1,899.00)", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA7F3D0))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Recent Transactions:", style = MaterialTheme.typography.labelMedium, color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(expenses) { exp ->
                        Surface(
                            color = Color(0xFF151822),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(exp.merchant, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("${exp.category} • ${exp.paymentMode}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("₹${exp.amount}", style = MaterialTheme.typography.titleSmall, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                                    Text(exp.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Dialog: Macro Recorder
@Composable
fun MacroRecorderDialog(
    macros: List<MacroItem>,
    isRecording: Boolean,
    onDismiss: () -> Unit,
    onToggleRecording: () -> Unit,
    onRunMacro: (MacroItem) -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0E0B14))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FiberManualRecord, contentDescription = null, tint = if (isRecording) Color.Red else Color(0xFFEC4899))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Macro Recorder Studio", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onToggleRecording,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isRecording) Color(0xFFDC2626) else Color(0xFFEC4899))
                ) {
                    Icon(if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isRecording) "Stop Recording Macro" else "Start New Macro Recording")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Saved Automation Macros:", style = MaterialTheme.typography.labelMedium, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(macros) { macro ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF161220)),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF2E1C38), Color(0xFF1F1828))))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(macro.name, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                    Button(
                                        onClick = { onRunMacro(macro) },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC4899))
                                    ) {
                                        Text("Run Macro", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                                Text(macro.description, style = MaterialTheme.typography.bodySmall, color = Color(0xFFC084FC))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Voice Trigger: \"${macro.triggerVoice}\"", style = MaterialTheme.typography.labelSmall, color = Color(0xFF9E9EB0))
                            }
                        }
                    }
                }
            }
        }
    }
}

// Dialog: Parallel Agents Fleet
@Composable
fun ParallelAgentsFleetDialog(
    subAgents: List<MayaSubAgentStatus>,
    onDismiss: () -> Unit,
    onTriggerFleet: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF090914))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Hub, contentDescription = null, tint = Color(0xFF38BDF8))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Parallel Agent Fleet Deck", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onTriggerFleet,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Trigger Synchronized Fleet Mission")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(subAgents) { agent ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF121422)),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF20243C), Color(0xFF16182C))))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(agent.name, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                    Surface(
                                        color = if (agent.status == "Active") MyraNeonGreen.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = agent.status,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (agent.status == "Active") MyraNeonGreen else Color.Gray,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text("Role: ${agent.role}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF38BDF8))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Current: ${agent.currentTask}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1))
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { agent.progress },
                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                    color = Color(0xFF38BDF8)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Log: ${agent.lastLog}", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Dialog: Stock & Finance
@Composable
fun StockFinanceDialog(
    quotes: List<StockQuote>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0F16))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF10B981))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Stock & Finance Radar", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quotes) { quote ->
                        val isPositive = quote.changePercent >= 0
                        Surface(
                            color = Color(0xFF111827),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(quote.symbol, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text(quote.name, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("₹${quote.price}", style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "${if (isPositive) "+" else ""}${quote.changePercent}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isPositive) MyraNeonGreen else Color(0xFFEF4444),
                                        fontWeight = FontWeight.Bold
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

// Dialog: Persona Switcher
@Composable
fun PersonaSwitchDialog(
    currentPersona: String,
    onSelectPersona: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val personas = listOf(
        Triple("MJ", "Intelligent Cyberpunk Personal Assistant (Balanced, loyal & ultra-fast)", Icons.Default.SmartToy),
        Triple("MAYA", "Tactical & Sweet AI Master (MAYA 2.0 system intelligence)", Icons.Default.AutoAwesome),
        Triple("FRIDAY", "Tony Stark Iron Man style high-tech laboratory assistant", Icons.Default.Security),
        Triple("VENOM", "Aggressive, witty, badass protector persona with punchy humor", Icons.Default.FlashOn)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141424)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Select AI Persona", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(14.dp))

                personas.forEach { (name, desc, icon) ->
                    val isSelected = currentPersona.equals(name, ignoreCase = true)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectPersona(name) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFF7C3AED).copy(alpha = 0.3f) else Color(0xFF1A1A2E),
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFFC084FC)))) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, contentDescription = null, tint = if (isSelected) Color(0xFFC084FC) else Color.Gray)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(name, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(desc, style = MaterialTheme.typography.labelSmall, color = Color(0xFFA5A5C0))
                            }
                        }
                    }
                }
            }
        }
    }
}

// Dialog: Clipboard History
@Composable
fun ClipboardHistoryDialog(
    clipboardItems: List<ClipboardHistoryItem>,
    onDismiss: () -> Unit,
    onClear: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0C0C16))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ContentPaste, contentDescription = null, tint = Color(0xFF38BDF8))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clipboard History Guardian", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onClear,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Clear All Clipboard Items")
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clipboardItems) { item ->
                        Surface(
                            color = Color(0xFF161626),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(item.text, style = MaterialTheme.typography.bodySmall, color = Color.White)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Source: ${item.sourceApp}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Dialog: Generic Feature Detail
@Composable
fun GenericFeatureDetailDialog(
    feature: MayaFeatureItem,
    onDismiss: () -> Unit,
    onExecute: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131322)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(feature.title, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Text(feature.hindiTitle, style = MaterialTheme.typography.labelMedium, color = Color(0xFFC084FC))
                Spacer(modifier = Modifier.height(10.dp))
                Text(feature.description, style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1))
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onExecute,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Execute Now with Voice")
                }
            }
        }
    }
}

private fun getIconForFeature(iconName: String): ImageVector {
    return when (iconName) {
        "Laptop" -> Icons.Default.Laptop
        "Folder" -> Icons.Default.Folder
        "ContentPaste" -> Icons.Default.ContentPaste
        "TableChart" -> Icons.Default.TableChart
        "Visibility" -> Icons.Default.Visibility
        "Chat" -> Icons.Default.Chat
        "Email" -> Icons.Default.Email
        "AccountBalanceWallet" -> Icons.Default.AccountBalanceWallet
        "Devices" -> Icons.Default.Devices
        "Language" -> Icons.Default.Language
        "Description" -> Icons.Default.Description
        "Biotech" -> Icons.Default.Biotech
        "Draw" -> Icons.Default.Draw
        "Videocam" -> Icons.Default.Videocam
        "MicExternalOn" -> Icons.Default.Mic
        "PlayCircle" -> Icons.Default.PlayCircle
        "MusicNote" -> Icons.Default.MusicNote
        "Face" -> Icons.Default.Face
        "TrendingUp" -> Icons.Default.TrendingUp
        "WbSunny" -> Icons.Default.WbSunny
        "Code" -> Icons.Default.Code
        "Hub" -> Icons.Default.Hub
        "Psychology" -> Icons.Default.Psychology
        "Extension" -> Icons.Default.Extension
        "FiberManualRecord" -> Icons.Default.FiberManualRecord
        "Memory" -> Icons.Default.Memory
        "Security" -> Icons.Default.Security
        "RecordVoiceOver" -> Icons.Default.RecordVoiceOver
        "Bedtime" -> Icons.Default.Bedtime
        "Update" -> Icons.Default.Update
        "Home" -> Icons.Default.Home
        "Tune" -> Icons.Default.Tune
        else -> Icons.Default.Star
    }
}
