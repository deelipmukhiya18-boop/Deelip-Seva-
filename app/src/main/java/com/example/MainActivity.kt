package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.model.AppScreen
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MyraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleWidgetVoiceIntent(intent)
        setContent {
            MYRATheme {
                MyraApp(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleWidgetVoiceIntent(intent)
    }

    private fun handleWidgetVoiceIntent(intent: android.content.Intent?) {
        if (intent != null && intent.getBooleanExtra(com.example.widget.Myra3DWidgetProvider.ACTION_OPEN_VOICE, false)) {
            viewModel.dismissIntroVideo()
            viewModel.openDialog("VOICE_MODE")
        }
    }
}

@Composable
fun MyraApp(viewModel: MyraViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val orbSettings by viewModel.orbSettings.collectAsState()
    val auraSettings by viewModel.auraSettings.collectAsState()
    val connectors by viewModel.connectors.collectAsState()
    val triggers by viewModel.triggers.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val toolGuideItems = viewModel.toolGuideSections
    val activeDialog by viewModel.activeDialog.collectAsState()
    val currentBgTheme by viewModel.mjBackgroundTheme.collectAsState()
    val isIntroVideoShowing by viewModel.isIntroVideoShowing.collectAsState()

    val dynamicBackground = remember(currentBgTheme) {
        Brush.verticalGradient(
            listOf(
                Color(currentBgTheme.bgPrimaryHex),
                Color(currentBgTheme.bgSecondaryHex)
            )
        )
    }

    // Android System Back Navigation Handler
    BackHandler(enabled = isIntroVideoShowing || activeDialog != null || currentScreen != AppScreen.HOME) {
        if (isIntroVideoShowing) {
            viewModel.dismissIntroVideo()
        } else if (activeDialog != null) {
            viewModel.closeDialog()
        } else {
            when (currentScreen) {
                AppScreen.ORB_CUSTOMIZE -> viewModel.setScreen(AppScreen.AURA_CONTROL)
                AppScreen.CHOOSE_TRIGGER_TYPE -> viewModel.setScreen(AppScreen.TRIGGERS)
                AppScreen.DEEP_RESEARCH_SETTINGS -> viewModel.setScreen(AppScreen.API_CLOUD_SETTINGS)
                AppScreen.VOICE_SETTINGS -> viewModel.setScreen(AppScreen.VOICE_AI_MODELS)
                AppScreen.BG_COLOR_CUSTOMIZER,
                AppScreen.AURA_CONTROL,
                AppScreen.CONNECTORS,
                AppScreen.TRIGGERS,
                AppScreen.PERMISSIONS,
                AppScreen.API_CLOUD_SETTINGS,
                AppScreen.VOICE_AI_MODELS,
                AppScreen.AI_IDENTITY,
                AppScreen.FULL_DEVICE_CONTROL,
                AppScreen.CALL_SMS_ANNOUNCER,
                AppScreen.VOICE_AUTH,
                AppScreen.WAKE_WORD,
                AppScreen.INTELLIGENCE_MODES,
                AppScreen.PC_CONNECT,
                AppScreen.FILE_MANAGER,
                AppScreen.CONVERSATION_DATABASE,
                AppScreen.LICENSE_ACTIVATION,
                AppScreen.SUBSCRIPTION,
                AppScreen.USER_PROFILE,
                AppScreen.BATCH_UPDATE,
                AppScreen.ACCOUNT -> viewModel.setScreen(AppScreen.SETTINGS)
                AppScreen.SETTINGS,
                AppScreen.CHAT,
                AppScreen.TOOLS_GUIDE,
                AppScreen.MISSIONS,
                AppScreen.PHONE_LAUNCHER,
                AppScreen.MAYA_SUITE -> viewModel.setScreen(AppScreen.HOME)
                AppScreen.HOME -> { /* Handled by system to exit */ }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dynamicBackground)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            bottomBar = {
                if (currentScreen != AppScreen.PHONE_LAUNCHER) {
                    MyraBottomNavigationBar(
                        currentScreen = currentScreen,
                        onSelectScreen = {
                            viewModel.performHaptic()
                            viewModel.setScreen(it)
                        },
                        onCenterOrbClick = {
                            viewModel.performHaptic()
                            viewModel.openDialog("VOICE_MODE")
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (currentScreen == AppScreen.PHONE_LAUNCHER) PaddingValues(0.dp) else innerPadding)
            ) {
                Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                    when (screen) {
                        AppScreen.HOME -> HomeScreen(
                            viewModel = viewModel,
                            orbSettings = orbSettings,
                            onNavigate = { viewModel.setScreen(it) }
                        )
                        AppScreen.CHAT -> ChatScreen(
                            viewModel = viewModel,
                            messages = chatMessages
                        )
                        AppScreen.TOOLS_GUIDE -> ToolsGuideScreen(
                            viewModel = viewModel,
                            toolItems = toolGuideItems,
                            onNavigateBack = { viewModel.setScreen(AppScreen.HOME) }
                        )
                        AppScreen.AURA_CONTROL -> AuraControlScreen(
                            viewModel = viewModel,
                            auraSettings = auraSettings,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) },
                            onNavigate = { viewModel.setScreen(it) }
                        )
                        AppScreen.ORB_CUSTOMIZE -> OrbCustomizeScreen(
                            viewModel = viewModel,
                            orbSettings = orbSettings,
                            onNavigateBack = { viewModel.setScreen(AppScreen.AURA_CONTROL) }
                        )
                        AppScreen.CONNECTORS -> ConnectorsScreen(
                            viewModel = viewModel,
                            connectors = connectors,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) }
                        )
                        AppScreen.TRIGGERS -> TriggersScreen(
                            viewModel = viewModel,
                            triggers = triggers,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) },
                            onNavigateToChooseTrigger = { viewModel.setScreen(AppScreen.CHOOSE_TRIGGER_TYPE) }
                        )
                        AppScreen.CHOOSE_TRIGGER_TYPE -> ChooseTriggerTypeScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.TRIGGERS) }
                        )
                        AppScreen.MISSIONS -> MissionsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.HOME) },
                            onNavigateToTriggers = { viewModel.setScreen(AppScreen.TRIGGERS) }
                        )
                        AppScreen.PERMISSIONS -> PermissionsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) }
                        )
                        AppScreen.API_CLOUD_SETTINGS -> ApiCloudSettingsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) },
                            onNavigateToDeepResearch = { viewModel.setScreen(AppScreen.DEEP_RESEARCH_SETTINGS) },
                            onNavigateToConnectors = { viewModel.setScreen(AppScreen.CONNECTORS) }
                        )
                        AppScreen.DEEP_RESEARCH_SETTINGS -> DeepResearchSettingsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.API_CLOUD_SETTINGS) }
                        )
                        AppScreen.VOICE_SETTINGS -> VoiceSettingsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.VOICE_AI_MODELS) }
                        )
                        AppScreen.VOICE_AI_MODELS -> VoiceModelsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) },
                            onNavigateToVoiceSettings = { viewModel.setScreen(AppScreen.VOICE_SETTINGS) }
                        )
                        AppScreen.AI_IDENTITY -> AiIdentityScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) }
                        )
                        AppScreen.FULL_DEVICE_CONTROL -> FullDeviceControlScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) }
                        )
                        AppScreen.CALL_SMS_ANNOUNCER -> CallSmsAnnouncerScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) }
                        )
                        AppScreen.BG_COLOR_CUSTOMIZER -> MjBgCustomizerScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) }
                        )
                        AppScreen.MAYA_SUITE -> MayaFleetScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.HOME) },
                            onNavigateToPcControl = { viewModel.setScreen(AppScreen.PC_CONNECT) },
                            onNavigateToFileManager = { viewModel.setScreen(AppScreen.FILE_MANAGER) }
                        )
                        AppScreen.PC_CONNECT -> PcControlScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) }
                        )
                        AppScreen.FILE_MANAGER -> FileManagerScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.SETTINGS) }
                        )
                        AppScreen.CONVERSATION_DATABASE -> ConversationMemoryScreen(
                            viewModel = viewModel
                        )
                        AppScreen.PHONE_LAUNCHER -> MyraPhoneLauncherScreen(
                            viewModel = viewModel,
                            onNavigate = { viewModel.setScreen(it) },
                            onExitLauncher = { viewModel.setScreen(AppScreen.HOME) }
                        )
                        AppScreen.SETTINGS,
                        AppScreen.VOICE_AUTH,
                        AppScreen.WAKE_WORD,
                        AppScreen.INTELLIGENCE_MODES,
                        AppScreen.LICENSE_ACTIVATION,
                        AppScreen.SUBSCRIPTION,
                        AppScreen.USER_PROFILE,
                        AppScreen.BATCH_UPDATE,
                        AppScreen.ACCOUNT -> SettingsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { viewModel.setScreen(AppScreen.HOME) },
                            onNavigate = { viewModel.setScreen(it) }
                        )
                    }
                }

                // Dialogs
                when (activeDialog) {
                    "VOICE_MODE" -> VoiceModeDialog(
                        viewModel = viewModel,
                        orbSettings = orbSettings,
                        onDismiss = { viewModel.closeDialog() },
                        onSendCommand = { cmd -> viewModel.sendUserMessage(cmd) }
                    )
                    "VISUAL_LENS" -> VisualLensDialog(
                        onDismiss = { viewModel.closeDialog() },
                        onAnalyze = { query -> viewModel.sendUserMessage(query) }
                    )
                    "JARVIS_MODE" -> JarvisModeDialog(
                        onDismiss = { viewModel.closeDialog() },
                        onSendCommand = { cmd -> viewModel.sendUserMessage(cmd) }
                    )
                    "DEEP_RESEARCH" -> DeepResearchDialog(
                        onDismiss = { viewModel.closeDialog() },
                        onRunResearch = { topic -> viewModel.sendUserMessage("Deep Research request: $topic") }
                    )
                    "IMAGE_GEN" -> VisualLensDialog(
                        onDismiss = { viewModel.closeDialog() },
                        onAnalyze = { prompt -> viewModel.sendUserMessage("Generate visual image: $prompt") }
                    )
                    "AMOLED_MAP" -> AmoledMapDialog(
                        onDismiss = { viewModel.closeDialog() }
                    )
                    "MISSION_MODE" -> MissionModeDialog(
                        onDismiss = { viewModel.closeDialog() },
                        onExecuteMission = { mission -> viewModel.sendUserMessage(mission) }
                    )
                    "CODING_CORES" -> CodingCoresDialog(
                        onDismiss = { viewModel.closeDialog() },
                        onGenerateCode = { prompt -> viewModel.sendUserMessage(prompt) }
                    )
                    "MYRA_SPECS" -> MyraSpecsDialog(
                        onDismiss = { viewModel.closeDialog() }
                    )
                    "NOTIFICATIONS", "SYSTEM_HEALTH" -> NotificationsCenterDialog(
                        onDismiss = { viewModel.closeDialog() }
                    )
                }
            }
        }

        // Full Screen Intro Video Splash Player (with Cancel / Skip option)
        AnimatedVisibility(
            visible = isIntroVideoShowing,
            enter = androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.fadeOut()
        ) {
            IntroVideoSplashScreen(
                viewModel = viewModel,
                onDismiss = { viewModel.dismissIntroVideo() }
            )
        }
    }
}

