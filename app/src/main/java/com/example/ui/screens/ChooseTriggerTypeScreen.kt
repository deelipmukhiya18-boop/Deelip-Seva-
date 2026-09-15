package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TriggerRule
import com.example.viewmodel.MyraViewModel
import java.util.UUID

private val TriggerDarkCanvasBg = Color(0xFF07070B)
private val TriggerDarkCardBg = Color(0xFF10101A)
private val TriggerCardBorder = Color(0xFF1C1C2A)
private val TriggerIconBg = Color(0xFF1F080C)
private val TriggerIconBorder = Color(0xFF330C14)
private val TriggerVibrantRed = Color(0xFFFF002E)
private val TriggerMutedTextColor = Color(0xFF9E9EA8)
private val TriggerChevronColor = Color(0xFF6B6B7B)

data class TriggerTypeItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val defaultCondition: String,
    val defaultAction: String,
    val sampleConditions: List<String>,
    val sampleActions: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTriggerTypeScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTriggerType by remember { mutableStateOf<TriggerTypeItem?>(null) }
    var selectedCondition by remember { mutableStateOf("") }
    var selectedAction by remember { mutableStateOf("") }
    var customTitle by remember { mutableStateOf("") }

    val triggerTypes = remember {
        listOf(
            TriggerTypeItem(
                id = "charging",
                title = "Charging State",
                subtitle = "When the charger goes in or comes out.",
                icon = Icons.Default.BatteryChargingFull,
                defaultCondition = "When charger is plugged in",
                defaultAction = "Speak battery health & charging ETA",
                sampleConditions = listOf(
                    "When charger is plugged in",
                    "When charger is unplugged",
                    "When fast wireless charging starts"
                ),
                sampleActions = listOf(
                    "Speak battery health & charging ETA",
                    "Announce: 'Charging Started, Boss'",
                    "Enable bedside ambient clock & DND"
                )
            ),
            TriggerTypeItem(
                id = "battery_level",
                title = "Battery Level",
                subtitle = "When the battery drops below or climbs above a level.",
                icon = Icons.Default.BatteryAlert,
                defaultCondition = "When battery drops below 20%",
                defaultAction = "Enable battery saver & alert via voice",
                sampleConditions = listOf(
                    "When battery drops below 20%",
                    "When battery reaches 100% full",
                    "When battery drops below 10%"
                ),
                sampleActions = listOf(
                    "Enable battery saver & alert via voice",
                    "Speak: 'Battery full! Please unplug charger.'",
                    "Dim screen brightness & turn off Bluetooth"
                )
            ),
            TriggerTypeItem(
                id = "app_open_close",
                title = "App Opened or Closed",
                subtitle = "When you open or leave any app on the phone.",
                icon = Icons.Default.GridView,
                defaultCondition = "When YouTube or Instagram is opened",
                defaultAction = "Set 30-min mindful screen timer",
                sampleConditions = listOf(
                    "When YouTube or Instagram is opened",
                    "When Google Maps is opened",
                    "When Work Slack or Gmail is closed"
                ),
                sampleActions = listOf(
                    "Set 30-min mindful screen timer",
                    "Proactively prompt: 'Boss, traffic check chahiye?'",
                    "Switch profile to Personal Relax mode"
                )
            ),
            TriggerTypeItem(
                id = "screen_unlock",
                title = "Screen & Unlock",
                subtitle = "When the screen turns on or off, or you unlock the phone.",
                icon = Icons.Default.PhoneAndroid,
                defaultCondition = "When phone is unlocked for the first time in morning",
                defaultAction = "Speak morning briefing & weather summary",
                sampleConditions = listOf(
                    "When phone is unlocked for the first time in morning",
                    "When screen turns off",
                    "When phone is unlocked"
                ),
                sampleActions = listOf(
                    "Speak morning briefing & weather summary",
                    "Clean background cache & lock sensitive apps",
                    "Show unread high-priority notifications summary"
                )
            ),
            TriggerTypeItem(
                id = "headphones",
                title = "Headphones",
                subtitle = "When wired or Bluetooth audio connects or disconnects.",
                icon = Icons.Default.Headphones,
                defaultCondition = "When Bluetooth headphones are connected",
                defaultAction = "Resume Spotify daily mix & announce battery",
                sampleConditions = listOf(
                    "When Bluetooth headphones are connected",
                    "When wired earphones are plugged in",
                    "When headphones disconnect"
                ),
                sampleActions = listOf(
                    "Resume Spotify daily mix & announce battery",
                    "Open Podcast / Audio app",
                    "Pause media playback immediately"
                )
            ),
            TriggerTypeItem(
                id = "wifi",
                title = "Wi-Fi",
                subtitle = "When Wi-Fi connects or disconnects.",
                icon = Icons.Default.Wifi,
                defaultCondition = "When connected to Home Wi-Fi network",
                defaultAction = "Turn on Smart Home lights & welcome home",
                sampleConditions = listOf(
                    "When connected to Home Wi-Fi network",
                    "When disconnected from Home Wi-Fi (leaving home)",
                    "When connected to Office Wi-Fi"
                ),
                sampleActions = listOf(
                    "Turn on Smart Home lights & welcome home",
                    "Lock doors, turn off smart plugs & speak travel ETA",
                    "Set phone to Vibrate mode"
                )
            ),
            TriggerTypeItem(
                id = "scheduled_time",
                title = "Scheduled Time",
                subtitle = "At a specific time, on the days you choose.",
                icon = Icons.Default.AccessTime,
                defaultCondition = "Every weekday at 08:30 AM",
                defaultAction = "Run autonomous morning routine & news digest",
                sampleConditions = listOf(
                    "Every weekday at 08:30 AM",
                    "Every night at 11:00 PM",
                    "Every Sunday at 10:00 AM"
                ),
                sampleActions = listOf(
                    "Run autonomous morning routine & news digest",
                    "Activate sleep mode, dim lights & silent phone",
                    "Generate weekly AI productivity summary"
                )
            ),
            TriggerTypeItem(
                id = "notification",
                title = "Notification",
                subtitle = "When a notification arrives from a chosen app.",
                icon = Icons.Default.Notifications,
                defaultCondition = "When an urgent WhatsApp or Bank SMS arrives",
                defaultAction = "Read sender name and extract OTP/alert",
                sampleConditions = listOf(
                    "When an urgent WhatsApp or Bank SMS arrives",
                    "When VIP contact calls or messages",
                    "When food delivery status updates"
                ),
                sampleActions = listOf(
                    "Read sender name and extract OTP/alert",
                    "Interrupt DND and announce call via voice",
                    "Speak: 'Your delivery rider is nearby!'"
                )
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TriggerDarkCanvasBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.performHaptic()
                        onNavigateBack()
                    },
                    modifier = Modifier.testTag("btn_trigger_type_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Column {
                    Text(
                        text = "Choose Trigger Type",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Select what should start the action",
                        style = MaterialTheme.typography.bodySmall,
                        color = TriggerMutedTextColor
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 48.dp)
        ) {
            items(triggerTypes, key = { it.id }) { item ->
                TriggerTypeCard(
                    item = item,
                    onClick = {
                        viewModel.performHaptic()
                        selectedTriggerType = item
                        customTitle = "${item.title} Rule"
                        selectedCondition = item.defaultCondition
                        selectedAction = item.defaultAction
                    }
                )
            }
        }
    }

    // Modal Sheet / Dialog to configure & save the chosen trigger type
    if (selectedTriggerType != null) {
        val currentItem = selectedTriggerType!!
        AlertDialog(
            onDismissRequest = { selectedTriggerType = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(TriggerIconBg)
                            .border(1.dp, TriggerIconBorder, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = currentItem.icon,
                            contentDescription = null,
                            tint = TriggerVibrantRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Configure Trigger",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = currentItem.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = TriggerVibrantRed
                        )
                    }
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = customTitle,
                        onValueChange = { customTitle = it },
                        label = { Text("Rule Name", color = Color(0xFFA1A1AA)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = TriggerVibrantRed,
                            unfocusedBorderColor = Color(0xFF2E2E3E)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_trigger_title")
                    )

                    // Condition selection
                    Text(
                        text = "WHEN (Condition):",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF7A59)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        currentItem.sampleConditions.forEach { conditionOption ->
                            val isSelected = selectedCondition == conditionOption
                            Surface(
                                onClick = {
                                    selectedCondition = conditionOption
                                    viewModel.performHaptic()
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFF2A0D15) else Color(0xFF161622),
                                border = if (isSelected) CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(TriggerVibrantRed)
                                ) else null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            selectedCondition = conditionOption
                                            viewModel.performHaptic()
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = TriggerVibrantRed,
                                            unselectedColor = Color(0xFF6B6B7B)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = conditionOption,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) Color.White else Color(0xFFCCCCCC)
                                    )
                                }
                            }
                        }
                    }

                    // Action selection
                    Text(
                        text = "THEN (Action to Execute):",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF7A59)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        currentItem.sampleActions.forEach { actionOption ->
                            val isSelected = selectedAction == actionOption
                            Surface(
                                onClick = {
                                    selectedAction = actionOption
                                    viewModel.performHaptic()
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFF2A0D15) else Color(0xFF161622),
                                border = if (isSelected) CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(TriggerVibrantRed)
                                ) else null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            selectedAction = actionOption
                                            viewModel.performHaptic()
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = TriggerVibrantRed,
                                            unselectedColor = Color(0xFF6B6B7B)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = actionOption,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) Color.White else Color(0xFFCCCCCC)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customTitle.isNotEmpty() && selectedCondition.isNotEmpty() && selectedAction.isNotEmpty()) {
                            viewModel.addTrigger(
                                TriggerRule(
                                    id = UUID.randomUUID().toString(),
                                    title = customTitle,
                                    triggerType = currentItem.title,
                                    conditionText = selectedCondition,
                                    actionText = selectedAction,
                                    isEnabled = true
                                )
                            )
                            viewModel.performHaptic()
                            viewModel.showToast("Trigger rule created successfully!")
                            selectedTriggerType = null
                            onNavigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TriggerVibrantRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_save_trigger_rule")
                ) {
                    Text("Save Rule", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { selectedTriggerType = null },
                    modifier = Modifier.testTag("btn_cancel_trigger_dialog")
                ) {
                    Text("Cancel", color = Color(0xFFA1A1AA))
                }
            },
            containerColor = Color(0xFF12121D),
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun TriggerTypeCard(
    item: TriggerTypeItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("card_trigger_type_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TriggerDarkCardBg),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(TriggerCardBorder)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Glowing Red Rounded Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TriggerIconBg)
                    .border(1.dp, TriggerIconBorder, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = TriggerVibrantRed,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Title and description
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TriggerMutedTextColor,
                    lineHeight = 17.sp
                )
            }

            // Chevron Right
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Select",
                tint = TriggerChevronColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
