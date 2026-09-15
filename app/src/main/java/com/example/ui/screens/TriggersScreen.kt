package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.TriggerRule
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel
import java.util.UUID

@Composable
fun TriggersScreen(
    viewModel: MyraViewModel,
    triggers: List<TriggerRule>,
    onNavigateBack: () -> Unit,
    onNavigateToChooseTrigger: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newCondition by remember { mutableStateOf("") }
    var newAction by remember { mutableStateOf("") }
    var newType by remember { mutableStateOf("Scheduled Time") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MyraDarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
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
                        Text("Automation & Triggers", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("\"Agar X ho to Y karo\" Rules", style = MaterialTheme.typography.labelSmall, color = MyraRedGlow)
                    }
                }
                IconButton(
                    onClick = {
                        viewModel.performHaptic()
                        onNavigateToChooseTrigger()
                    },
                    modifier = Modifier.testTag("btn_add_trigger")
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Add Trigger", tint = MyraRed, modifier = Modifier.size(28.dp))
                }
            }
        }

        // Habit Tracking Banner (from PDF page 11)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MyraCardDarkElevated),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MyraCardBorderGlow, MyraCardBorder)))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MyraNeonGreen, modifier = Modifier.size(18.dp))
                        Text("Habit Tracking (Autonomous AI)", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "MYRA aapke daily patterns seekhti hai (jaise roz subah Maps kholna) aur proactively prompt karti hai: \"Boss, aap roughly is time Maps kholte ho, kuch chahiye?\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MyraTextSecondary
                    )
                }
            }
        }

        items(triggers, key = { it.id }) { trigger ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Create New Trigger Rule", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Rule Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCondition,
                        onValueChange = { newCondition = it },
                        label = { Text("Agar (Condition)") },
                        placeholder = { Text("e.g. Headphones plug hone pe") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newAction,
                        onValueChange = { newAction = it },
                        label = { Text("To Ye Karo (Action)") },
                        placeholder = { Text("e.g. Spotify pe gaana shuru karo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotEmpty() && newCondition.isNotEmpty()) {
                            viewModel.addTrigger(
                                TriggerRule(UUID.randomUUID().toString(), newTitle, newType, newCondition, newAction, true)
                            )
                            showAddDialog = false
                            newTitle = ""
                            newCondition = ""
                            newAction = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed)
                ) {
                    Text("Save Rule")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = MyraTextMuted)
                }
            },
            containerColor = MyraCardDarkElevated
        )
    }
}
