package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.ConnectorItem
import com.example.ui.theme.*

@Composable
fun VisualLensDialog(
    onDismiss: () -> Unit,
    onAnalyze: (String) -> Unit
) {
    var queryText by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf("See & Analyze") }
    var scanResult by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MyraBlack)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Visual Lens HUD", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Simulated Viewfinder HUD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MyraCardDarkElevated)
                        .border(2.dp, Brush.linearGradient(listOf(MyraRed, MyraNeonCyan)), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = MyraRedGlow, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("AI Visual Scanner Active", style = MaterialTheme.typography.labelLarge, color = Color.White)
                        Text("Point camera or query visual scene", style = MaterialTheme.typography.bodyMedium, color = MyraTextMuted)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("See & Analyze", "Barcode/QR", "Doc OCR", "Product Search").forEach { mode ->
                        FilterChip(
                            selected = selectedMode == mode,
                            onClick = { selectedMode = mode },
                            label = { Text(mode, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MyraRedDark,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = queryText,
                    onValueChange = { queryText = it },
                    placeholder = { Text("Ask about what you are seeing...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MyraRed,
                        unfocusedBorderColor = MyraCardBorder
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                Button(
                    onClick = {
                        scanResult = "Object detected: Modern workspace with monitor, smartphone and ambient RGB backlight. Lighting level optimal."
                        onAnalyze("Visual scan ($selectedMode): $queryText")
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.CenterFocusStrong, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture & Analyze Scene")
                }

                scanResult?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("MYRA Vision Output:", fontWeight = FontWeight.Bold, color = MyraRedGlow)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(it, color = MyraTextPrimary, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JarvisModeDialog(
    onDismiss: () -> Unit,
    onSendCommand: (String) -> Unit
) {
    var pcIp by remember { mutableStateOf("192.168.1.104") }
    var pcPin by remember { mutableStateOf("8421") }
    var isConnected by remember { mutableStateOf(false) }
    var commandText by remember { mutableStateOf("") }
    var logStatus by remember { mutableStateOf("Ready to pair with MYRA PC Companion") }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MyraBlack)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("JARVIS Mode (PC Assistant)", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MyraCardBorderGlow, MyraCardBorder)))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Default.Laptop, contentDescription = null, tint = if (isConnected) MyraNeonGreen else MyraRedGlow)
                            Text(if (isConnected) "PC Connected: Windows 11 Workstation" else "PC Disconnected", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        OutlinedTextField(
                            value = pcIp,
                            onValueChange = { pcIp = it },
                            label = { Text("PC IP Address") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = pcPin,
                            onValueChange = { pcPin = it },
                            label = { Text("4-Digit Security PIN") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                isConnected = !isConnected
                                logStatus = if (isConnected) "Paired successfully with $pcIp" else "Disconnected from PC"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isConnected) Color(0xFF2E7D32) else MyraRed),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (isConnected) "Disconnect PC" else "Connect to Companion PC")
                        }
                    }
                }

                if (isConnected) {
                    Text("Quick Remote Actions", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                onSendCommand("pc_command: lock workstation")
                                logStatus = "Command sent: Lock PC screen"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MyraCardDarkElevated),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Lock PC", style = MaterialTheme.typography.labelSmall)
                        }
                        Button(
                            onClick = {
                                onSendCommand("pc_command: mute audio")
                                logStatus = "Command sent: Mute PC audio"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MyraCardDarkElevated),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Mute PC", style = MaterialTheme.typography.labelSmall)
                        }
                        Button(
                            onClick = {
                                onSendCommand("send_file_to_pc")
                                logStatus = "File transfer channel open"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MyraCardDarkElevated),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Send File", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    OutlinedTextField(
                        value = commandText,
                        onValueChange = { commandText = it },
                        placeholder = { Text("e.g. Open Chrome, launch VS Code...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (commandText.isNotEmpty()) {
                                onSendCommand("PC Exec: $commandText")
                                logStatus = "Executed on PC: $commandText"
                                commandText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Send PC Command")
                    }
                }

                Text(logStatus, color = MyraNeonCyan, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun DeepResearchDialog(
    onDismiss: () -> Unit,
    onRunResearch: (String) -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var researchResult by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MyraBlack)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Deep Research Engine", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Text("Tavily & Multi-Agent web intelligence synthesis", style = MaterialTheme.typography.bodyMedium, color = MyraTextMuted)

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    placeholder = { Text("Enter topic: e.g. Quantum Computing breakthroughs 2026") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Button(
                    onClick = {
                        isRunning = true
                        onRunResearch(topic)
                        researchResult = "Executive Research Brief on: $topic\n\n1. Key Findings: Next-generation topological qubits achieved error mitigation below threshold.\n2. Commercial Timeline: Commercial hybrid cloud deployments scheduled for late 2026.\n3. Impact Assessment: Cryptographic migration protocols mandated across critical financial infrastructures."
                        isRunning = false
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Execute Deep Research")
                }

                researchResult?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Research Synthesis Report", fontWeight = FontWeight.Bold, color = MyraNeonCyan)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(it, color = MyraTextPrimary, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmoledMapDialog(
    onDismiss: () -> Unit
) {
    var searchLocation by remember { mutableStateOf("") }
    var selectedLayer by remember { mutableStateOf("Vector HUD") }
    var isTracking by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2A0008)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Map, contentDescription = null, tint = MyraRedGlow)
                        }
                        Column {
                            Text("AMOLED Map Guide", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Vector GPS Navigation HUD", style = MaterialTheme.typography.bodySmall, color = MyraTextMuted)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchLocation,
                    onValueChange = { searchLocation = it },
                    placeholder = { Text("Search location, coordinates, or POI...", color = MyraTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MyraRedGlow) },
                    trailingIcon = {
                        if (searchLocation.isNotEmpty()) {
                            IconButton(onClick = { searchLocation = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null, tint = Color.White)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0A0A10),
                        unfocusedContainerColor = Color(0xFF0A0A10),
                        focusedBorderColor = MyraRed,
                        unfocusedBorderColor = Color(0xFF202030),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                // Simulated Pitch-Black AMOLED Vector Map Surface
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF050508))
                        .border(1.5.dp, Brush.linearGradient(listOf(MyraRed, Color(0xFF1E1E2E))), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Cyber Grid & Vector Roads Simulation
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val gridStep = 40.dp.toPx()

                        // Grid Lines
                        var x = 0f
                        while (x < w) {
                            drawLine(
                                color = Color(0xFF151522),
                                start = androidx.compose.ui.geometry.Offset(x, 0f),
                                end = androidx.compose.ui.geometry.Offset(x, h),
                                strokeWidth = 1f
                            )
                            x += gridStep
                        }
                        var y = 0f
                        while (y < h) {
                            drawLine(
                                color = Color(0xFF151522),
                                start = androidx.compose.ui.geometry.Offset(0f, y),
                                end = androidx.compose.ui.geometry.Offset(w, y),
                                strokeWidth = 1f
                            )
                            y += gridStep
                        }

                        // Main Vector Route Lines
                        drawLine(
                            brush = Brush.horizontalGradient(listOf(MyraRed, Color(0xFFFF5252))),
                            start = androidx.compose.ui.geometry.Offset(w * 0.2f, h * 0.8f),
                            end = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.5f),
                            strokeWidth = 5.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        drawLine(
                            brush = Brush.horizontalGradient(listOf(Color(0xFFFF5252), MyraNeonCyan)),
                            start = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.5f),
                            end = androidx.compose.ui.geometry.Offset(w * 0.8f, h * 0.25f),
                            strokeWidth = 5.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )

                        // Current User Location Ping
                        drawCircle(
                            color = MyraRed.copy(alpha = 0.3f),
                            radius = 28.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.5f)
                        )
                        drawCircle(
                            color = MyraRedGlow,
                            radius = 10.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.5f)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 4.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.5f)
                        )

                        // Destination Pin
                        drawCircle(
                            color = MyraNeonCyan,
                            radius = 8.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(w * 0.8f, h * 0.25f)
                        )
                    }

                    // Floating Map Overlay Telemetry
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(14.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xCC080810),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262638))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("GPS: 28.6139° N, 77.2090° E", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MyraNeonCyan)
                                Text("Speed: 42 km/h • Heading: 042° NE", style = MaterialTheme.typography.labelSmall, color = Color.White)
                                Text("Vector Waypoint: Alpha Hub (2.4 km)", style = MaterialTheme.typography.labelSmall, color = MyraRedGlow)
                            }
                        }
                    }

                    // Layer Toggles Floating Bottom
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Vector HUD", "Dark Topology", "Satellite Night").forEach { layer ->
                            Surface(
                                onClick = { selectedLayer = layer },
                                shape = RoundedCornerShape(20.dp),
                                color = if (selectedLayer == layer) MyraRed else Color(0xDD12121E),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedLayer == layer) MyraRed else Color(0xFF2A2A3E))
                            ) {
                                Text(
                                    text = layer,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Action Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { isTracking = !isTracking },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isTracking) MyraRed else Color(0xFF1E1E2E))
                    ) {
                        Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isTracking) "Live Tracking ON" else "Start Guide")
                    }

                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF161622))
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Route")
                    }
                }
            }
        }
    }
}

