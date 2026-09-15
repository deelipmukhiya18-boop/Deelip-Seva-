package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PcPowerAction
import com.example.model.PcWindowAction
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PcControlScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected by viewModel.pcBridgeConnected.collectAsState()
    val pcIp by viewModel.pcBridgeIp.collectAsState()
    val pcVolume by viewModel.pcVolume.collectAsState()
    val pcBrightness by viewModel.pcBrightness.collectAsState()
    val activeApp by viewModel.pcActiveApp.collectAsState()
    val runningApps by viewModel.pcRunningApps.collectAsState()
    val terminalHistory by viewModel.terminalHistory.collectAsState()
    val screenAnalysis by viewModel.screenAnalysisText.collectAsState()
    val errorAnalysis by viewModel.errorAnalysisText.collectAsState()
    val isCleaningTemp by viewModel.isCleaningTemp.collectAsState()
    val tempCleaningResult by viewModel.tempCleaningResult.collectAsState()
    val voiceTypingActive by viewModel.voiceTypingActive.collectAsState()
    val clipboardSync by viewModel.pcClipboardSync.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Quick Remote", "Touch & Keys", "PowerShell", "AI Vision & Clean")

    var terminalInput by remember { mutableStateOf("") }
    var voiceInputText by remember { mutableStateOf("") }
    var showPowerConfirmDialog by remember { mutableStateOf<PcPowerAction?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Advanced PC Control",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isConnected) MyraNeonGreen else MyraRed)
                            )
                        }
                        Text(
                            text = if (isConnected) "Connected: $pcIp" else "Disconnected (Tap to retry)",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isConnected) MyraNeonCyan else Color(0xFFA1A1AA)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.togglePcBridge() }) {
                        Icon(
                            imageVector = if (isConnected) Icons.Default.CloudDone else Icons.Default.CloudOff,
                            contentDescription = "Toggle Bridge",
                            tint = if (isConnected) MyraNeonGreen else MyraRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MyraDarkBg)
            )
        },
        containerColor = MyraDarkBg,
        modifier = modifier.testTag("pc_control_screen")
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab Selector Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MyraCardDark,
                contentColor = Color.White,
                edgePadding = 12.dp,
                indicator = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) PurpleAccent else Color.Transparent)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            ),
                            color = if (isSelected) Color.White else Color(0xFFA1A1AA)
                        )
                    }
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> QuickRemoteTab(
                    pcVolume = pcVolume,
                    pcBrightness = pcBrightness,
                    activeApp = activeApp,
                    runningApps = runningApps,
                    clipboardSync = clipboardSync,
                    onVolumeChange = { viewModel.setPcVolume(it) },
                    onBrightnessChange = { viewModel.setPcBrightness(it) },
                    onLaunchApp = { viewModel.launchPcApp(it) },
                    onCloseApp = { viewModel.closePcApp(it) },
                    onWindowAction = { viewModel.controlPcWindow(it) },
                    onToggleClipboard = { viewModel.togglePcClipboardSync() },
                    onRequestPower = { showPowerConfirmDialog = it }
                )
                1 -> TouchAndKeysTab(
                    voiceTypingActive = voiceTypingActive,
                    voiceInputText = voiceInputText,
                    onVoiceTextChange = { voiceInputText = it },
                    onSendVoiceText = {
                        viewModel.sendPcVoiceTyping(voiceInputText)
                        voiceInputText = ""
                    },
                    onToggleVoiceTyping = { viewModel.toggleVoiceTyping() },
                    onMouseAction = { viewModel.sendPcMouseAction(it) },
                    onSendHotkey = { viewModel.sendPcHotkey(it) }
                )
                2 -> PowerShellTerminalTab(
                    terminalHistory = terminalHistory,
                    terminalInput = terminalInput,
                    onInputChange = { terminalInput = it },
                    onExecute = {
                        viewModel.runPowerShellCommand(terminalInput)
                        terminalInput = ""
                    }
                )
                3 -> VisionAndCleanTab(
                    screenAnalysis = screenAnalysis,
                    errorAnalysis = errorAnalysis,
                    isCleaningTemp = isCleaningTemp,
                    tempCleaningResult = tempCleaningResult,
                    onReadScreen = { viewModel.readPcScreen() },
                    onExplainError = { viewModel.explainPcErrorDialog() },
                    onCleanTemp = { viewModel.cleanPcTempFiles() }
                )
            }
        }
    }

    // Power Confirmation Dialog
    showPowerConfirmDialog?.let { action ->
        val actionName = when (action) {
            PcPowerAction.SHUTDOWN -> "Shut Down"
            PcPowerAction.RESTART -> "Restart"
            PcPowerAction.SLEEP -> "Sleep"
            PcPowerAction.LOCK -> "Lock (Win+L)"
        }
        AlertDialog(
            onDismissRequest = { showPowerConfirmDialog = null },
            title = { Text("Confirm PC $actionName", fontWeight = FontWeight.Bold, color = Color.White) },
            text = { Text("Are you sure you want to trigger PC $actionName remotely?", color = Color(0xFFD4D4D8)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.powerPc(action)
                        showPowerConfirmDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (action == PcPowerAction.SHUTDOWN) MyraRed else PurpleAccent
                    )
                ) {
                    Text("Confirm $actionName")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPowerConfirmDialog = null }) {
                    Text("Cancel", color = Color(0xFFA1A1AA))
                }
            },
            containerColor = MyraCardDarkElevated
        )
    }
}

