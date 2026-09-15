package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.model.AuraSettings
import com.example.viewmodel.MyraViewModel

private val AuraScreenBg = Color(0xFF0A0A10)
private val AuraCardBg = Color(0xFF141420)
private val AuraCardBorder = Color(0xFF222232)
private val AuraCoralHeader = Color(0xFFFF7A59)
private val AuraRedIcon = Color(0xFFEF4444)
private val AuraUnlockBtnBg = Color(0xFF221118)
private val AuraUnlockBtnBorder = Color(0xFF4C1820)
private val AuraUnlockText = Color(0xFFFFA896)
private val AuraMutedText = Color(0xFFA1A1AA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraControlScreen(
    viewModel: MyraViewModel,
    auraSettings: AuraSettings,
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSignatureDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showSystemNodesDialog by remember { mutableStateOf(false) }
    var showUnlockModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AuraScreenBg,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            ) {
                // Back Button (Dark circular badge)
                Surface(
                    onClick = onNavigateBack,
                    shape = CircleShape,
                    color = Color(0xFF161622),
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.CenterStart)
                        .testTag("aura_control_back_btn")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Centered Screen Title
                Text(
                    text = "AURA CONTROL",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AuraCoralHeader,
                    letterSpacing = 2.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .testTag("aura_control_screen"),
            contentPadding = PaddingValues(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // -------------------------------------------------------------
            // Section 1: Visual Identity
            // -------------------------------------------------------------
            item {
                Text(
                    text = "Visual Identity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_launcher_core"),
                    shape = RoundedCornerShape(18.dp),
                    color = AuraCardBg,
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(AuraCardBorder)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Launcher Core",
                                tint = AuraRedIcon,
                                modifier = Modifier.size(26.dp)
                            )
                            Column {
                                Text(
                                    text = "Launcher Core",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (auraSettings.isLifetimeUnlocked) "Lifetime Premium Active" else "Lifetime Premium Required",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (auraSettings.isLifetimeUnlocked) Color(0xFF34D399) else AuraCoralHeader,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Unlock Lifetime Identity Button
                        Surface(
                            onClick = {
                                if (auraSettings.isLifetimeUnlocked) {
                                    viewModel.showToast("Lifetime Identity already active!")
                                } else {
                                    showUnlockModal = true
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = AuraUnlockBtnBg,
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(AuraUnlockBtnBorder)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_unlock_lifetime_identity")
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = AuraUnlockText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = if (auraSettings.isLifetimeUnlocked) "Lifetime Identity Unlocked" else "Unlock Lifetime Identity",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AuraUnlockText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------------------
            // Section 2: Core Interaction
            // -------------------------------------------------------------
            item {
                Text(
                    text = "Core Interaction",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Card 1: Aura Signature
            item {
                AuraInteractionCard(
                    icon = Icons.Default.GraphicEq,
                    title = "Aura Signature",
                    subtitle = "Current: ${auraSettings.signatureName}",
                    testTag = "card_aura_signature",
                    onClick = { showSignatureDialog = true }
                )
            }

            // Card 2: Language
            item {
                AuraInteractionCard(
                    icon = Icons.Default.Language,
                    title = "Language",
                    subtitle = "Current: ${auraSettings.language}",
                    testTag = "card_aura_language",
                    onClick = { showLanguageDialog = true }
                )
            }

            // Card 3: Haptic Feedback (with Toggle Switch)
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_aura_haptic"),
                    shape = RoundedCornerShape(16.dp),
                    color = AuraCardBg,
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(AuraCardBorder)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = "Haptic Feedback",
                                tint = AuraRedIcon,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = "Haptic Feedback",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Somatic vibrations",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AuraMutedText
                                )
                            }
                        }

                        Switch(
                            checked = auraSettings.hapticEnabled,
                            onCheckedChange = { viewModel.toggleAuraHaptic() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AuraRedIcon,
                                uncheckedThumbColor = Color(0xFFA1A1AA),
                                uncheckedTrackColor = Color(0xFF27273A)
                            ),
                            modifier = Modifier.testTag("switch_aura_haptic")
                        )
                    }
                }
            }

            // Card 4: System Nodes
            item {
                AuraInteractionCard(
                    icon = Icons.Default.Tune,
                    title = "System Nodes",
                    subtitle = "Access all hardware nodes",
                    testTag = "card_aura_system_nodes",
                    onClick = { showSystemNodesDialog = true }
                )
            }

            // -------------------------------------------------------------
            // Footer Branding
            // -------------------------------------------------------------
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "MJ MULTIMODAL SYSTEM",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AuraCoralHeader,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Engineered for Vikas • 2026",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraMutedText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // -------------------------------------------------------------
    // MODALS & DIALOGS
    // -------------------------------------------------------------

    // 1. Aura Signature Picker
    if (showSignatureDialog) {
        val signatures = listOf(
            "Crimson Aura",
            "Cyber Blue Aura",
            "Solar Flare Aura",
            "Emerald Pulse Aura",
            "Obsidian Void Aura",
            "Amethyst Eclipse"
        )
        AlertDialog(
            onDismissRequest = { showSignatureDialog = false },
            containerColor = AuraCardBg,
            title = {
                Text(
                    text = "Select Aura Signature",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    signatures.forEach { sig ->
                        val isSelected = auraSettings.signatureName == sig
                        Surface(
                            onClick = {
                                viewModel.updateAuraSignature(sig)
                                showSignatureDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFF2E1520) else Color(0xFF181826),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(
                                    if (isSelected) AuraRedIcon else AuraCardBorder
                                )
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = sig,
                                    color = if (isSelected) AuraCoralHeader else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = AuraCoralHeader,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSignatureDialog = false }) {
                    Text("Close", color = AuraCoralHeader)
                }
            }
        )
    }

    // 2. Language Picker
    if (showLanguageDialog) {
        val languages = listOf(
            "Auto (Hinglish)",
            "English (US)",
            "Hindi (हिंदी)",
            "English (UK)",
            "Japanese (日本語)",
            "Spanish (Español)"
        )
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            containerColor = AuraCardBg,
            title = {
                Text(
                    text = "Select Primary Language",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    languages.forEach { lang ->
                        val isSelected = auraSettings.language == lang
                        Surface(
                            onClick = {
                                viewModel.updateAuraLanguage(lang)
                                showLanguageDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFF2E1520) else Color(0xFF181826),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(
                                    if (isSelected) AuraRedIcon else AuraCardBorder
                                )
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = lang,
                                    color = if (isSelected) AuraCoralHeader else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = AuraCoralHeader,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Close", color = AuraCoralHeader)
                }
            }
        )
    }

    // 3. System Nodes Dialog
    if (showSystemNodesDialog) {
        AlertDialog(
            onDismissRequest = { showSystemNodesDialog = false },
            containerColor = AuraCardBg,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = AuraRedIcon
                    )
                    Text(
                        text = "System Hardware Nodes",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Live telemetry of connected multimodal coprocessor nodes:",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraMutedText
                    )
                    SystemNodeRow("Neural NPU Core", "98% Efficiency • 32 TOPS", true)
                    SystemNodeRow("Haptic Actuator", "Somatic Feedback Engine Ready", auraSettings.hapticEnabled)
                    SystemNodeRow("Acoustic DSP Array", "Dual Mic Array Calibrated", true)
                    SystemNodeRow("Visual HUD Pipeline", "Direct GPU Surface Shader Active", true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSystemNodesDialog = false
                        onNavigate(AppScreen.ORB_CUSTOMIZE)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AuraCoralHeader)
                ) {
                    Text("Orb Customization", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSystemNodesDialog = false }) {
                    Text("Close", color = AuraMutedText)
                }
            }
        )
    }

    // 4. Lifetime Identity Unlock Modal
    if (showUnlockModal) {
        AlertDialog(
            onDismissRequest = { showUnlockModal = false },
            containerColor = AuraCardBg,
            title = {
                Text(
                    text = "Unlock Lifetime Identity",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Activate lifetime VIP privileges for launcher customization, custom holographic cores, and deep neural telemetry.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuraMutedText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Status: Verified for Vikas (2026 Build)",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = AuraCoralHeader
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.unlockLifetimeIdentity()
                        showUnlockModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AuraCoralHeader)
                ) {
                    Text("Activate Now", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnlockModal = false }) {
                    Text("Cancel", color = AuraMutedText)
                }
            }
        )
    }
}

@Composable
private fun AuraInteractionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        color = AuraCardBg,
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(AuraCardBorder)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = AuraRedIcon,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = AuraMutedText
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SystemNodeRow(name: String, status: String, isActive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF101018))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(name, fontWeight = FontWeight.Bold, color = Color.White, style = MaterialTheme.typography.bodySmall)
            Text(status, color = AuraMutedText, style = MaterialTheme.typography.labelSmall)
        }
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isActive) Color(0xFF34D399) else Color(0xFFEF4444))
        )
    }
}
