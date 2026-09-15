package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ConnectorCategory
import com.example.model.ConnectorItem
import com.example.model.ConnectorStatus
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

private val ConnectorCardBg = Color(0xFF141417)
private val ConnectorCardBorder = Color(0xFF222226)
private val IconBoxBg = Color(0xFF26262B)
private val StatusNotConnectedBg = Color(0xFF232328)
private val StatusNotConnectedText = Color(0xFF9E9EA6)
private val StatusErrorBg = Color(0xFF381A1D)
private val StatusErrorText = Color(0xFFFF6F6F)
private val StatusConnectedBg = Color(0xFF1B3B26)
private val StatusConnectedText = Color(0xFF81C784)
private val RedFilterActive = Color(0xFF7A1C1C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectorsScreen(
    viewModel: MyraViewModel,
    connectors: List<ConnectorItem>,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedConnectorForConfig by remember { mutableStateOf<ConnectorItem?>(null) }

    val connectedCount = connectors.count { it.isConnected }
    val totalCount = connectors.size

    val filteredList = connectors.filter { item ->
        val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true) ||
                item.category.displayName.contains(searchQuery, ignoreCase = true) ||
                item.roleBadge.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Cloud Services" -> item.category == ConnectorCategory.CLOUD_SERVICES || item.isLiveConnector
            "Connected" -> item.isConnected
            "Not Connected" -> !item.isConnected
            else -> true
        }
        matchesSearch && matchesFilter
    }

    val categoriesOrder = listOf(
        ConnectorCategory.CLOUD_SERVICES,
        ConnectorCategory.AI_MODELS,
        ConnectorCategory.VOICE_MEDIA,
        ConnectorCategory.CREATIVE,
        ConnectorCategory.PRODUCTIVITY,
        ConnectorCategory.MEDIA,
        ConnectorCategory.AUTOMATION
    )

    Scaffold(
        containerColor = Color(0xFF08080B),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF08080B))
                    .padding(top = 12.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("btn_connectors_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Connectors",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.testTag("title_connectors")
                        )
                        Text(
                            text = "$connectedCount of $totalCount connected",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                            color = Color(0xFF8E8E93)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Search connectors...",
                            color = Color(0xFF7A7A82),
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF7A7A82),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color(0xFF7A7A82),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(52.dp)
                        .testTag("input_search_connectors"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF33333A),
                        unfocusedBorderColor = Color(0xFF222228),
                        focusedContainerColor = Color(0xFF121215),
                        unfocusedContainerColor = Color(0xFF121215),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Filter Pills (All, Cloud Services, Connected, Not Connected)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "Cloud Services", "Connected", "Not Connected").forEach { filter ->
                        val isSelected = selectedFilter == filter
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    selectedFilter = filter
                                    viewModel.performHaptic()
                                }
                                .testTag("filter_$filter"),
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) RedFilterActive else Color(0xFF1C1C20),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) RedFilterActive else Color(0xFF2A2A30)
                            )
                        ) {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isSelected) Color.White else Color(0xFFB0B0B8),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 48.dp)
        ) {
            categoriesOrder.forEach { category ->
                val categoryItems = filteredList.filter { it.category == category }
                if (categoryItems.isNotEmpty()) {
                    item(key = "header_${category.name}") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                        ) {
                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = Color.White
                            )
                            if (category == ConnectorCategory.CLOUD_SERVICES) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF064E3B)
                                ) {
                                    Text(
                                        text = "LIVE OAUTH",
                                        color = Color(0xFF6EE7B7),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    items(categoryItems, key = { it.id }) { connector ->
                        ConnectorItemCard(
                            item = connector,
                            onCardClick = {
                                selectedConnectorForConfig = connector
                                viewModel.performHaptic()
                            },
                            onToggleStatus = {
                                if (connector.isLiveConnector) {
                                    selectedConnectorForConfig = connector
                                } else {
                                    viewModel.toggleConnector(connector.id)
                                }
                                viewModel.performHaptic()
                            }
                        )
                    }
                }
            }

            if (filteredList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = Color(0xFF6B6B75),
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "No connectors found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF8E8E93)
                            )
                        }
                    }
                }
            }
        }
    }

    // Config Bottom Sheets
    selectedConnectorForConfig?.let { connector ->
        if (connector.isLiveConnector) {
            LiveConnectorAuthSheet(
                connector = connector,
                onDismiss = { selectedConnectorForConfig = null },
                onAuthorize = { account ->
                    viewModel.authorizeConnector(connector.id, account)
                    selectedConnectorForConfig = null
                },
                onRevoke = {
                    viewModel.revokeConnector(connector.id)
                    selectedConnectorForConfig = null
                },
                onSync = {
                    viewModel.performHaptic()
                    viewModel.showToast("Syncing ${connector.name} in background...")
                    viewModel.speak("${connector.name} live sync complete ho gaya hai.")
                }
            )
        } else {
            ConnectorConfigSheet(
                connector = connector,
                onDismiss = { selectedConnectorForConfig = null },
                onSaveApiKey = { key ->
                    viewModel.toggleConnector(connector.id, key)
                    selectedConnectorForConfig = null
                    viewModel.showToast("${connector.name} connector updated")
                },
                onTest = {
                    viewModel.performHaptic()
                    viewModel.showToast("Testing connection to ${connector.name}...")
                },
                onTestProjectGen = {
                    selectedConnectorForConfig = null
                    viewModel.generateProject("Autonomous Task Assistant with Jetpack Compose & Cloud Sync")
                }
            )
        }
    }
}