// ---------------- 1. QUICK REMOTE TAB ----------------
@Composable
private fun QuickRemoteTab(
    pcVolume: Int,
    pcBrightness: Int,
    activeApp: String,
    runningApps: List<com.example.model.PcAppInfo>,
    clipboardSync: Boolean,
    onVolumeChange: (Int) -> Unit,
    onBrightnessChange: (Int) -> Unit,
    onLaunchApp: (String) -> Unit,
    onCloseApp: (String) -> Unit,
    onWindowAction: (PcWindowAction) -> Unit,
    onToggleClipboard: () -> Unit,
    onRequestPower: (PcPowerAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Active Window & Desktop Controls
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(PurpleAccent.copy(alpha = 0.5f), MyraNeonCyan.copy(alpha = 0.5f)))
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Active PC Window", style = MaterialTheme.typography.labelMedium, color = Color(0xFFA1A1AA))
                            Text(
                                text = activeApp,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF0F2E22)) {
                            Text(
                                "Live Focus",
                                color = MyraNeonGreen,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Window Controls: Minimize, Maximize, Show Desktop, Close
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ActionButton(label = "Min", icon = Icons.Default.Remove, modifier = Modifier.weight(1f)) {
                            onWindowAction(PcWindowAction.MINIMIZE)
                        }
                        ActionButton(label = "Max", icon = Icons.Default.CropSquare, modifier = Modifier.weight(1f)) {
                            onWindowAction(PcWindowAction.MAXIMIZE)
                        }
                        ActionButton(label = "Desktop", icon = Icons.Default.DesktopWindows, modifier = Modifier.weight(1.2f)) {
                            onWindowAction(PcWindowAction.SHOW_DESKTOP)
                        }
                        ActionButton(label = "Close", icon = Icons.Default.Close, color = MyraRed, modifier = Modifier.weight(1f)) {
                            onWindowAction(PcWindowAction.CLOSE)
                        }
                    }
                }
            }
        }

        // Hardware Controls: Volume & Brightness Sliders
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDark)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Hardware Sliders", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)

                    // Volume
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Volume", tint = MyraNeonCyan, modifier = Modifier.size(18.dp))
                                Text("Master Volume", color = Color(0xFFD4D4D8), style = MaterialTheme.typography.bodyMedium)
                            }
                            Text("$pcVolume%", color = MyraNeonCyan, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = pcVolume.toFloat(),
                            onValueChange = { onVolumeChange(it.toInt()) },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = MyraNeonCyan, activeTrackColor = MyraNeonCyan)
                        )
                    }

                    // Brightness
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.BrightnessMedium, contentDescription = "Brightness", tint = Color(0xFFFBBF24), modifier = Modifier.size(18.dp))
                                Text("Display Brightness", color = Color(0xFFD4D4D8), style = MaterialTheme.typography.bodyMedium)
                            }
                            Text("$pcBrightness%", color = Color(0xFFFBBF24), fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = pcBrightness.toFloat(),
                            onValueChange = { onBrightnessChange(it.toInt()) },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = Color(0xFFFBBF24), activeTrackColor = Color(0xFFFBBF24))
                        )
                    }
                }
            }
        }

        // App Launcher & Running Processes
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDark)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("PC Apps & Launcher", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)

                    runningApps.forEach { app ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (app.isFocused) Color(0xFF1E293B) else Color(0xFF141721))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (app.isRunning) MyraNeonGreen else Color(0xFF52525B))
                                )
                                Column {
                                    Text(app.name, fontWeight = FontWeight.SemiBold, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        if (app.isRunning) "${app.processName} • ${app.memoryUsageMb} MB" else "Not running",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFA1A1AA)
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (app.isRunning) {
                                    FilledTonalButton(
                                        onClick = { onCloseApp(app.id) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF3F1B24))
                                    ) {
                                        Text("Close", color = MyraRed, fontSize = 11.sp)
                                    }
                                } else {
                                    FilledTonalButton(
                                        onClick = { onLaunchApp(app.id) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF1E3A8A))
                                    ) {
                                        Text("Open", color = Color(0xFF60A5FA), fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Power Management: Sleep, Restart, Shutdown, Lock
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDark)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Workstation Power State", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PowerButton(label = "Lock", icon = Icons.Default.Lock, modifier = Modifier.weight(1f)) {
                            onRequestPower(PcPowerAction.LOCK)
                        }
                        PowerButton(label = "Sleep", icon = Icons.Default.Bedtime, modifier = Modifier.weight(1f)) {
                            onRequestPower(PcPowerAction.SLEEP)
                        }
                        PowerButton(label = "Restart", icon = Icons.Default.RestartAlt, modifier = Modifier.weight(1f)) {
                            onRequestPower(PcPowerAction.RESTART)
                        }
                        PowerButton(label = "Shutdown", icon = Icons.Default.PowerSettingsNew, isDanger = true, modifier = Modifier.weight(1f)) {
                            onRequestPower(PcPowerAction.SHUTDOWN)
                        }
                    }
                }
            }
        }
    }
}

