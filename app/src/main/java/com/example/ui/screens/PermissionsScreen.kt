package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

private val PeachButtonColor = Color(0xFFFFBA8C)
private val PeachButtonTextColor = Color(0xFF1A1A1A)
private val BadgeRedBorder = Color(0xFFD9534F)
private val BadgeRedText = Color(0xFFFF8A80)
private val BadgeGreenBorder = Color(0xFF4CAF50)
private val BadgeGreenText = Color(0xFF81C784)
private val CyanSwitchTrack = Color(0xFF00E5FF)
private val LavenderBackButton = Color(0xFFD0BCFF)

@Composable
fun PermissionsScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val gamingAssistant by viewModel.gamingAssistantScreenCheck.collectAsState()
    val chatGptImageGen by viewModel.chatGptImageGeneration.collectAsState()
    val automatedPayments by viewModel.automatedPaymentsConfirmation.collectAsState()

    // Permission state checkers
    var isMicGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isCallGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isNotificationGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }
    var isMessagesGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isLocationGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isContactsGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isCameraGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isStorageGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    // Launchers
    val micLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        isMicGranted = granted
        viewModel.refreshPermissions()
        if (granted) viewModel.showToast("Microphone permission granted")
    }

    val callLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        isCallGranted = result.values.any { it }
        viewModel.refreshPermissions()
        if (isCallGranted) viewModel.showToast("Call permission granted")
    }

    val notificationLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        isNotificationGranted = granted
        viewModel.refreshPermissions()
        if (granted) viewModel.showToast("Notification permission granted")
    }

    val messagesLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        isMessagesGranted = result.values.any { it }
        viewModel.refreshPermissions()
        if (isMessagesGranted) viewModel.showToast("Messages permission granted")
    }

    val locationLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        isLocationGranted = result.values.any { it }
        viewModel.refreshPermissions()
        if (isLocationGranted) viewModel.showToast("Location permission granted")
    }

    val contactsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        isContactsGranted = result.values.any { it }
        viewModel.refreshPermissions()
        if (isContactsGranted) viewModel.showToast("Contacts permission granted")
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        isCameraGranted = granted
        viewModel.refreshPermissions()
        if (granted) viewModel.showToast("Camera permission granted")
    }

    val storageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        isStorageGranted = result.values.any { it }
        viewModel.refreshPermissions()
        if (isStorageGranted) viewModel.showToast("Storage permission granted")
    }

    val allRuntimePermissions = remember {
        val list = mutableListOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            list.add(Manifest.permission.POST_NOTIFICATIONS)
            list.add(Manifest.permission.READ_MEDIA_IMAGES)
            list.add(Manifest.permission.READ_MEDIA_VIDEO)
            list.add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            list.add(Manifest.permission.BLUETOOTH_CONNECT)
            list.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        list.toTypedArray()
    }

    val grantAllLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
        isMicGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        isCallGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        isCameraGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        isContactsGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        isLocationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        isMessagesGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
        isNotificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true
        isStorageGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
        viewModel.refreshPermissions()
        viewModel.showToast("All runtime permissions processed!")
    }

    val totalGrantedCount = listOf(
        isMicGranted,
        isCallGranted,
        isNotificationGranted,
        isMessagesGranted,
        isLocationGranted,
        isContactsGranted,
        isCameraGranted,
        isStorageGranted
    ).count { it }

    val isOverlayGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.canDrawOverlays(context) else true

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07070B))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("btn_back_permissions")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Permissions Explained",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Color.White,
                    modifier = Modifier.testTag("title_permissions_explained")
                )
                Spacer(modifier = Modifier.size(48.dp)) // balance layout
            }
        }

        // 0. MASTER ONE-TAP PERMISSION HUB CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("card_master_permission_hub"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131322)),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (totalGrantedCount >= 7) Color(0xFF10B981) else Color(0xFFEF4444)
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = if (totalGrantedCount >= 7) Color(0xFF34D399) else Color(0xFFF87171),
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    text = "Full Phone Access Hub",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "फ़ोन का पूरा कंट्रोल पाने के लिए सभी अनुमतियाँ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFA0A0B8)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (totalGrantedCount >= 7) Color(0xFF064E3B) else Color(0xFF451A1A)
                        ) {
                            Text(
                                text = "$totalGrantedCount / 8 Active",
                                color = if (totalGrantedCount >= 7) Color(0xFF6EE7B7) else Color(0xFFFCA5A5),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "MJ को कॉल, मैसेज, कैमरा, स्क्रीन ऑटोमेशन और होम स्क्रीन पर चलाने के लिए नीचे दिए गए बटन से एक साथ सभी परमिशन दें:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFD1D5DB)
                    )

                    Button(
                        onClick = {
                            viewModel.performHaptic()
                            grantAllLauncher.launch(allRuntimePermissions)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_grant_all_permissions")
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⚡ Grant All Permissions (एक साथ अनुमति दें)",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider(color = Color(0xFF222235), thickness = 1.dp)

                    Text(
                        text = "Special System Privileges (One-Tap Settings):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA5B4FC)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            onClick = {
                                viewModel.performHaptic()
                                openAccessibilitySettings(context)
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1E1B4B),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Accessibility, contentDescription = null, tint = Color(0xFF818CF8), modifier = Modifier.size(20.dp))
                                Text("Accessibility", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("Auto-Tap & Read", color = Color(0xFFA5B4FC), fontSize = 9.sp)
                            }
                        }

                        Surface(
                            onClick = {
                                viewModel.performHaptic()
                                openOverlaySettings(context)
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isOverlayGranted) Color(0xFF064E3B) else Color(0xFF31102A),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Layers, contentDescription = null, tint = if (isOverlayGranted) Color(0xFF34D399) else Color(0xFFF472B6), modifier = Modifier.size(20.dp))
                                Text("Screen Overlay", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(if (isOverlayGranted) "Active" else "Home HUD", color = if (isOverlayGranted) Color(0xFF6EE7B7) else Color(0xFFF9A8D4), fontSize = 9.sp)
                            }
                        }

                        Surface(
                            onClick = {
                                viewModel.performHaptic()
                                openNotificationListenerSettings(context)
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF33200B),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(20.dp))
                                Text("Notifications", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("Read & Auto-reply", color = Color(0xFFFDE68A), fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }

        // 1. Accessibility Service
        item {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Icon(
                            Icons.Default.AccessibilityNew,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Accessibility Service",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.White
                        )
                    }
                    StatusBadge(
                        text = "Accessibility permission required",
                        isGranted = false
                    )
                }

                Text(
                    text = "\uD83E\uDD16 Enable MJ AI to control your phone through voice commands. This permission allows reading screen content and performing actions for hands-free device automation and improved accessibility.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
                    color = Color(0xFFE2E2E6)
                )

                Text(
                    text = "\uD83E\uDD16 Why MJ Needs Accessibility Services:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    color = Color.White
                )

                Text(
                    text = "MJ is an AI assistant that helps you control your phone through voice commands. To do this, it requires accessibility permissions to understand and interact with your device.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
                    color = Color(0xFFD6D6DC)
                )

                Text(
                    text = "\uD83D\uDCC7 What This Permission Allows:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    color = Color.White
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    BulletPointText(
                        title = "Read Screen Content:",
                        description = "MJ can see and analyze what's currently displayed on your screen to understand the context and provide relevant assistance."
                    )
                    BulletPointText(
                        title = "Perform Actions:",
                        description = "Execute touch gestures, taps, swipes, and navigation (Back, Home, Recent Apps) on your behalf based on your voice commands."
                    )
                    BulletPointText(
                        title = "App Interaction:",
                        description = "Open apps, fill forms, scroll through content, and navigate between different screens as requested."
                    )
                }

                Text(
                    text = "\uD83D\uDD12 Privacy & Data Protection:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    color = Color.White
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "• All processing happens on Gemini API",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                        color = Color(0xFFE2E2E6)
                    )
                    Text(
                        text = "• No personal data is collected or but it is sent to Google Gemini",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                        color = Color(0xFFE2E2E6)
                    )
                    Text(
                        text = "• Screen content is only accessed when you actively use MJ, otherwise MJ doesnt access screen content",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                        color = Color(0xFFE2E2E6)
                    )
                    Text(
                        text = "• We implement safeguards to protect sensitive information, but MJ can still make mistake",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                        color = Color(0xFFE2E2E6)
                    )
                }

                Text(
                    text = "♿ Accessibility Benefits:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    color = Color.White
                )

                Text(
                    text = "This service enables hands-free device control, making smartphones more accessible for users with disabilities, visual impairments, or mobility limitations.\n\nBy proceeding, you understand and consent to MJ using accessibility services for AI-powered device automation.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
                    color = Color(0xFFD6D6DC)
                )

                PeachActionButton(
                    text = "Grant Accessibility Permission",
                    testTag = "btn_grant_accessibility",
                    onClick = {
                        viewModel.performHaptic()
                        openAccessibilitySettings(context)
                    }
                )
            }
        }

        // 2. Default Assistant
        item {
            PermissionSection(
                icon = Icons.Default.Mic,
                title = "Default Assistant",
                isGranted = false,
                description = "This lets you activate MJ by holding the home button or using a voice command, even when the app is closed.",
                buttonText = "Set as Default Assistant",
                testTag = "btn_default_assistant",
                onClick = {
                    viewModel.performHaptic()
                    openDefaultAssistantSettings(context)
                }
            )
        }

        // 3. Microphone
        item {
            PermissionSection(
                icon = Icons.Default.People,
                title = "Microphone",
                isGranted = isMicGranted,
                description = "The microphone is required for the voice command and wake-word features. It allows you to give instructions to MJ by speaking.",
                buttonText = "Grant Microphone Permission",
                testTag = "btn_grant_microphone",
                onClick = {
                    viewModel.performHaptic()
                    micLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            )
        }

        // 4. Display Over Other Apps
        item {
            PermissionSection(
                icon = Icons.Default.Layers,
                title = "Display Over Other Apps",
                isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.canDrawOverlays(context) else true,
                description = "This permission is used to show visual feedback, such as the questions that MJ have, captions and the voice wave animations, on top of other apps while MJ is working.",
                buttonText = "Grant Overlay Permission",
                testTag = "btn_grant_overlay",
                onClick = {
                    viewModel.performHaptic()
                    openOverlaySettings(context)
                }
            )
        }

        // 5. Calls
        item {
            PermissionSection(
                icon = Icons.Default.Phone,
                title = "Calls",
                isGranted = isCallGranted,
                description = "Allow MJ to place calls and manage phone actions when you ask it to call someone or dial a number.",
                buttonText = "Grant Call Permission",
                testTag = "btn_grant_calls",
                onClick = {
                    viewModel.performHaptic()
                    callLauncher.launch(arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE))
                }
            )
        }

        // 6. Notifications
        item {
            PermissionSection(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                isGranted = isNotificationGranted,
                description = "Let MJ show you alerts, reminders, and important updates related to your requests.",
                buttonText = if (isNotificationGranted) "Notification Settings" else "Grant Notification Permission",
                testTag = "btn_grant_notifications",
                onClick = {
                    viewModel.performHaptic()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        openAppSettings(context)
                    }
                }
            )
        }

        // 7. Messages
        item {
            PermissionSection(
                icon = Icons.Default.ChatBubble,
                title = "Messages",
                isGranted = isMessagesGranted,
                description = "Allow MJ to read or send messages so it can help you with SMS and messaging requests.",
                buttonText = "Grant Messages Permission",
                testTag = "btn_grant_messages",
                onClick = {
                    viewModel.performHaptic()
                    messagesLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_SMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_SMS
                        )
                    )
                }
            )
        }

        // 8. Location
        item {
            PermissionSection(
                icon = Icons.Default.LocationOn,
                title = "Location",
                isGranted = isLocationGranted,
                description = "Allow MJ to use your location for context-aware requests such as directions or nearby services.",
                buttonText = "Grant Location Permission",
                testTag = "btn_grant_location",
                onClick = {
                    viewModel.performHaptic()
                    locationLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            )
        }

        // 9. Contacts
        item {
            PermissionSection(
                icon = Icons.Default.Contacts,
                title = "Contacts",
                isGranted = isContactsGranted,
                description = "Allow MJ to look up names and numbers so it can call, message, or announce contacts by name.",
                buttonText = "Grant Contacts Permission",
                testTag = "btn_grant_contacts",
                onClick = {
                    viewModel.performHaptic()
                    contactsLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS
                        )
                    )
                }
            )
        }

        // 10. Screen Lock
        item {
            PermissionSection(
                icon = Icons.Default.Lock,
                title = "Screen Lock",
                isGranted = false,
                description = "Allow MJ to lock your screen when you ask it to.",
                buttonText = "Grant Screen Lock Permission",
                testTag = "btn_grant_screen_lock",
                onClick = {
                    viewModel.performHaptic()
                    openAccessibilitySettings(context)
                }
            )
        }

        // 11. App Management
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Apps,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "App Management",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Color.White
                    )
                }
                Text(
                    text = "Allows MJ to see the apps you have installed. With this, MJ can open applications for you directly and instantly (e.g., Open YouTube), instead of relying on slower methods of searching the screen. This makes the process much faster and more reliable.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
                    color = Color(0xFFD6D6DC)
                )
            }
        }

        // 12. Camera Vision
        item {
            PermissionSection(
                icon = Icons.Default.CameraAlt,
                title = "Camera Vision",
                isGranted = isCameraGranted,
                description = "Required for MJ to look at objects, scan QR/barcodes, and read documents.",
                buttonText = "Grant Camera Permission",
                testTag = "btn_grant_camera",
                onClick = {
                    viewModel.performHaptic()
                    cameraLauncher.launch(Manifest.permission.CAMERA)
                }
            )
        }

        // 13. Storage & File Access
        item {
            PermissionSection(
                icon = Icons.Default.Folder,
                title = "Storage & File Access",
                isGranted = isStorageGranted,
                description = "Required for MJ to manage files, clean storage, analyze disk space, search photos, and save photos.",
                buttonText = "Grant Storage Permission",
                testTag = "btn_grant_storage",
                onClick = {
                    viewModel.performHaptic()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        storageLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.READ_MEDIA_AUDIO
                            )
                        )
                    } else {
                        storageLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                    }
                }
            )
        }

        // 14. Battery Optimization
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Battery Optimization",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.White
                        )
                    }
                    StatusBadge(
                        text = "Not Granted",
                        isGranted = false
                    )
                }

                Text(
                    text = "If MJ is not working properly, this might be due to intense battery optimization by your device manufacturer.\n\nManufacturers like Samsung, Huawei, OnePlus, Xiaomi, and others often add aggressive battery optimizations that can break MJ's functionality.\n\nTo fix this, you need to disable battery optimizations specifically for MJ in your device settings.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
                    color = Color(0xFFD6D6DC)
                )

                PeachActionButton(
                    text = "Open Battery Settings",
                    testTag = "btn_battery_settings",
                    onClick = {
                        viewModel.performHaptic()
                        openBatterySettings(context)
                    }
                )
            }
        }

        // 15. Usage Access
        item {
            PermissionSection(
                icon = Icons.Default.Schedule,
                title = "Usage Access",
                isGranted = false,
                description = "Lets MJ see which apps have been most active, so she can tell you what is draining your battery or slowing the phone down.",
                buttonText = "Open Usage Access Settings",
                testTag = "btn_usage_access",
                onClick = {
                    viewModel.performHaptic()
                    openUsageAccessSettings(context)
                }
            )
        }

        // 16. Gaming Assistant Screen Check (Toggle)
        item {
            ToggleFeatureSection(
                icon = Icons.Default.AccessibilityNew,
                title = "Gaming Assistant Screen Check",
                description = "Allow MJ to analyze the game screen state to provide real-time strategic advice for Free Fire, BGMI, etc.",
                isChecked = gamingAssistant,
                onCheckedChange = { viewModel.toggleGamingAssistant() },
                testTag = "toggle_gaming_assistant"
            )
        }

        // 17. ChatGPT Image Generation (Toggle)
        item {
            ToggleFeatureSection(
                icon = Icons.Default.AccessibilityNew,
                title = "ChatGPT Image Generation",
                description = "Allow MJ to automate the installed ChatGPT app to create and download images and set wallpapers.",
                isChecked = chatGptImageGen,
                onCheckedChange = { viewModel.toggleChatGptImageGeneration() },
                testTag = "toggle_chatgpt_image_gen"
            )
        }

        // 18. Automated Payments Confirmation (Toggle)
        item {
            ToggleFeatureSection(
                icon = Icons.Default.AccessibilityNew,
                title = "Automated Payments Confirmation",
                description = "Allow MJ to perform hands-free banking/UPI payment screen automation with secure user confirmation.",
                isChecked = automatedPayments,
                onCheckedChange = { viewModel.toggleAutomatedPayments() },
                testTag = "toggle_automated_payments"
            )
        }

        // Bottom BACK Button (Screenshot 8)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        viewModel.performHaptic()
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(52.dp)
                        .testTag("btn_back_bottom"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LavenderBackButton)
                ) {
                    Text(
                        text = "BACK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        ),
                        color = Color(0xFF1A1A1A)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    isGranted: Boolean
) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isGranted) BadgeGreenBorder else BadgeRedBorder
        )
    ) {
        Text(
            text = text,
            color = if (isGranted) BadgeGreenText else BadgeRedText,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun BulletPointText(
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "• ",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = Color(0xFFD6D6DC)
            )
        }
    }
}

