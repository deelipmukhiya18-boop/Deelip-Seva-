package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.model.AppScreen
import com.example.model.MissionItem
import com.example.model.TriggerRule
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@Composable
fun MissionsScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTriggers: () -> Unit,
    modifier: Modifier = Modifier
) {
    val missions by viewModel.missions.collectAsState()
    val triggers by viewModel.triggers.collectAsState()
    val isRunningAutonomousTask by viewModel.isRunningAutonomousTask.collectAsState()
    val autonomousTaskStatus by viewModel.autonomousTaskStatus.collectAsState()
    val autonomousTaskProgress by viewModel.autonomousTaskProgress.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Missions, 1: Instant Automations, 2: Habit Learning
    var customMissionInput by remember { mutableStateOf("") }

    val quickAutomations = listOf(
        QuickAutoTask(
            id = "auto_morning",
            title = "Morning Auto-Briefing",
            subtitle = "Mausam, schedule, battery & news auto speak",
            icon = Icons.Default.WbSunny,
            color = Color(0xFFFF9800),
            category = "Daily Routine",
            command = "Morning Briefing chalu karo"
        ),
        QuickAutoTask(
            id = "auto_clean",
            title = "Auto Storage Cleaner",
            subtitle = "Cache scan, temp files clean aur RAM optimize",
            icon = Icons.Default.CleaningServices,
            color = Color(0xFF00E676),
            category = "Device Health",
            command = "Storage clean kar do"
        ),
        QuickAutoTask(
            id = "auto_drive",
            title = "Driving Mode Copilot",
            subtitle = "Google Maps navigation + 75% volume + handsfree",
            icon = Icons.Default.DirectionsCar,
            color = Color(0xFF2979FF),
            category = "Maps & Travel",
            command = "Driving mode on kar do"
        ),
        QuickAutoTask(
            id = "auto_battery",
            title = "Battery Shield & Save",
            subtitle = "Power save mode on + dim brightness + apps optimize",
            icon = Icons.Default.BatteryChargingFull,
            color = Color(0xFF00B0FF),
            category = "System Care",
            command = "Battery kitni hai"
        ),
        QuickAutoTask(
            id = "auto_night",
            title = "Night Sleep Routine",
            subtitle = "DND activate + 6 AM alarm + screen dimming",
            icon = Icons.Default.Bedtime,
            color = Color(0xFF7C4DFF),
            category = "Bedtime",
            command = "Subah 6 baje alarm laga do"
        ),
        QuickAutoTask(
            id = "auto_sos",
            title = "Emergency SOS Dispatch",
            subtitle = "Location coordinates ke sath trusted alert bhejna",
            icon = Icons.Default.Warning,
            color = MyraRed,
            category = "Safety Guard",
            command = "SOS alert bhej do"
        ),
        QuickAutoTask(
            id = "auto_screen",
            title = "Screen Automation Task",
            subtitle = "Screen read, visual check & background agent task",
            icon = Icons.Default.TouchApp,
            color = Color(0xFFFF4081),
            category = "Screen AI",
            command = "Screen automation task start karo"
        ),
        QuickAutoTask(
            id = "auto_research",
            title = "Autonomous Web Research",
            subtitle = "Deep web search, multi-source extraction & summary",
            icon = Icons.Default.TravelExplore,
            color = Color(0xFF00E5FF),
            category = "Generative AI",
            command = "Deep research request: AI developments in 2026"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MyraDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // 1. Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            "Autonomous Missions & Automation",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "MJ Automatic Multi-Step Execution Hub",
                            style = MaterialTheme.typography.labelSmall,
                            color = MyraRedGlow
                        )
                    }
                }
            }
        }

        // Live Autonomous Task Running Banner
        if (isRunningAutonomousTask) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E0E18)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(MyraRed, MyraNeonCyan))
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MyraNeonCyan,
                                    strokeWidth = 2.5.dp
                                )
                                Text(
                                    "MJ Autonomous Mission Running...",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                            IconButton(
                                onClick = { viewModel.cancelAutonomousTask() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Cancel, contentDescription = "Cancel", tint = MyraRed)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { autonomousTaskProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = MyraRedGlow,
                            trackColor = Color(0xFF281C28)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = autonomousTaskStatus,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2E2F0),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 2. Tab Selectors (Missions, Instant Automations, Habit AI)
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF14141E),
                contentColor = MyraRed,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MyraRedGlow,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MyraCardBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        viewModel.performHaptic()
                    },
                    text = {
                        Text(
                            "Autonomous Missions",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (selectedTab == 0) Color.White else Color(0xFF8E8E9E)
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        viewModel.performHaptic()
                    },
                    text = {
                        Text(
                            "Instant Tasks (15x)",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (selectedTab == 1) Color.White else Color(0xFF8E8E9E)
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        viewModel.performHaptic()
                    },
                    text = {
                        Text(
                            "Habit AI & Rules",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (selectedTab == 2) Color.White else Color(0xFF8E8E9E)
                        )
                    }
                )
            }
        }

        // TAB 0: AUTONOMOUS MISSIONS
        if (selectedTab == 0) {
            // Launch Custom Mission Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(MyraCardBorderGlow, MyraCardBorder))
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MyraRedGlow, modifier = Modifier.size(20.dp))
                            Text("Launch Any Autonomous Goal", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "MJ multi-step planning, automated tool calls, aur device actions execute karegi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = customMissionInput,
                                onValueChange = { customMissionInput = it },
                                placeholder = { Text("e.g. Plan my trip to Goa, clean cache, setup morning routine...", color = Color(0xFF6E6E82), fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MyraRed,
                                    unfocusedBorderColor = Color(0xFF26263A),
                                    focusedContainerColor = Color(0xFF0F0F16),
                                    unfocusedContainerColor = Color(0xFF0F0F16),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    if (customMissionInput.isNotBlank()) {
                                        viewModel.executeAutonomousMission(customMissionInput)
                                        customMissionInput = ""
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MyraRed)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Start", tint = Color.White)
                            }
                        }
                    }
                }
            }

            // Mission Cards List
            items(missions, key = { it.id }) { mission ->
                MissionCardItem(
                    mission = mission,
                    onPauseResume = { viewModel.toggleMissionStatus(mission.id) },
                    onCancel = { viewModel.cancelMission(mission.id) },
                    onExecute = { viewModel.executeAutonomousMission(mission.title) }
                )
            }
        }

        // TAB 1: INSTANT 15-CATEGORY AUTOMATIONS
        if (selectedTab == 1) {
            item {
                Text(
                    "One-Tap Automatic Actions (15 All-Inclusive Tools)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            items(quickAutomations, key = { it.id }) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.performHaptic()
                            viewModel.executeAutonomousMission(task.command)
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(task.color.copy(alpha = 0.6f), MyraCardBorder))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(task.color.copy(alpha = 0.15f))
                                    .border(1.dp, task.color.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(task.icon, contentDescription = null, tint = task.color, modifier = Modifier.size(24.dp))
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(task.title, fontWeight = FontWeight.Bold, color = Color.White)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFF1F1F2C))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(task.category, fontSize = 10.sp, color = task.color, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(task.subtitle, style = MaterialTheme.typography.bodySmall, color = MyraTextSecondary, fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.performHaptic()
                                viewModel.executeAutonomousMission(task.command)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = task.color.copy(alpha = 0.85f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("RUN AUTO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // TAB 2: HABIT LEARNING & CONDITIONAL TRIGGER RULES
        if (selectedTab == 2) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1A15)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(Color(0xFF00E676), Color(0xFF00B0FF)))
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(22.dp))
                            Text("Habit Learning AI (Autonomous Engine)", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "MJ aapke daily routines seekhkar automatically suggest aur execute karti hai bina aapke bolne ke pehle.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFC8E6C9)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Learned Habits Active: 4", color = Color(0xFF81C784), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Button(
                                onClick = onNavigateToTriggers,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("Manage Triggers", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Active Trigger Automations (\"Agar X ho to Y karo\")",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            items(triggers, key = { it.id }) { trigger ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            listOf(if (trigger.isEnabled) MyraCardBorderGlow else MyraCardBorder, MyraCardBorder)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(trigger.title, fontWeight = FontWeight.Bold, color = Color.White)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MyraCardDark)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                    Text(trigger.triggerType, style = MaterialTheme.typography.labelSmall, color = MyraNeonCyan)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("WHEN: ${trigger.conditionText}", style = MaterialTheme.typography.bodySmall, color = MyraTextSecondary)
                            Text("THEN: ${trigger.actionText}", style = MaterialTheme.typography.bodySmall, color = MyraRedGlow)
                        }

                        Switch(
                            checked = trigger.isEnabled,
                            onCheckedChange = {
                                viewModel.toggleTrigger(trigger.id)
                                viewModel.performHaptic()
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MyraRed)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MissionCardItem(
    mission: MissionItem,
    onPauseResume: () -> Unit,
    onCancel: () -> Unit,
    onExecute: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(
                    when (mission.status) {
                        "Active" -> MyraNeonCyan
                        "Completed" -> Color(0xFF00E676)
                        else -> Color(0xFFFFB300)
                    },
                    MyraCardBorder
                )
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mission.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (mission.status) {
                        "Active" -> Color(0xFF004D40)
                        "Completed" -> Color(0xFF1B5E20)
                        else -> Color(0xFF422700)
                    }
                ) {
                    Text(
                        text = mission.status.uppercase(),
                        color = when (mission.status) {
                            "Active" -> MyraNeonCyan
                            "Completed" -> Color(0xFF69F0AE)
                            else -> Color(0xFFFFD54F)
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { mission.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when (mission.status) {
                    "Completed" -> Color(0xFF00E676)
                    else -> MyraNeonCyan
                },
                trackColor = Color(0xFF1E1E2C)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Current Step: ${mission.currentStep}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC0C0D4)
            )

            if (mission.logs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0C0C14))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    mission.logs.takeLast(3).forEach { log ->
                        Text(
                            text = "• $log",
                            fontSize = 11.sp,
                            color = Color(0xFF9E9EB4)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onExecute,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Auto Run", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onPauseResume,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text(if (mission.status == "Active") "Pause" else "Resume", fontSize = 12.sp)
                }

                IconButton(
                    onClick = onCancel,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFF8E8E9E))
                }
            }
        }
    }
}

data class QuickAutoTask(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val category: String,
    val command: String
)
