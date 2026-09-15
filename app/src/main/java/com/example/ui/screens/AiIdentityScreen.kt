package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.viewmodel.MyraViewModel

private val AiIdBackground = Color(0xFF07070B)
private val AiIdCardBg = Color(0xFF0F0F16)
private val AiIdCardBorder = Color(0xFF38121C)
private val AiIdFieldBg = Color(0xFF14141E)
private val AiIdRed = Color(0xFFFF2A55)
private val AiIdCoral = Color(0xFFFF7A59)
private val AiIdLabel = Color(0xFF9E7C85)
private val AiIdDivider = Color(0xFF24141E)
private val AiIdAmber = Color(0xFFFBBF24)
private val AiIdGreen = Color(0xFF22C55E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiIdentityScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val masterName by viewModel.masterName.collectAsState()
    val chatId by viewModel.chatId.collectAsState()
    val communicationLink by viewModel.communicationLink.collectAsState()
    val emergencyContact by viewModel.emergencyContact.collectAsState()
    val sosProtocolEnabled by viewModel.sosProtocolEnabled.collectAsState()
    val subscriptionStatus by viewModel.subscriptionStatus.collectAsState()
    val currentPlan by viewModel.currentPlan.collectAsState()
    val creditsUsed by viewModel.creditsUsed.collectAsState()
    val creditsRemaining by viewModel.creditsRemaining.collectAsState()
    val validTill by viewModel.validTill.collectAsState()
    val deviceSessionId by viewModel.deviceSessionId.collectAsState()
    val deviceModel by viewModel.deviceModel.collectAsState()
    val osVersion by viewModel.osVersion.collectAsState()
    val deviceExtraSlot by viewModel.deviceExtraSlot.collectAsState()
    val deviceRamTotal by viewModel.deviceRamTotal.collectAsState()
    val deviceRamAvailable by viewModel.deviceRamAvailable.collectAsState()
    val deviceBoard by viewModel.deviceBoard.collectAsState()
    val deviceSdkVersion by viewModel.deviceSdkVersion.collectAsState()
    val deviceCpuAbi by viewModel.deviceCpuAbi.collectAsState()
    val referralCode by viewModel.referralCode.collectAsState()
    val isCoreSyncing by viewModel.isCoreSyncing.collectAsState()
    val isCoreOnline by viewModel.isCoreOnline.collectAsState()

    var showEditNameDialog by remember { mutableStateOf(false) }
    var showEditChatIdDialog by remember { mutableStateOf(false) }
    var showEditEmailDialog by remember { mutableStateOf(false) }
    var showEditSosDialog by remember { mutableStateOf(false) }
    var showApplyReferralDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ai_identity_screen"),
        containerColor = AiIdBackground,
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AI IDENTITY",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = AiIdRed
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("btn_ai_identity_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AiIdRed
                        )
                    }
                },
                actions = {
                    Text(
                        text = if (isCoreOnline) "Online" else "Offline",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isCoreOnline) AiIdGreen else Color(0xFFFF8E72),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AiIdBackground
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AiIdBackground)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = { viewModel.syncWithCore() },
                    enabled = !isCoreSyncing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_sync_with_core"),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AiIdRed,
                        disabledContainerColor = Color(0xFF6B1A2A)
                    )
                ) {
                    if (isCoreSyncing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "SYNCING WITH CORE...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "SYNC WITH CORE",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
            // Silhouette Avatar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer glow
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        AiIdRed.copy(alpha = 0.25f),
                                        Color.Transparent
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Inner circle avatar
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF14141E))
                                .border(1.5.dp, Color(0xFF4A1824), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "AI Identity Avatar",
                                tint = Color.White,
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }
                }
            }

            // Main Details Container
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_identity_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AiIdCardBg),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF4A101C), AiIdCardBorder)
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // 1. MASTER NAME
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "MASTER NAME",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AiIdLabel,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showEditNameDialog = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = AiIdRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = masterName.ifBlank { "Deelip Mukhiya" },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // 2. CHAT ID
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "CHAT ID",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AiIdLabel,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = AiIdFieldBg),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = Brush.horizontalGradient(
                                        listOf(Color(0xFF2D1822), Color(0xFF1E1420))
                                    )
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = chatId,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (chatId == "Not set") Color(0xFF8E8E98) else Color.White
                                    )
                                    Text(
                                        text = "CHANGE",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = AiIdCoral,
                                        modifier = Modifier
                                            .clickable { showEditChatIdDialog = true }
                                            .testTag("btn_change_chat_id")
                                    )
                                }
                            }
                        }

                        // 3. COMMUNICATION LINK
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "COMMUNICATION LINK",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AiIdLabel,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showEditEmailDialog = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = AiIdRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = communicationLink.ifBlank { "deelipmukhiya18@gmail.com" },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }

                        // 4. SOS OVERRIDE
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "SOS OVERRIDE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AiIdLabel,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showEditSosDialog = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = AiIdRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = emergencyContact.ifBlank { "Emergency contact..." },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (emergencyContact.isBlank()) Color(0xFF7E7E88) else Color.White
                                )
                            }
                        }

                        HorizontalDivider(color = AiIdDivider, thickness = 1.dp)

                        // 5. ENABLE SOS PROTOCOL Toggle
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ENABLE SOS PROTOCOL",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Switch(
                                    checked = sosProtocolEnabled,
                                    onCheckedChange = { viewModel.toggleSosProtocol(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = AiIdRed,
                                        uncheckedThumbColor = Color(0xFF71717A),
                                        uncheckedTrackColor = Color(0xFF27272A)
                                    ),
                                    modifier = Modifier.testTag("switch_sos_protocol")
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "MJ will broadcast your GPS coordinates to this contact during emergencies.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFA1A1AA),
                                lineHeight = 16.sp
                            )
                        }

                        HorizontalDivider(color = AiIdDivider, thickness = 1.dp)

                        // 6. SUBSCRIPTION STATUS
                        IdentitySectionBox(
                            label = "SUBSCRIPTION STATUS"
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(if (isCoreOnline) AiIdGreen else AiIdAmber)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = subscriptionStatus,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCoreOnline) AiIdGreen else AiIdAmber
                                )
                            }
                        }

                        // 7. CURRENT PLAN
                        IdentitySectionBox(label = "CURRENT PLAN") {
                            Text(
                                text = currentPlan,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AiIdCoral
                            )
                        }

                        // 8. CREDITS USED
                        IdentitySectionBox(label = "CREDITS USED") {
                            Text(
                                text = creditsUsed,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AiIdAmber
                            )
                        }

                        // 9. CREDITS REMAINING
                        IdentitySectionBox(label = "CREDITS REMAINING") {
                            Text(
                                text = creditsRemaining,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AiIdGreen
                            )
                        }

                        // 10. VALID TILL
                        IdentitySectionBox(label = "VALID TILL") {
                            Text(
                                text = validTill,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }

                        // 11. DEVICE & SESSION (Auto-Detected Device Biodata)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "DEVICE & SESSION",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AiIdLabel,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "AUTO-DETECTED",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AiIdGreen,
                                    modifier = Modifier
                                        .clickable { viewModel.refreshDeviceBiodata() }
                                        .testTag("btn_rescan_device_biodata")
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            // Session ID
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = AiIdFieldBg)
                            ) {
                                Text(
                                    text = deviceSessionId,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFC4C4CD),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Device Model
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = AiIdFieldBg)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = deviceModel,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Default.PhoneAndroid,
                                        contentDescription = "Device Hardware",
                                        tint = AiIdCoral,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // OS Version & Extra RAM/Arch Slot Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = AiIdFieldBg)
                                ) {
                                    Text(
                                        text = osVersion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                    )
                                }
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = AiIdFieldBg)
                                ) {
                                    Text(
                                        text = deviceExtraSlot,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (deviceExtraSlot == "--") Color(0xFF8E8E98) else AiIdCoral,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                    )
                                }
                            }
                        }

                        // 12. REFER & EARN
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "REFER & EARN",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AiIdLabel,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Referral Code & Share Row
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = AiIdFieldBg)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = referralCode,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "SHARE",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = AiIdCoral,
                                        modifier = Modifier
                                            .clickable {
                                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(
                                                        Intent.EXTRA_TEXT,
                                                        "Join me on MJ AI Assistant! Use my code: $referralCode to get free VIP credits."
                                                    )
                                                }
                                                context.startActivity(
                                                    Intent.createChooser(shareIntent, "Share MJ Referral Code")
                                                )
                                            }
                                            .testTag("btn_share_referral")
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Share your code - get 1 credit the moment someone new subscribes using it.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFA1A1AA),
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Have referral code? Apply
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Have a referral code?",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                                Text(
                                    text = "APPLY",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = AiIdCoral,
                                    modifier = Modifier
                                        .clickable { showApplyReferralDialog = true }
                                        .testTag("btn_apply_referral")
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialogs for modifying values
    if (showEditNameDialog) {
        var tempName by remember { mutableStateOf(masterName) }
        Dialog(onDismissRequest = { showEditNameDialog = false }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AiIdCardBg),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AiIdRed, AiIdCardBorder)))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Edit Master Name", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Master Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AiIdRed,
                            unfocusedBorderColor = Color(0xFF38121C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showEditNameDialog = false }) {
                            Text("Cancel", color = Color(0xFFA1A1AA))
                        }
                        Button(
                            onClick = {
                                viewModel.updateMasterName(tempName)
                                showEditNameDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AiIdRed)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showEditChatIdDialog) {
        var tempChatId by remember { mutableStateOf(if (chatId == "Not set") "" else chatId) }
        Dialog(onDismissRequest = { showEditChatIdDialog = false }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AiIdCardBg),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AiIdRed, AiIdCardBorder)))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Change Chat ID", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                    OutlinedTextField(
                        value = tempChatId,
                        onValueChange = { tempChatId = it },
                        placeholder = { Text("e.g. @mj_core_01") },
                        label = { Text("Chat ID") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AiIdRed,
                            unfocusedBorderColor = Color(0xFF38121C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showEditChatIdDialog = false }) {
                            Text("Cancel", color = Color(0xFFA1A1AA))
                        }
                        Button(
                            onClick = {
                                viewModel.updateChatId(if (tempChatId.isBlank()) "Not set" else tempChatId)
                                showEditChatIdDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AiIdRed)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showEditEmailDialog) {
        var tempEmail by remember { mutableStateOf(communicationLink) }
        Dialog(onDismissRequest = { showEditEmailDialog = false }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AiIdCardBg),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AiIdRed, AiIdCardBorder)))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Edit Communication Link", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AiIdRed,
                            unfocusedBorderColor = Color(0xFF38121C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showEditEmailDialog = false }) {
                            Text("Cancel", color = Color(0xFFA1A1AA))
                        }
                        Button(
                            onClick = {
                                viewModel.updateCommunicationLink(tempEmail)
                                showEditEmailDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AiIdRed)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showEditSosDialog) {
        var tempSos by remember { mutableStateOf(emergencyContact) }
        Dialog(onDismissRequest = { showEditSosDialog = false }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AiIdCardBg),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AiIdRed, AiIdCardBorder)))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Emergency SOS Contact", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                    OutlinedTextField(
                        value = tempSos,
                        onValueChange = { tempSos = it },
                        placeholder = { Text("e.g. +91 9876543210 or emergency@domain.com") },
                        label = { Text("SOS Contact") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AiIdRed,
                            unfocusedBorderColor = Color(0xFF38121C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showEditSosDialog = false }) {
                            Text("Cancel", color = Color(0xFFA1A1AA))
                        }
                        Button(
                            onClick = {
                                viewModel.updateEmergencyContact(tempSos)
                                showEditSosDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AiIdRed)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showApplyReferralDialog) {
        var inputCode by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showApplyReferralDialog = false }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AiIdCardBg),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AiIdCoral, AiIdCardBorder)))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Apply Referral Code", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                    Text(
                        "Enter a friend's referral code to instantly claim 10 bonus VIP AI credits.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFA1A1AA)
                    )
                    OutlinedTextField(
                        value = inputCode,
                        onValueChange = { inputCode = it },
                        placeholder = { Text("e.g. MJ-STAR99") },
                        label = { Text("Referral Code") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AiIdCoral,
                            unfocusedBorderColor = Color(0xFF38121C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showApplyReferralDialog = false }) {
                            Text("Cancel", color = Color(0xFFA1A1AA))
                        }
                        Button(
                            onClick = {
                                viewModel.applyReferralCode(inputCode)
                                showApplyReferralDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AiIdCoral)
                        ) {
                            Text("Apply", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IdentitySectionBox(
    label: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = AiIdLabel,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = AiIdFieldBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                content()
            }
        }
    }
}
