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
fun FullDeviceControlScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFullControlEnabled by viewModel.isFullDeviceControlEnabled.collectAsState()
    val isAccessibilityEnabled by viewModel.isAccessibilityEnabled.collectAsState()
    val isOverlayEnabled by viewModel.isOverlayEnabled.collectAsState()
    val isNotificationAccessEnabled by viewModel.isNotificationAccessEnabled.collectAsState()
    val isDeviceAdminEnabled by viewModel.isDeviceAdminEnabled.collectAsState()
    val isBatteryOptIgnored by viewModel.isBatteryOptIgnored.collectAsState()

    val brightnessLevel by viewModel.brightnessLevel.collectAsState()
    val mediaVolume by viewModel.mediaVolume.collectAsState()
    val callVolume by viewModel.callVolume.collectAsState()
    val ringVolume by viewModel.ringVolume.collectAsState()
    val alarmVolume by viewModel.alarmVolume.collectAsState()

    val isWifiEnabled by viewModel.isWifiEnabled.collectAsState()
    val isBluetoothEnabled by viewModel.isBluetoothEnabled.collectAsState()
    val isHotspotEnabled by viewModel.isHotspotEnabled.collectAsState()
    val isDndEnabled by viewModel.isDndEnabled.collectAsState()
    val isFlashlightOn by viewModel.isFlashlightOn.collectAsState()
    val isAutoRotateEnabled by viewModel.isAutoRotateEnabled.collectAsState()
    val soundMode by viewModel.soundMode.collectAsState()
    val screenTimeoutText by viewModel.screenTimeoutText.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_master")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
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
                                text = "Full Device Control",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Surface(
                                color = if (isFullControlEnabled) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isFullControlEnabled) Color(0xFF10B981) else Color(0xFFEF4444))
                            ) {
                                Text(
                                    text = if (isFullControlEnabled) "MASTER ACTIVE" else "PAUSED",
                                    color = if (isFullControlEnabled) Color(0xFF10B981) else Color(0xFFEF4444),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Complete Phone Automation & Master Privileges",
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
            // 1. MASTER AUTONOMOUS CONTROL BANNER
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .testTag("master_autonomous_control_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141422)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(
                                if (isFullControlEnabled) MyraRedGlow.copy(alpha = pulseGlow) else Color(0xFF333344),
                                if (isFullControlEnabled) Color(0xFF9333EA).copy(alpha = pulseGlow) else Color(0xFF222230)
                            )
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
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            Brush.radialGradient(
                                                listOf(
                                                    if (isFullControlEnabled) MyraRed.copy(alpha = 0.35f) else Color(0xFF252535),
                                                    Color(0xFF0F0F1A)
                                                )
                                            )
                                        )
                                        .border(1.dp, if (isFullControlEnabled) MyraRedGlow else Color(0xFF444455), RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = if (isFullControlEnabled) MyraRedGlow else Color.Gray,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Full Phone Master Control",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = if (isFullControlEnabled) "Autonomous mode: MJ can control the entire phone" else "Phone control is paused",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isFullControlEnabled) Color(0xFF34D399) else Color(0xFF9E9EB6)
                                    )
                                }
                            }
                            Switch(
                                checked = isFullControlEnabled,
                                onCheckedChange = { viewModel.toggleFullDeviceControl() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = MyraRedGlow
                                ),
                                modifier = Modifier.testTag("toggle_master_phone_control")
                            )
                        }

                        Divider(color = Color(0xFF222235), thickness = 1.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Autonomous Execution: Auto-Tap, System Settings, Notifications & 24/7 Daemon",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                color = Color(0xFFCBD5E1),
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = { viewModel.boostDeviceRam() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1B4B)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("btn_quick_boost")
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFFA5B4FC), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Boost RAM", color = Color(0xFFA5B4FC), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 2. MASTER PRIVILEGES & SUBSYSTEMS (6 CARDS)
            item {
                Text(
                    text = "Master Control Privileges",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // 1. Accessibility Service Automation
                    MasterPrivilegeCard(
                        title = "Accessibility Automation Engine",
                        subtitle = "Allows MJ to auto-tap, auto-type, scroll, and read any third-party app screen.",
                        icon = Icons.Default.Accessibility,
                        iconTint = Color(0xFF60A5FA),
                        isEnabled = isAccessibilityEnabled,
                        onToggle = { viewModel.toggleAccessibilityService() },
                        badge = "AUTO-TAP & GESTURES"
                    )

                    // 2. Screen Overlay (Draw Over Apps)
                    MasterPrivilegeCard(
                        title = "Floating Master HUD (Overlay)",
                        subtitle = "Displays the holographic AI Orb on top of all apps to command phone from anywhere.",
                        icon = Icons.Default.PictureInPicture,
                        iconTint = Color(0xFFF472B6),
                        isEnabled = isOverlayEnabled,
                        onToggle = { viewModel.toggleOverlayPermission() },
                        badge = "ALWAYS VISIBLE"
                    )

                    // 3. Notification Interceptor & Auto-Reply
                    MasterPrivilegeCard(
                        title = "Notification Interceptor & Auto-Reply",
                        subtitle = "Reads incoming notifications (WhatsApp, SMS, Telegram, Bank) and sends auto-replies.",
                        icon = Icons.Default.NotificationsActive,
                        iconTint = Color(0xFFFBBF24),
                        isEnabled = isNotificationAccessEnabled,
                        onToggle = { viewModel.toggleNotificationListener() },
                        badge = "ALL APPS"
                    )

                    // 4. Device Admin & Security Protocol
                    MasterPrivilegeCard(
                        title = "Device Admin & Instant Lock",
                        subtitle = "Enables emergency remote screen lock, anti-theft lockdown, and system security control.",
                        icon = Icons.Default.AdminPanelSettings,
                        iconTint = Color(0xFFEF4444),
                        isEnabled = isDeviceAdminEnabled,
                        onToggle = { viewModel.toggleDeviceAdminPolicy() },
                        badge = "SECURITY ADMIN"
                    )

                    // 5. 24/7 Background Daemon
                    MasterPrivilegeCard(
                        title = "24/7 Background Unlimited Daemon",
                        subtitle = "Exempts MJ from OS battery restrictions so voice listener and triggers run continuously.",
                        icon = Icons.Default.BatteryChargingFull,
                        iconTint = Color(0xFF34D399),
                        isEnabled = isBatteryOptIgnored,
                        onToggle = { viewModel.toggleBatteryOptimization() },
                        badge = "UNLIMITED 24/7"
                    )
                }
            }

            // 3. HARDWARE CONTROLS & SYSTEM SETTINGS
            item {
                Text(
                    text = "System Settings & Hardware Controls",
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Quick Toggles Grid (Wi-Fi, BT, Hotspot, DND, Torch, Rotate)
                        Text(
                            text = "Quick Hardware Toggles",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFFA5B4FC),
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HardwareQuickToggle(
                                label = "Wi-Fi",
                                icon = Icons.Default.Wifi,
                                isActive = isWifiEnabled,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.toggleWifiState() }
                            )
                            HardwareQuickToggle(
                                label = "Bluetooth",
                                icon = Icons.Default.Bluetooth,
                                isActive = isBluetoothEnabled,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.toggleBluetoothState() }
                            )
                            HardwareQuickToggle(
                                label = "Torch",
                                icon = Icons.Default.FlashlightOn,
                                isActive = isFlashlightOn,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.toggleFlashlight() }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HardwareQuickToggle(
                                label = "Hotspot",
                                icon = Icons.Default.WifiTethering,
                                isActive = isHotspotEnabled,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.toggleHotspotState() }
                            )
                            HardwareQuickToggle(
                                label = "DND Mode",
                                icon = Icons.Default.DoNotDisturb,
                                isActive = isDndEnabled,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.toggleDndState() }
                            )
                            HardwareQuickToggle(
                                label = "Auto-Rotate",
                                icon = Icons.Default.ScreenRotation,
                                isActive = isAutoRotateEnabled,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.toggleAutoRotateState() }
                            )
                        }

                        Divider(color = Color(0xFF222235), thickness = 1.dp)

                        // Sound Mode Selector
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ringer & Sound Mode",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = soundMode,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MyraRedGlow,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Ring", "Vibrate", "Silent").forEach { mode ->
                                    val isSelected = soundMode == mode
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { viewModel.setDeviceSoundMode(mode) },
                                        color = if (isSelected) MyraRed.copy(alpha = 0.25f) else Color(0xFF1E1E2C),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) MyraRedGlow else Color(0xFF333348)
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = when (mode) {
                                                    "Ring" -> "🔔 Ring"
                                                    "Vibrate" -> "📳 Vibrate"
                                                    else -> "🔇 Silent"
                                                },
                                                color = if (isSelected) Color.White else Color(0xFFA0A0B0),
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Divider(color = Color(0xFF222235), thickness = 1.dp)

                        // Brightness Slider
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.Brightness6, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(18.dp))
                                    Text("Display Brightness", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                }
                                Text("${(brightnessLevel * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFBBF24), fontWeight = FontWeight.Bold)
                            }
                            Slider(
                                value = brightnessLevel,
                                onValueChange = { viewModel.setBrightnessLevel(it) },
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFFFBBF24),
                                    activeTrackColor = Color(0xFFFBBF24),
                                    inactiveTrackColor = Color(0xFF333348)
                                ),
                                modifier = Modifier.testTag("slider_brightness")
                            )
                        }

                        // Media Volume Slider
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(18.dp))
                                    Text("Media Volume", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                }
                                Text("${(mediaVolume * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = Color(0xFF60A5FA), fontWeight = FontWeight.Bold)
                            }
                            Slider(
                                value = mediaVolume,
                                onValueChange = { viewModel.setMediaVolumeLevel(it) },
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF60A5FA),
                                    activeTrackColor = Color(0xFF60A5FA),
                                    inactiveTrackColor = Color(0xFF333348)
                                ),
                                modifier = Modifier.testTag("slider_media_volume")
                            )
                        }

                        // Call & Ringtone Volume Slider
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(18.dp))
                                    Text("Call & Ringtone Volume", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                }
                                Text("${(callVolume * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = Color(0xFF34D399), fontWeight = FontWeight.Bold)
                            }
                            Slider(
                                value = callVolume,
                                onValueChange = { viewModel.setCallVolumeLevel(it) },
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF34D399),
                                    activeTrackColor = Color(0xFF34D399),
                                    inactiveTrackColor = Color(0xFF333348)
                                ),
                                modifier = Modifier.testTag("slider_call_volume")
                            )
                        }

                        // Screen Timeout
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Screen Timeout", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                Text(screenTimeoutText, style = MaterialTheme.typography.bodySmall, color = Color(0xFFA5B4FC), fontWeight = FontWeight.Bold)
                            }

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val timeouts = listOf("30 Seconds", "1 Minute", "2 Minutes", "5 Minutes", "10 Minutes", "Never Sleep")
                                items(timeouts) { duration ->
                                    val isSelected = screenTimeoutText == duration
                                    Surface(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.setScreenTimeout(duration) },
                                        color = if (isSelected) Color(0xFF4F46E5).copy(alpha = 0.3f) else Color(0xFF1E1E2C),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) Color(0xFF818CF8) else Color(0xFF333348)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = duration,
                                            color = if (isSelected) Color.White else Color(0xFFA0A0B0),
                                            fontSize = 11.5.sp,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. ACCESSIBILITY SIMULATION & TEST ACTIONS
            item {
                Text(
                    text = "Live Accessibility Sandbox & Actions",
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
                            text = "Simulate Automated Actions Across Phone:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0A0B0)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.simulateAccessibilityTap(540, 1100, "Auto-Click Target") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("btn_test_autotap")
                            ) {
                                Icon(Icons.Default.TouchApp, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Auto-Tap", color = Color(0xFF38BDF8), fontSize = 12.sp)
                            }

                            Button(
                                onClick = { viewModel.simulateAccessibilityScroll("DOWN") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("btn_test_autoscroll")
                            ) {
                                Icon(Icons.Default.SwapVert, contentDescription = null, tint = Color(0xFFC084FC), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Auto-Scroll", color = Color(0xFFC084FC), fontSize = 12.sp)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.simulateSystemKey("BACK") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("btn_test_back")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Key: Back", color = Color(0xFFFBBF24), fontSize = 12.sp)
                            }

                            Button(
                                onClick = { viewModel.simulateSystemKey("HOME") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("btn_test_home")
                            ) {
                                Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Key: Home", color = Color(0xFF34D399), fontSize = 12.sp)
                            }

                            Button(
                                onClick = { viewModel.executeToolCommand(com.example.model.ToolGuideItem("lock", 1, "Security", "Lock Phone", "Instant Lock", "Lock device", actionType = "LOCK")) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B1522)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("btn_test_lock")
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = MyraRedGlow, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Lock", color = MyraRedGlow, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // 5. QUICK APP LAUNCHER & CONTROLLER
            item {
                Text(
                    text = "Automated App Launch & Control",
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
                            text = "One-tap launch and automated takeover:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0A0B0)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppLaunchChip(
                                name = "WhatsApp",
                                icon = Icons.Default.Chat,
                                tint = Color(0xFF25D366),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.launchSystemApp("com.whatsapp", "WhatsApp", "https://web.whatsapp.com") }
                            )
                            AppLaunchChip(
                                name = "YouTube",
                                icon = Icons.Default.PlayCircle,
                                tint = Color(0xFFFF0000),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.launchSystemApp("com.google.android.youtube", "YouTube", "https://youtube.com") }
                            )
                            AppLaunchChip(
                                name = "Maps",
                                icon = Icons.Default.Navigation,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.launchSystemApp("com.google.android.apps.maps", "Google Maps", "https://maps.google.com") }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppLaunchChip(
                                name = "Camera",
                                icon = Icons.Default.CameraAlt,
                                tint = Color(0xFFF472B6),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.launchSystemApp("com.android.camera", "Camera") }
                            )
                            AppLaunchChip(
                                name = "Dialer",
                                icon = Icons.Default.Phone,
                                tint = Color(0xFF34D399),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.launchSystemApp("com.android.dialer", "Phone Dialer") }
                            )
                            AppLaunchChip(
                                name = "Settings",
                                icon = Icons.Default.Settings,
                                tint = Color(0xFFA5B4FC),
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.launchSystemApp("com.android.settings", "System Settings") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MasterPrivilegeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    isEnabled: Boolean,
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
                    if (isEnabled) iconTint.copy(alpha = 0.5f) else Color(0xFF2A2A3A),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFA0A0B0),
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = if (isEnabled) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFF6B7280).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (isEnabled) "ACTIVE: $badge" else "OFFLINE",
                        color = if (isEnabled) Color(0xFF34D399) else Color(0xFF9CA3AF),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Switch(
                checked = isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = iconTint
                )
            )
        }
    }
}

@Composable
private fun HardwareQuickToggle(
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (isActive) MyraRed.copy(alpha = 0.25f) else Color(0xFF1E1E2C),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) MyraRedGlow else Color(0xFF333348)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive) MyraRedGlow else Color(0xFF9E9EB6),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = if (isActive) Color.White else Color(0xFFA0A0B0),
                fontSize = 11.5.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = if (isActive) "ON" else "OFF",
                color = if (isActive) Color(0xFF34D399) else Color(0xFF6B6B80),
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun AppLaunchChip(
    name: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = Color(0xFF1A1A28),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2C3E)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
