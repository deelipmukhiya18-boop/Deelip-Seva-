package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MyraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepResearchSettingsScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tavilyKeyFromVm by viewModel.tavilyApiKey.collectAsState()
    val tavilyUrlFromVm by viewModel.tavilyCustomUrl.collectAsState()
    val tavilyStatusFromVm by viewModel.tavilyStatus.collectAsState()
    val isTesting by viewModel.isTestingTavily.collectAsState()

    var tavilyApiKey by remember(tavilyKeyFromVm) { mutableStateOf(tavilyKeyFromVm) }
    var tavilyCustomUrl by remember(tavilyUrlFromVm) { mutableStateOf(tavilyUrlFromVm) }

    val statusColor = when {
        tavilyStatusFromVm.contains("Connected") || tavilyStatusFromVm.contains("Ready") -> Color(0xFF34D399)
        tavilyStatusFromVm.contains("Failed") -> Color(0xFFF87171)
        else -> Color(0xFFFB923C) // Coral orange for Not Configured
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Deep Research Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .testTag("deep_research_back_btn")
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkCardBg,
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(DarkBorderColor)
                            ),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset",
                                    tint = Color(0xFFA1A1AA),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkScreenBg)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("deep_research_settings_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Card Container matching screenshot
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = DarkCardBg,
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(DarkBorderColor)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // PROVIDER
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "PROVIDER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 1.sp,
                                fontSize = 11.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9CA3AF)
                        )
                        Text(
                            text = "Tavily AI (Recommended)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // API KEY
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "API KEY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 1.sp,
                                fontSize = 11.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9CA3AF)
                        )
                        OutlinedTextField(
                            value = tavilyApiKey,
                            onValueChange = { tavilyApiKey = it },
                            placeholder = {
                                Text(
                                    text = "Enter Tavily API Key",
                                    color = Color(0xFF52525B),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_tavily_key"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = PurpleAccent,
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    // CUSTOM API URL (OPTIONAL)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "CUSTOM API URL (OPTIONAL)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 1.sp,
                                fontSize = 11.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9CA3AF)
                        )
                        OutlinedTextField(
                            value = tavilyCustomUrl,
                            onValueChange = { tavilyCustomUrl = it },
                            placeholder = {
                                Text(
                                    text = "https://api.tavily.com/search",
                                    color = Color(0xFF52525B),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_tavily_url"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkInputFieldBg,
                                unfocusedContainerColor = DarkInputFieldBg,
                                focusedBorderColor = PurpleAccent,
                                unfocusedBorderColor = DarkBorderColor,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    // Status Indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Status: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = tavilyStatusFromVm,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Test Connection Button
            Surface(
                onClick = { viewModel.testTavilyConnection(tavilyApiKey, tavilyCustomUrl) },
                shape = RoundedCornerShape(14.dp),
                color = DarkCardBg,
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(DarkBorderColor)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_test_tavily_connection")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isTesting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Test Connection",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Save Settings Button
            Button(
                onClick = {
                    viewModel.saveTavilySettings(tavilyApiKey, tavilyCustomUrl)
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_save_tavily_settings"),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleButtonBg),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Save Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