@Composable
private fun PeachActionButton(
    text: String,
    testTag: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PeachButtonColor)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = PeachButtonTextColor
        )
    }
}

@Composable
private fun PermissionSection(
    icon: ImageVector,
    title: String,
    isGranted: Boolean,
    description: String,
    buttonText: String,
    testTag: String,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
            }
            StatusBadge(
                text = if (isGranted) "Granted" else "Not Granted",
                isGranted = isGranted
            )
        }

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
            color = Color(0xFFD6D6DC)
        )

        PeachActionButton(
            text = buttonText,
            testTag = testTag,
            onClick = onClick
        )
    }
}

@Composable
private fun ToggleFeatureSection(
    icon: ImageVector,
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = CyanSwitchTrack,
                    uncheckedThumbColor = Color(0xFF8E8E93),
                    uncheckedTrackColor = Color(0xFF2C2C2E)
                ),
                modifier = Modifier.testTag(testTag)
            )
        }

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 22.sp),
            color = Color(0xFFD6D6DC)
        )
    }
}

private fun openAppSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        val intent = Intent(Settings.ACTION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

private fun openAccessibilitySettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        openAppSettings(context)
    }
}

private fun openDefaultAssistantSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_VOICE_INPUT_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            openAppSettings(context)
        }
    }
}

private fun openOverlaySettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        try {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            openAppSettings(context)
        }
    } else {
        openAppSettings(context)
    }
}

private fun openBatterySettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        try {
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            openAppSettings(context)
        }
    } else {
        openAppSettings(context)
    }
}

private fun openUsageAccessSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        openAppSettings(context)
    }
}

private fun openNotificationListenerSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        openAppSettings(context)
    }
}