@Composable
fun MissionModeDialog(
    onDismiss: () -> Unit,
    onExecuteMission: (String) -> Unit
) {
    var missionGoal by remember { mutableStateOf("") }
    var autonomyLevel by remember { mutableStateOf("Full Autonomy (5 Iterations)") }
    var isExecuting by remember { mutableStateOf(false) }
    var executionStep by remember { mutableStateOf(0) }
    val missionLogs = remember { mutableStateListOf<String>() }

    val templates = listOf(
        "Generate daily tech & market intelligence summary",
        "Autonomous code refactor & optimization sweep",
        "Monitor system hardware telemetry & notify on threshold",
        "Deep competitor analysis & aesthetic synthesis"
    )

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF07070C))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF330812)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = MyraRedGlow)
                        }
                        Column {
                            Text("Mission Mode", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Autonomous Goal Execution Engine", style = MaterialTheme.typography.bodySmall, color = MyraTextMuted)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Text(
                    text = "Give MJ a high-level goal. The neural agent will break down tasks, execute tools, verify results, and complete the objective autonomously.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB0B0C0)
                )

                // Goal Input
                OutlinedTextField(
                    value = missionGoal,
                    onValueChange = { missionGoal = it },
                    label = { Text("Describe Autonomous Mission Goal") },
                    placeholder = { Text("e.g. Analyze latest AI models and summarize changes...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF10101A),
                        unfocusedContainerColor = Color(0xFF10101A),
                        focusedBorderColor = MyraRed,
                        unfocusedBorderColor = Color(0xFF222234),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                // Goal Templates
                Text("Quick Goal Templates", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    templates.forEach { tmpl ->
                        Surface(
                            onClick = { missionGoal = tmpl },
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF12121E),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF222236))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MyraRedGlow, modifier = Modifier.size(16.dp))
                                Text(tmpl, style = MaterialTheme.typography.bodySmall, color = Color.White, maxLines = 1)
                            }
                        }
                    }
                }

                // Autonomy Level Selector
                Text("Autonomy Constraints", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Supervised", "Autonomous (5 Steps)", "Deep Loop (10 Steps)").forEach { level ->
                        Surface(
                            onClick = { autonomyLevel = level },
                            shape = RoundedCornerShape(12.dp),
                            color = if (autonomyLevel == level) MyraRed else Color(0xFF12121E),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (autonomyLevel == level) MyraRed else Color(0xFF242438)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(modifier = Modifier.fillMaxSize().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                Text(level, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                            }
                        }
                    }
                }

                // Launch Mission Button
                Button(
                    onClick = {
                        val finalGoal = if (missionGoal.isBlank()) "Autonomous diagnostic & intelligence sync" else missionGoal
                        isExecuting = true
                        executionStep = 1
                        missionLogs.clear()
                        missionLogs.add("[1/4] Initiating Autonomous Agent Core...")
                        missionLogs.add("[2/4] Deconstructing goal: '$finalGoal' into sub-tasks...")
                        missionLogs.add("[3/4] Running multi-hop web retrieval & code synthesis...")
                        missionLogs.add("[4/4] Mission Objective Accomplished. Output validated.")
                        onExecuteMission("Mission: $finalGoal")
                        isExecuting = false
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Launch Autonomous Mission", fontWeight = FontWeight.Bold)
                }

                // Real-time Mission Logs
                if (missionLogs.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0C0C14)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MyraRedGlow.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MyraNeonGreen))
                                Text("Autonomous Telemetry Console", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            HorizontalDivider(color = Color(0xFF1E1E2E))
                            missionLogs.forEach { log ->
                                Text(log, style = MaterialTheme.typography.bodySmall, color = MyraNeonGreen)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CodingCoresDialog(
    onDismiss: () -> Unit,
    onGenerateCode: (String) -> Unit
) {
    var codePrompt by remember { mutableStateOf("Write a high-performance Jetpack Compose custom shader button in Kotlin") }
    var selectedLanguage by remember { mutableStateOf("Kotlin") }
    var codeResult by remember {
        mutableStateOf(
            """// Kotlin Jetpack Compose High-Performance Cyber Button
@Composable
fun CyberNeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Neon")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Glow"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF110005),
        border = BorderStroke(2.dp, Color(0xFFFF1133).copy(alpha = glowAlpha)),
        modifier = modifier.height(52.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}"""
        )
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF080810))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2C0510)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = MyraRedGlow)
                        }
                        Column {
                            Text("Coding Cores", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Neural Code Generation & Diagnostics", style = MaterialTheme.typography.bodySmall, color = MyraTextMuted)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Language Selector
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Kotlin", "Python", "Rust", "TypeScript", "C++").forEach { lang ->
                        Surface(
                            onClick = { selectedLanguage = lang },
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedLanguage == lang) MyraRed else Color(0xFF141422),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedLanguage == lang) MyraRed else Color(0xFF242438))
                        ) {
                            Text(
                                text = lang,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Prompt Input
                OutlinedTextField(
                    value = codePrompt,
                    onValueChange = { codePrompt = it },
                    label = { Text("What would you like to code?") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF10101C),
                        unfocusedContainerColor = Color(0xFF10101C),
                        focusedBorderColor = MyraRed,
                        unfocusedBorderColor = Color(0xFF222234),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                // Code View Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF05050A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF202032))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("$selectedLanguage Engine", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MyraNeonCyan)
                            Text("Ready to copy", style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                        }
                        HorizontalDivider(color = Color(0xFF1A1A2A))
                        Text(
                            text = codeResult,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2E8F0),
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }

                // Generate Button
                Button(
                    onClick = {
                        onGenerateCode("Coding Request ($selectedLanguage): $codePrompt")
                        codeResult = "// Generated $selectedLanguage snippet for: $codePrompt\n\nfun executeNeuralTask() {\n    println(\"Executing task with optimal quantum memory efficiency.\")\n}"
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate & Optimize Code", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MyraSpecsDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF07070C))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF330812)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = MyraRedGlow)
                        }
                        Column {
                            Text("MJ System Specs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Architecture & Creator Intelligence", style = MaterialTheme.typography.bodySmall, color = MyraTextMuted)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Dedicated Creator Identity Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF140810)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MyraRedGlow)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = MyraRedGlow)
                            Text("Primary Architect & Master", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MyraRedGlow)
                        }
                        Text("Deelip Mukhiya", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("MJ is custom-engineered and personalized specifically for Deelip Mukhiya, featuring an autonomous neural core, multimodal holographic visualizers, and instant voice telemetry.", style = MaterialTheme.typography.bodySmall, color = Color(0xFFD0D0E0))
                    }
                }

                // Hardware & AI Specifications
                Text("Core Neural Specifications", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)

                val specs = listOf(
                    "AI Foundation Model" to "Gemini 2.5 Flash & Multimodal Quantum Reasoning",
                    "Acoustic Synthesis Engine" to "Dual-Band Neural Speech Pipeline (30 Voice Models)",
                    "Visual Surface Shader" to "Direct GPU Surface Vulkan Raymarching Shaders",
                    "NPU Computing Core" to "32 TOPS Neural Processing Unit • 98% Power Efficiency",
                    "Autonomous Memory Grid" to "Persistent Local Vector Cache & Room Database",
                    "App Build Version" to "v2.5.0 Quantum Cyber Build"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    specs.forEach { (label, value) ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF10101A),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF222234))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(label, style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                                    Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed)
                ) {
                    Text("Close Specs", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun NotificationsCenterDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF07070C))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF330812)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = MyraRedGlow)
                        }
                        Column {
                            Text("Notifications & Alerts", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("3 Unread System Telemetry Alerts", style = MaterialTheme.typography.bodySmall, color = MyraTextMuted)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                val alerts = listOf(
                    Triple("Mission Mode Ready", "Autonomous agent task executor is calibrated and ready to run.", "Just now"),
                    Triple("Voice Models Synced", "All 30 voice model catalogs are active with ultra-low latency playback.", "5m ago"),
                    Triple("Quantum Core Stable", "Neural NPU efficiency at 98%. Battery telemetry nominal.", "15m ago")
                )

                alerts.forEach { (title, desc, time) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF10101A)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF222234))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(MyraRedGlow).padding(top = 4.dp))
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(time, style = MaterialTheme.typography.labelSmall, color = MyraTextMuted)
                                }
                                Text(desc, style = MaterialTheme.typography.bodySmall, color = Color(0xFFB0B0C0))
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed)
                ) {
                    Text("Clear All & Dismiss", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