@Composable
fun MyraBottomNavigationBar(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit,
    onCenterOrbClick: () -> Unit
) {
    Surface(
        color = Color(0xFF0D0C14),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(Color.Transparent, Color(0xFFFF2A55).copy(alpha = 0.5f), Color.Transparent))
        ),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("myra_bottom_nav")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTabItem(
                label = "Home",
                icon = Icons.Default.Home,
                isSelected = currentScreen == AppScreen.HOME,
                onClick = { onSelectScreen(AppScreen.HOME) }
            )

            NavTabItem(
                label = "Chat",
                icon = Icons.Default.ChatBubble,
                isSelected = currentScreen == AppScreen.CHAT,
                onClick = { onSelectScreen(AppScreen.CHAT) }
            )

            // Center Floating Pulsing Glowing Mic Button from screenshot
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                Color(0xFFFF3366),
                                Color(0xFFE50914),
                                Color(0xFF700010),
                                Color(0xFF140003)
                            )
                        )
                    )
                    .border(2.dp, Color(0xFFFF4D79), CircleShape)
                    .clickable(onClick = onCenterOrbClick)
                    .testTag("center_floating_orb_fab"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Quick Voice Input",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            NavTabItem(
                label = "Triggers",
                icon = Icons.Default.Bolt,
                isSelected = currentScreen == AppScreen.TRIGGERS,
                onClick = { onSelectScreen(AppScreen.TRIGGERS) }
            )

            val isSettingsActive = currentScreen == AppScreen.SETTINGS ||
                    currentScreen == AppScreen.PERMISSIONS ||
                    currentScreen == AppScreen.CONNECTORS ||
                    currentScreen == AppScreen.ORB_CUSTOMIZE
            NavTabItem(
                label = "Settings",
                icon = Icons.Default.Settings,
                isSelected = isSettingsActive,
                selectedTint = Color(0xFF38BDF8),
                onClick = { onSelectScreen(AppScreen.SETTINGS) }
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    selectedTint: Color = MyraRedGlow,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("nav_tab_${label.lowercase()}")
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) selectedTint else Color(0xFF71717A),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = if (isSelected) (if (selectedTint != MyraRedGlow) selectedTint else Color.White) else Color(0xFF71717A),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