@Composable
private fun ConnectorItemCard(
    item: ConnectorItem,
    onCardClick: () -> Unit,
    onToggleStatus: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onCardClick)
            .testTag("connector_card_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        color = ConnectorCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, ConnectorCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Leading Letter Box
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(IconBoxBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.iconLetter,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ),
                        color = Color.White
                    )
                }

                // Name, Role Badge, and Description
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (item.roleBadge.isNotEmpty()) {
                            val badgeColor = when (item.roleBadge) {
                                "Chat के लिए" -> Color(0xFF0C4A6E)
                                "AI backend" -> Color(0xFF1E3A8A)
                                "Deep Research के लिए" -> Color(0xFF78350F)
                                "generate_project के लिए" -> Color(0xFF4C1D95)
                                "Voice/Media" -> Color(0xFF701A75)
                                "Cloud Service" -> Color(0xFF14532D)
                                else -> Color(0xFF27272A)
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = badgeColor
                            ) {
                                Text(
                                    text = item.roleBadge,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        color = Color(0xFF9E9EA6),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (item.isLiveConnector && item.authorizedAccount != null) {
                        Text(
                            text = "✓ Authorized: ${item.authorizedAccount}",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color(0xFF34D399),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right Status Badge / Button
            val (badgeBg, badgeText, badgeLabel) = when (item.status) {
                ConnectorStatus.CONNECTED -> Triple(StatusConnectedBg, StatusConnectedText, "Connected")
                ConnectorStatus.NEEDS_AUTH -> Triple(Color(0xFF3B240B), Color(0xFFFBBF24), "Authorize")
                ConnectorStatus.ERROR -> Triple(StatusErrorBg, StatusErrorText, "Error")
                ConnectorStatus.NOT_CONNECTED -> Triple(StatusNotConnectedBg, StatusNotConnectedText, "Connect")
            }

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(onClick = onToggleStatus)
                    .testTag("status_badge_${item.id}"),
                shape = RoundedCornerShape(20.dp),
                color = badgeBg
            ) {
                Text(
                    text = badgeLabel,
                    color = badgeText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LiveConnectorAuthSheet(
    connector: ConnectorItem,
    onDismiss: () -> Unit,
    onAuthorize: (String) -> Unit,
    onRevoke: () -> Unit,
    onSync: () -> Unit
) {
    var accountInput by remember {
        mutableStateOf(connector.authorizedAccount ?: "usachannel4859@gmail.com")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF141418),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF3E3E48)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F3E2E)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = connector.iconLetter,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = Color(0xFF6EE7B7)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = connector.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.White
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF14532D)
                        ) {
                            Text(
                                text = "Live Cloud Connector",
                                color = Color(0xFF86EFAC),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Live OAuth Authorization Required",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9EA6)
                    )
                }
            }

            Text(
                text = "PDF Live Connector: Authorize ${connector.name} securely to allow MYRA real-time synchronization with your cloud Workspace.",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = Color(0xFFD6D6DC)
            )

            // Permissions Scope Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1C1C22),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2C35))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Granted Scopes & Capabilities:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    connector.permissionsGranted.forEach { perm ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("•", color = Color(0xFF34D399), fontWeight = FontWeight.Bold)
                            Text(text = perm, color = Color(0xFFC7C7D0), fontSize = 12.sp)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = accountInput,
                onValueChange = { accountInput = it },
                label = { Text("Workspace Account / Email") },
                placeholder = { Text("e.g. user@gmail.com") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_connector_account"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF34D399),
                    unfocusedBorderColor = Color(0xFF2E2E36),
                    focusedContainerColor = Color(0xFF1A1A20),
                    unfocusedContainerColor = Color(0xFF1A1A20),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            if (connector.isConnected) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onSync,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF34D399))
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sync Now")
                    }

                    Button(
                        onClick = onRevoke,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F1D1D))
                    ) {
                        Text("Revoke Access", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Button(
                    onClick = { onAuthorize(accountInput.ifBlank { "usachannel4859@gmail.com" }) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_authorize_connector"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Authorize with ${connector.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConnectorConfigSheet(
    connector: ConnectorItem,
    onDismiss: () -> Unit,
    onSaveApiKey: (String) -> Unit,
    onTest: () -> Unit,
    onTestProjectGen: (() -> Unit)? = null
) {
    var apiKeyText by remember { mutableStateOf(connector.apiKey) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF141418),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF3E3E48)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(IconBoxBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = connector.iconLetter,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Color.White
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = connector.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.White
                        )
                        if (connector.roleBadge.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF374151)
                            ) {
                                Text(
                                    text = connector.roleBadge,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = connector.category.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9EA6)
                    )
                }
            }

            Text(
                text = connector.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFD6D6DC)
            )

            OutlinedTextField(
                value = apiKeyText,
                onValueChange = { apiKeyText = it },
                label = { Text("API Key / Secret Token") },
                placeholder = { Text("Enter secret key...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_connector_key"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyraRed,
                    unfocusedBorderColor = Color(0xFF2E2E36),
                    focusedContainerColor = Color(0xFF1A1A20),
                    unfocusedContainerColor = Color(0xFF1A1A20),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            if ((connector.id == "deepseek" || connector.id == "openrouter") && onTestProjectGen != null) {
                OutlinedButton(
                    onClick = onTestProjectGen,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFA78BFA))
                ) {
                    Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Test 'generate_project' Autonomous Execution", fontSize = 13.sp)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onTest,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Test")
                }

                Button(
                    onClick = { onSaveApiKey(apiKeyText) },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("btn_save_connector"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyraRed)
                ) {
                    Text(
                        text = if (connector.isConnected) "Update Connection" else "Connect Now",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
