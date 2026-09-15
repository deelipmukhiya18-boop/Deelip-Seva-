package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppScreen
import com.example.model.OrbStyle
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@Composable
fun SettingsScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val callAssistantEnabled by viewModel.callAssistantEnabled.collectAsState()
    val chatNotificationsEnabled by viewModel.chatNotificationsEnabled.collectAsState()
    val orbSettings by viewModel.orbSettings.collectAsState()
    val isFloating3DOrbActive by viewModel.isFloating3DOrbActive.collectAsState()

    var activeModal by remember { mutableStateOf<String?>(null) }

    val coralHeaderColor = Color(0xFFFF8E72)
    val purpleIconBg = Color(0xFFC084FC).copy(alpha = 0.28f)
    val lavenderTint = Color(0xFFE9D5FF)

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF0C0C12))) {
        // Subtle top right purple glow behind the avatar matching screenshot
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(260.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.22f),
                            Color(0xFF6B21A8).copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section with AI Character Photo matching screenshot
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "MJ",
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = coralHeaderColor,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Your Intelligent AI Assistant",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFA0A0AB),
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(22.dp))
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Customize your MJ experience",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFA0A0AB)
                        )
                    }

                    // Cyberpunk anime female AI character avatar from screenshot
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF140F20))
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color(0xFFC084FC),
                                        Color(0xFFFF8E72).copy(alpha = 0.6f),
                                        Color(0xFF7C3AED).copy(alpha = 0.3f)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .clickable { onNavigate(AppScreen.AI_IDENTITY) },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.myra_ai_avatar_1788284446471),
                            contentDescription = "MJ AI Assistant Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(24.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // 1. Voice Settings
            item {
                SettingRowItem(
                    title = "Voice Settings",
                    subtitle = "Tune how MJ sounds and listens",
                    icon = Icons.Default.GraphicEq,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_voice_settings",
                    onClick = { onNavigate(AppScreen.VOICE_SETTINGS) }
                )
            }

            // 2. Voice & AI Models
            item {
                SettingRowItem(
                    title = "Voice & AI Models",
                    subtitle = "Manage assistant voice and AI models",
                    icon = Icons.Default.SmartToy,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_voice_ai",
                    onClick = { onNavigate(AppScreen.VOICE_AI_MODELS) }
                )
            }

            // 2. Aura Control
            item {
                SettingRowItem(
                    title = "Aura Control",
                    subtitle = "Launcher core, somatic vibrations & aura signatures",
                    icon = Icons.Default.Tune,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_aura_control",
                    onClick = { onNavigate(AppScreen.AURA_CONTROL) }
                )
            }

            // 3. Orb Customization
            item {
                SettingRowItem(
                    title = "Orb Customization",
                    subtitle = "Change assistant orb color, speed and glow",
                    icon = Icons.Default.Circle,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_orb_customization",
                    onClick = { onNavigate(AppScreen.ORB_CUSTOMIZE) }
                )
            }

            // 3. API & Cloud Settings
            item {
                SettingRowItem(
                    title = "API & Cloud Settings",
                    subtitle = "Configure all API keys and services",
                    icon = Icons.Default.Cloud,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_api_cloud",
                    onClick = { onNavigate(AppScreen.API_CLOUD_SETTINGS) }
                )
            }

            // 4. Connectors
            item {
                SettingRowItem(
                    title = "Connectors",
                    subtitle = "Connect MJ with apps and AI services",
                    icon = Icons.Default.Power,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_connectors",
                    onClick = { onNavigate(AppScreen.CONNECTORS) }
                )
            }

            // 5. Automation & Triggers
            item {
                SettingRowItem(
                    title = "Automation & Triggers",
                    subtitle = "Set autonomous 'When X happens, do Y' triggers",
                    icon = Icons.Default.Bolt,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_triggers",
                    onClick = { onNavigate(AppScreen.TRIGGERS) }
                )
            }

            // 6. Permissions & Privacy
            item {
                SettingRowItem(
                    title = "Permissions & Privacy",
                    subtitle = "Permissions Explained, accessibility & feature controls",
                    icon = Icons.Default.Shield,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_permissions",
                    onClick = { onNavigate(AppScreen.PERMISSIONS) }
                )
            }

            // 6. Voice Authentication
            item {
                SettingRowItem(
                    title = "Voice Authentication",
                    subtitle = "Secure your assistant with your voice",
                    icon = Icons.Default.Lock,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_voice_auth",
                    onClick = { activeModal = "VOICE_AUTH" }
                )
            }

            // 7. Wake Word
            item {
                SettingRowItem(
                    title = "Wake Word",
                    subtitle = "Customize how you start MJ",
                    icon = Icons.Default.RecordVoiceOver,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_wake_word",
                    onClick = { activeModal = "WAKE_WORD" }
                )
            }

            // 8. Intelligence & Modes
            item {
                SettingRowItem(
                    title = "Intelligence & Modes",
                    subtitle = "Map permissions, smart reading, and automation modes",
                    icon = Icons.Default.AutoAwesome,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_intelligence_modes",
                    onClick = { activeModal = "INTELLIGENCE_MODES" }
                )
            }

            // 9. Call Assistant (Switch Toggle)
            item {
                SettingRowToggle(
                    title = "Call Assistant",
                    subtitle = "Announce incoming calls and control them using your voice.",
                    icon = Icons.Default.Call,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    isChecked = callAssistantEnabled,
                    testTag = "toggle_call_assistant",
                    onCheckedChange = { viewModel.toggleCallAssistant() }
                )
            }

            // 10. Chat Notifications (Switch Toggle)
            item {
                SettingRowToggle(
                    title = "Chat Notifications",
                    subtitle = "Get notified about new chat messages.",
                    icon = Icons.Default.ChatBubble,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    isChecked = chatNotificationsEnabled,
                    testTag = "toggle_chat_notifications",
                    onCheckedChange = { viewModel.toggleChatNotifications() }
                )
            }

            // 12. License Activation
            item {
                SettingRowItem(
                    title = "License Activation",
                    subtitle = "Enter a license key bought from the website",
                    icon = Icons.Default.VpnKey,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_license_activation",
                    onClick = { activeModal = "LICENSE_ACTIVATION" }
                )
            }

            // 13. Subscription
            item {
                SettingRowItem(
                    title = "Subscription",
                    subtitle = "Manage plans and in-app payments",
                    icon = Icons.Default.WorkspacePremium,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_subscription",
                    onClick = { activeModal = "SUBSCRIPTION" }
                )
            }

            // 14. AI Identity & Profile
            item {
                SettingRowItem(
                    title = "AI Identity",
                    subtitle = "Master name, credits, SOS protocol & core sync",
                    icon = Icons.Default.Person,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_ai_identity",
                    onClick = { onNavigate(AppScreen.AI_IDENTITY) }
                )
            }

            // 15. Batch / Update
            item {
                SettingRowItem(
                    title = "Batch / Update",
                    subtitle = "Check updates and batch settings",
                    icon = Icons.Default.Sync,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_batch_update",
                    onClick = { activeModal = "BATCH_UPDATE" }
                )
            }

            // 16. Account
            item {
                SettingRowItem(
                    title = "Account",
                    subtitle = "Log out from your account",
                    icon = Icons.Default.MeetingRoom,
                    iconBgColor = purpleIconBg,
                    iconTint = lavenderTint,
                    testTag = "setting_account",
                    onClick = { activeModal = "ACCOUNT" }
                )
            }
        }

        // Active Dialog Modals
        when (activeModal) {
            "VOICE_AI" -> VoiceAndAiModelsDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "API_CLOUD" -> ApiAndCloudSettingsDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "VOICE_AUTH" -> VoiceAuthenticationDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "WAKE_WORD" -> WakeWordDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "INTELLIGENCE_MODES" -> IntelligenceAndModesDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "PC_CONNECT" -> PcConnectDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "LICENSE_ACTIVATION" -> LicenseActivationDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "SUBSCRIPTION" -> SubscriptionDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "USER_PROFILE" -> UserProfileDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "BATCH_UPDATE" -> BatchUpdateDialog(viewModel = viewModel, onDismiss = { activeModal = null })
            "ACCOUNT" -> AccountDialog(viewModel = viewModel, onDismiss = { activeModal = null })
        }
    }
}

@Composable
private fun SettingRowItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFA0A0AB),
                    lineHeight = 16.sp
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF71717A),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SettingRowToggle(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    isChecked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 6.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFA0A0AB),
                    lineHeight = 16.sp
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF8B5CF6),
                uncheckedThumbColor = Color(0xFF9CA3AF),
                uncheckedTrackColor = Color(0xFF374151)
            )
        )
    }
}