// ---------------- 2. TOUCH & KEYS TAB ----------------
@Composable
private fun TouchAndKeysTab(
    voiceTypingActive: Boolean,
    voiceInputText: String,
    onVoiceTextChange: (String) -> Unit,
    onSendVoiceText: () -> Unit,
    onToggleVoiceTyping: () -> Unit,
    onMouseAction: (String) -> Unit,
    onSendHotkey: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Voice Typing Bridge to PC Cursor
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(Color(0xFF8B5CF6), Color(0xFFEC4899)))
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice Typing", tint = if (voiceTypingActive) MyraNeonGreen else Color.White)
                            Text(
                                "Voice Typing Direct to PC Cursor",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Switch(
                            checked = voiceTypingActive,
                            onCheckedChange = { onToggleVoiceTyping() },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF8B5CF6))
                        )
                    }

                    Text(
                        "Dictate notes, code, or messages. Keystrokes are injected directly into your active PC cursor.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFA1A1AA)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = voiceInputText,
                            onValueChange = onVoiceTextChange,
                            placeholder = { Text("Speak or type text for PC...", color = Color(0xFF52525B)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF0F111A),
                                unfocusedContainerColor = Color(0xFF0F111A),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                        IconButton(
                            onClick = onSendVoiceText,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(PurpleAccent)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send text", tint = Color.White)
                        }
                    }
                }
            }
        }

        // Virtual Touchpad Surface
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDark)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Virtual Precision Touchpad", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF090A0F))
                            .border(1.dp, Color(0xFF27272A), RoundedCornerShape(12.dp))
                            .pointerInput(Unit) {
                                detectDragGestures { _, dragAmount ->
                                    // Simulated cursor movement
                                }
                            }
                            .clickable { onMouseAction("LEFT_CLICK") },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.TouchApp, contentDescription = "Touchpad", tint = Color(0xFF52525B), modifier = Modifier.size(32.dp))
                            Text("Drag to move cursor • Tap to Left Click", color = Color(0xFF71717A), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    // Mouse Click Buttons
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onMouseAction("LEFT_CLICK") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                        ) {
                            Text("Left Click")
                        }
                        Button(
                            onClick = { onMouseAction("DOUBLE_CLICK") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                        ) {
                            Text("2x Click")
                        }
                        Button(
                            onClick = { onMouseAction("RIGHT_CLICK") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                        ) {
                            Text("Right Click")
                        }
                    }

                    // Scroll Row
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { onMouseAction("SCROLL_UP") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = "Scroll Up", modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Scroll Up")
                        }
                        OutlinedButton(
                            onClick = { onMouseAction("SCROLL_DOWN") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = "Scroll Down", modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Scroll Down")
                        }
                    }
                }
            }
        }

        // Essential Hotkeys Pad
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDark)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Instant Windows Hotkeys", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)

                    val hotkeys = listOf(
                        "Win+D" to "Show Desktop",
                        "Alt+Tab" to "Switch App",
                        "Ctrl+C" to "Copy",
                        "Ctrl+V" to "Paste",
                        "Ctrl+Z" to "Undo",
                        "Win+E" to "Explorer",
                        "Ctrl+Shift+Esc" to "Taskmgr",
                        "Alt+F4" to "Kill Active"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        hotkeys.chunked(2).forEach { row ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { (key, label) ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF18181B),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF27272A)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { onSendHotkey(key) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(key, fontWeight = FontWeight.Bold, color = MyraNeonCyan, fontSize = 13.sp)
                                            Text(label, color = Color(0xFFA1A1AA), fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- 3. POWERSHELL TERMINAL TAB ----------------
@Composable
private fun PowerShellTerminalTab(
    terminalHistory: List<com.example.model.TerminalLogEntry>,
    terminalInput: String,
    onInputChange: (String) -> Unit,
    onExecute: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Quick Presets Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val presets = listOf("ipconfig", "Get-Process", "systeminfo", "Clear-RecycleBin")
            presets.forEach { cmd ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E293B),
                    modifier = Modifier.clickable { onInputChange(cmd) }
                ) {
                    Text(
                        text = cmd,
                        color = MyraNeonCyan,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Terminal Console Output Window
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1017)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        "Windows PowerShell 7.4.2 Remote Bridge\nCopyright (C) Microsoft Corporation. Connected to MYRA Agent.",
                        color = Color(0xFF64748B),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
                items(terminalHistory) { entry ->
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("PS C:\\Users\\Admin>", color = MyraNeonCyan, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(entry.command, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Text(
                            text = entry.output,
                            color = Color(0xFFCBD5E1),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Command Input Field
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = terminalInput,
                onValueChange = onInputChange,
                placeholder = { Text("Enter PowerShell command...", color = Color(0xFF52525B), fontFamily = FontFamily.Monospace) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF090B10),
                    unfocusedContainerColor = Color(0xFF090B10),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = MyraNeonCyan
                ),
                singleLine = true
            )
            Button(
                onClick = onExecute,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MyraNeonCyan)
            ) {
                Text("Run", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ---------------- 4. AI VISION & CLEAN TAB ----------------
@Composable
private fun VisionAndCleanTab(
    screenAnalysis: String?,
    errorAnalysis: String?,
    isCleaningTemp: Boolean,
    tempCleaningResult: String?,
    onReadScreen: () -> Unit,
    onExplainError: () -> Unit,
    onCleanTemp: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Temporary Files Cleaner Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(Color(0xFF059669), Color(0xFF10B981)))
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Temporary Files & Junk Cleaner", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Text("Cleans %TEMP%, C:\\Windows\\Temp, Prefetch & Recycle Bin", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA1A1AA))
                        }
                        Icon(Icons.Default.CleaningServices, contentDescription = "Clean", tint = Color(0xFF10B981))
                    }

                    tempCleaningResult?.let { res ->
                        Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF064E3B)) {
                            Text(res, color = Color(0xFFA7F3D0), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
                        }
                    }

                    Button(
                        onClick = onCleanTemp,
                        enabled = !isCleaningTemp,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                    ) {
                        if (isCleaningTemp) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                            Text("Purging junk files...")
                        } else {
                            Text("🧹 Temporary Files Clean Karo", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Screen को पढ़ना (Read PC Screen via AI Vision OCR)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDark)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Screen को पढ़ना (AI Vision OCR)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Text("Capture PC screen snapshot & summarize contents", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA1A1AA))
                        }
                        Icon(Icons.Default.Visibility, contentDescription = "Screen Reader", tint = MyraNeonCyan)
                    }

                    screenAnalysis?.let { analysis ->
                        Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF0C2436)) {
                            Text(analysis, color = Color(0xFFBAE6FD), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
                        }
                    }

                    Button(
                        onClick = onReadScreen,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                    ) {
                        Text("🖥️ PC Screen Padho & Samjhao", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Error message पढ़कर समझाना (Explain Error Box)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDark)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Error Message पढ़कर समझाना", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Text("Detect active error dialogue, crash reports & suggest fix", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA1A1AA))
                        }
                        Icon(Icons.Default.Warning, contentDescription = "Error Analyzer", tint = Color(0xFFF59E0B))
                    }

                    errorAnalysis?.let { err ->
                        Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF451A03)) {
                            Text(err, color = Color(0xFFFDE68A), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
                        }
                    }

                    Button(
                        onClick = onExplainError,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                    ) {
                        Text("⚠️ Error Message Scan & Solution", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ---------------- REUSABLE ACTION BUTTONS ----------------
@Composable
private fun ActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E293B),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(18.dp))
            Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun PowerButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDanger: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isDanger) Color(0xFF3F1B24) else Color(0xFF18181B),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = label, tint = if (isDanger) MyraRed else Color.White, modifier = Modifier.size(18.dp))
            Text(label, color = if (isDanger) MyraRed else Color(0xFFE4E4E7), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}
