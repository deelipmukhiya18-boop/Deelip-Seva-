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
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallSmsAnnouncerScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val incomingCallAnnouncerEnabled by viewModel.incomingCallAnnouncerEnabled.collectAsState()
    val incomingSmsAnnouncerEnabled by viewModel.incomingSmsAnnouncerEnabled.collectAsState()
    val incomingNotificationAnnouncerEnabled by viewModel.incomingNotificationAnnouncerEnabled.collectAsState()
    val readFullSmsContent by viewModel.readFullSmsContent.collectAsState()
    val announceUnknownNumbers by viewModel.announceUnknownNumbers.collectAsState()
    val repeatCount by viewModel.repeatCallAnnouncementCount.collectAsState()

    val currentIncomingCall by viewModel.currentIncomingCall.collectAsState()
    val currentIncomingSms by viewModel.currentIncomingSms.collectAsState()

    var quickReplyText by remember { mutableStateOf("") }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_announcer")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Call & SMS Announcer",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Surface(
                                color = if (incomingCallAnnouncerEnabled || incomingSmsAnnouncerEnabled) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (incomingCallAnnouncerEnabled || incomingSmsAnnouncerEnabled) Color(0xFF10B981) else Color(0xFFEF4444))
                            ) {
                                Text(
                                    text = if (incomingCallAnnouncerEnabled || incomingSmsAnnouncerEnabled) "VOICE ACTIVE" else "MUTED",
                                    color = if (incomingCallAnnouncerEnabled || incomingSmsAnnouncerEnabled) Color(0xFF10B981) else Color(0xFFEF4444),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Automatic Caller ID & SMS Voice Reading",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0A0B0)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.performHaptic()
                        onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0C0C12))
            )
        },
        containerColor = Color(0xFF0C0C12)
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // LIVE INCOMING CALL OVERLAY HUD BANNER
            if (currentIncomingCall != null) {
                item {
                    val call = currentIncomingCall!!
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .testTag("incoming_call_active_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1128)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFF10B981).copy(alpha = pulseGlow), MyraRedGlow.copy(alpha = pulseGlow))
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(if (call.isSpam) Color(0xFFEF4444).copy(alpha = 0.3f) else Color(0xFF10B981).copy(alpha = 0.3f))
                                    .border(2.dp, if (call.isSpam) Color(0xFFEF4444) else Color(0xFF10B981), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneInTalk,
                                    contentDescription = null,
                                    tint = if (call.isSpam) Color(0xFFEF4444) else Color(0xFF10B981),
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Text(
                                text = if (call.isSpam) "⚠️ Suspected Spam Call" else "Incoming Call...",
                                color = if (call.isSpam) Color(0xFFEF4444) else Color(0xFF34D399),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = call.callerName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )

                            Text(
                                text = call.callerNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFA5B4FC)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.rejectIncomingCall() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).testTag("btn_reject_call")
                                ) {
                                    Icon(Icons.Default.CallEnd, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Reject", color = Color.White, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { viewModel.answerIncomingCall() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).testTag("btn_answer_call")
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Answer", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // LIVE INCOMING SMS OVERLAY HUD BANNER
            if (currentIncomingSms != null) {
                item {
                    val sms = currentIncomingSms!!
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .testTag("incoming_sms_active_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF141A2E)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFF38BDF8).copy(alpha = pulseGlow), Color(0xFF818CF8).copy(alpha = pulseGlow))
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(
                                        imageVector = if (sms.isOtp) Icons.Default.VpnKey else Icons.Default.Chat,
                                        contentDescription = null,
                                        tint = if (sms.isOtp) Color(0xFFFBBF24) else Color(0xFF38BDF8),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (sms.isOtp) "🔐 Bank OTP Received" else "💬 New SMS Received",
                                        color = if (sms.isOtp) Color(0xFFFBBF24) else Color(0xFF38BDF8),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.dismissIncomingSms() },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = sms.senderName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = sms.senderNumber,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFA5B4FC)
                                )
                            }

                            Surface(
                                color = Color(0xFF0F172A),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = sms.messageBody,
                                    color = Color(0xFFE2E8F0),
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = quickReplyText,
                                    onValueChange = { quickReplyText = it },
                                    placeholder = { Text("Quick voice/text reply...", color = Color.Gray, fontSize = 12.sp) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF38BDF8),
                                        unfocusedBorderColor = Color(0xFF334155),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).testTag("input_quick_sms_reply")
                                )
                                Button(
                                    onClick = {
                                        if (quickReplyText.isNotBlank()) {
                                            viewModel.replyToSms(quickReplyText)
                                            quickReplyText = ""
                                        } else {
                                            viewModel.replyToSms("Thik hai, got it.")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            // 1. MASTER VOICE ANNOUNCER OVERVIEW
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .testTag("announcer_master_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161426)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF38BDF8).copy(alpha = 0.5f), MyraRedGlow.copy(alpha = 0.5f))
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFF0284C7).copy(alpha = 0.2f))
                                    .border(1.dp, Color(0xFF38BDF8), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Hands-Free Voice Caller ID & SMS",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "MJ announces who is calling and speaks your messages out loud.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFCBD5E1)
                                )
                            }
                        }

                        Divider(color = Color(0xFF26263A), thickness = 1.dp)

                        Text(
                            text = "Aapka phone pocket ya door rakha ho, MJ turant bolkar batayegi: 'Rahul ji ka phone aa raha hai' ya 'Suresh se naya SMS aaya hai'.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = Color(0xFFA5B4FC)
                        )
                    }
                }
            }

            // 2. CORE ANNOUNCEMENT SWITCHES
            item {
                Text(
                    text = "Voice Alert Preferences",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // 1. Incoming Call Announcer
                    AnnouncerSettingCard(
                        title = "Incoming Call Announcer (Caller ID)",
                        subtitle = "Speaks caller's contact name or number aloud when phone rings.",
                        icon = Icons.Default.PhoneInTalk,
                        iconTint = Color(0xFF34D399),
                        isChecked = incomingCallAnnouncerEnabled,
                        onToggle = { viewModel.toggleIncomingCallAnnouncer() },
                        badge = "CALLER ID VOICE"
                    )

                    // 2. Incoming SMS Announcer
                    AnnouncerSettingCard(
                        title = "Incoming SMS Voice Reader",
                        subtitle = "Announces who sent the SMS and reads message content aloud.",
                        icon = Icons.Default.ChatBubble,
                        iconTint = Color(0xFF38BDF8),
                        isChecked = incomingSmsAnnouncerEnabled,
                        onToggle = { viewModel.toggleIncomingSmsAnnouncer() },
                        badge = "SMS VOICE READER"
                    )

                    // 3. Read Full Message Body
                    AnnouncerSettingCard(
                        title = "Read Full SMS Body Content",
                        subtitle = "Speaks the complete message text rather than just sender name.",
                        icon = Icons.Default.Subject,
                        iconTint = Color(0xFFFBBF24),
                        isChecked = readFullSmsContent,
                        onToggle = { viewModel.toggleReadFullSms() },
                        badge = "FULL TEXT"
                    )

                    // 4. WhatsApp & App Notifications Announcer
                    AnnouncerSettingCard(
                        title = "WhatsApp & App Notifications Announcer",
                        subtitle = "Announces incoming messages from WhatsApp, Telegram, Gmail & Bank alerts.",
                        icon = Icons.Default.NotificationsActive,
                        iconTint = Color(0xFFC084FC),
                        isChecked = incomingNotificationAnnouncerEnabled,
                        onToggle = { viewModel.toggleIncomingNotificationAnnouncer() },
                        badge = "ALL APPS"
                    )

                    // 5. Announce Unknown Numbers
                    AnnouncerSettingCard(
                        title = "Announce Unknown Phone Numbers",
                        subtitle = "Reads unknown caller digits out loud when not saved in contacts.",
                        icon = Icons.Default.PermPhoneMsg,
                        iconTint = Color(0xFFF472B6),
                        isChecked = announceUnknownNumbers,
                        onToggle = { viewModel.toggleAnnounceUnknownNumbers() },
                        badge = "UNKNOWN CALLS"
                    )
                }
            }

            // 3. REPEAT ANNOUNCEMENT COUNT
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141424)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(Color(0xFF2E2038), Color(0xFF1E1E2C)))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Repeat Call Announcement",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$repeatCount Times",
                                style = MaterialTheme.typography.bodySmall,
                                color = MyraRedGlow,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val countOptions = listOf(1, 2, 3, 5, 10)
                            items(countOptions) { count ->
                                val isSelected = repeatCount == count
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.setRepeatCallAnnouncementCount(count) },
                                    color = if (isSelected) MyraRed.copy(alpha = 0.3f) else Color(0xFF1E1E2C),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) MyraRedGlow else Color(0xFF333348)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "$count x",
                                        color = if (isSelected) Color.White else Color(0xFFA0A0B0),
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. INTERACTIVE TEST LAB / LIVE SIMULATOR
            item {
                Text(
                    text = "Live Simulation & Audio Test Lab",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF13131F)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(Color(0xFF2E2038), Color(0xFF1E1E2C)))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Test hearing MJ speak incoming alerts live:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0A0B0)
                        )

                        // 1. Test Call from Papa
                        Button(
                            onClick = {
                                viewModel.triggerIncomingCall("Papa", "+91 98765 43210")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF065F46)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("btn_test_call_papa")
                        ) {
                            Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = Color(0xFF6EE7B7), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("📞 Test Call: 'Papa (+91 98765 43210)'", color = Color.White, fontSize = 13.sp)
                        }

                        // 2. Test Call from Unknown / Spam
                        Button(
                            onClick = {
                                viewModel.triggerIncomingCall("Unknown Caller", "+91 88001 23456", isSpam = true)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF450A0A)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("btn_test_call_spam")
                        ) {
                            Icon(Icons.Default.ReportProblem, contentDescription = null, tint = Color(0xFFFCA5A5), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("⚠️ Test Call: 'Spam / Unknown Caller'", color = Color.White, fontSize = 13.sp)
                        }

                        // 3. Test SMS from Friend
                        Button(
                            onClick = {
                                viewModel.triggerIncomingSms(
                                    senderName = "Rohan Verma",
                                    senderNumber = "+91 98111 22233",
                                    messageText = "Bhai shaam ko 6 baje ground pe milte hain match ke liye."
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF075985)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("btn_test_sms_friend")
                        ) {
                            Icon(Icons.Default.ChatBubble, contentDescription = null, tint = Color(0xFF7DD3FC), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("💬 Test SMS: 'Rohan Verma (Shaam ko milte hain)'", color = Color.White, fontSize = 13.sp)
                        }

                        // 4. Test Bank OTP SMS
                        Button(
                            onClick = {
                                viewModel.triggerIncomingSms(
                                    senderName = "HDFC Bank",
                                    senderNumber = "AD-HDFCBK",
                                    messageText = "Your OTP for transaction of Rs 2,500 is 849201. Valid for 10 mins.",
                                    isOtp = true,
                                    otpCode = "849201"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78350F)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("btn_test_sms_otp")
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, tint = Color(0xFFFCD34D), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("🔐 Test Bank OTP: 'HDFC Bank OTP 849201'", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnnouncerSettingCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    isChecked: Boolean,
    onToggle: () -> Unit,
    badge: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141420)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(
                    if (isChecked) iconTint.copy(alpha = 0.5f) else Color(0xFF2A2A3A),
                    Color(0xFF1A1A28)
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconTint.copy(alpha = 0.15f))
                    .border(1.dp, iconTint.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFA0A0B0),
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = if (isChecked) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFF6B7280).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (isChecked) "ACTIVE: $badge" else "OFF",
                        color = if (isChecked) Color(0xFF34D399) else Color(0xFF9CA3AF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = iconTint
                )
            )
        }
    }
}
