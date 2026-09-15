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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppScreen
import com.example.model.OrbSettings
import com.example.model.OrbStyle
import com.example.ui.components.AnimatedOrb
import com.example.ui.components.CyberSoundManager
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@Composable
fun HomeScreen(
    viewModel: MyraViewModel,
    orbSettings: OrbSettings,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val isFlashlightOn by viewModel.isFlashlightOn.collectAsState()
    val masterName by viewModel.masterName.collectAsState()
    val isApiKeyConfigured by viewModel.isApiKeyConfigured.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 3D Cybernetic Geometric Circuit Background (matching user screenshot)
        Image(
            painter = painterResource(id = R.drawable.img_cyber_vortex_bg_1789293161647),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark Sci-Fi Scrim for high contrast readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xDC080710),
                            Color(0xEA0B0916),
                            Color(0xF6080710)
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // 1. Top Header with User Greeting and Notification Bell
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.clickable { onNavigate(AppScreen.AI_IDENTITY) }
                    ) {
                        Text(
                            text = "Hello, $masterName",
                            style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "How can I assist you today?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MyraTextSecondary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Notification Bell with Red Glow Badge
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0x4414141E))
                                .border(1.2.dp, Color(0x66FF2A55), CircleShape)
                                .clickable {
                                    viewModel.performHaptic()
                                    viewModel.openDialog("NOTIFICATIONS")
                                }
                                .testTag("home_notification_bell"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            // Red Notification Badge Dot
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 10.dp, end = 10.dp)
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(MyraRedGlow)
                                    .border(1.5.dp, Color(0xFF14141E), CircleShape)
                            )
                        }

                        // Circular MJ App Logo Avatar with Red Ring Border
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .border(2.dp, Color(0xFFFF2A55), CircleShape)
                                .clickable {
                                    viewModel.performHaptic()
                                    onNavigate(AppScreen.AI_IDENTITY)
                                }
                                .testTag("home_top_mj_logo"),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.mj_app_logo),
                                contentDescription = "MJ AI Logo",
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        }
                    }
                }
            }

        // Voice Gated Reminder Banner if API Key is not configured
        if (!isApiKeyConfigured) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { viewModel.openDialog("VOICE_MODE") },
                    color = Color(0xFF241018),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(Color(0xFFFFB300), MyraRed))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF38141F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Voice Mode: API Key Add Karein",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "MJ voice mein baat tab karegi jab aap Gemini ya OpenAI API key add karenge.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFC0C0D4),
                                fontSize = 11.sp
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MyraRed
                        ) {
                            Text(
                                text = "ADD KEY",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. Central Glowing Holographic 3D Interactive Orb & Switcher
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Active 3D Core Indicator Pill
                val activeCore = orbSettings.style
                val activePrimaryColor = Color(activeCore.primaryColorHex)

                Surface(
                    onClick = {
                        viewModel.performHaptic()
                        onNavigate(AppScreen.ORB_CUSTOMIZE)
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0x66151022),
                    border = BorderStroke(1.dp, activePrimaryColor.copy(alpha = 0.6f)),
                    modifier = Modifier.testTag("home_active_3d_core_badge")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(activePrimaryColor)
                        )
                        Text(
                            text = "3D CORE: ${activeCore.displayName.uppercase()}",
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "• ${activeCore.subtitle}",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = Color(0xFFB0B0C4)
                        )
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Customize 3D Cores",
                            tint = activePrimaryColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                // Central 3D Interactive Orb
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedOrb(
                        settings = orbSettings,
                        isListening = false,
                        isSpeaking = false,
                        onClick = {
                            viewModel.performHaptic()
                            viewModel.openDialog("VOICE_MODE")
                        }
                    )
                }
            }
        }

        // 3. Search / Command Bar: "Ask anything..." (Matching user screenshot)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { viewModel.openDialog("VOICE_MODE") }
                    .testTag("home_search_bar"),
                colors = CardDefaults.cardColors(containerColor = Color(0x551C1226)),
                border = BorderStroke(1.2.dp, Color(0x66FF2A55))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (searchQuery.isEmpty()) "Ask anything..." else searchQuery,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                        color = if (searchQuery.isEmpty()) Color(0xFF9E9EB2) else MyraTextPrimary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Camera / Image button (Opens Visual Lens)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FF2A55))
                                .border(1.dp, Color(0x66FF2A55), CircleShape)
                                .clickable {
                                    viewModel.performHaptic()
                                    viewModel.openDialog("VISUAL_LENS")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Visual Lens",
                                tint = Color(0xFFFF4D79),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Send Arrow in Red Circle
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFFFF3366), Color(0xFFE50914))
                                    )
                                )
                                .clickable {
                                    viewModel.performHaptic()
                                    if (searchQuery.isNotBlank()) {
                                        viewModel.sendUserMessage(searchQuery)
                                        searchQuery = ""
                                    } else {
                                        viewModel.openDialog("VOICE_MODE")
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 6. Section: Quick Directives Header
        item {
            Text(
                text = "Quick Directives",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // 7. Quick Directives 2x2 Grid (Deep Research, Image Search, Coding Cores, MJ Specs)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickDirectiveCard(
                        title = "Deep Research",
                        subtitle = "Analyze local",
                        icon = Icons.Default.AutoAwesome,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.performHaptic()
                            viewModel.openDialog("DEEP_RESEARCH")
                        }
                    )
                    QuickDirectiveCard(
                        title = "Image Search",
                        subtitle = "Search aesthetic",
                        icon = Icons.Default.Image,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.performHaptic()
                            viewModel.openDialog("IMAGE_GEN")
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickDirectiveCard(
                        title = "Coding Cores",
                        subtitle = "Write some Kotlin",
                        icon = Icons.Default.Code,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.performHaptic()
                            viewModel.openDialog("CODING_CORES")
                        }
                    )
                    QuickDirectiveCard(
                        title = "MJ Specs",
                        subtitle = "Who built MJ?",
                        icon = Icons.Default.Info,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.performHaptic()
                            viewModel.openDialog("MYRA_SPECS")
                        }
                    )
                }
            }
        }

        // 8. NEURAL INSIGHT Card (with Quote and Cute AI Robot Mascot)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .testTag("neural_insight_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF13131F)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF2E1520), Color(0xFF1C1C2C))
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF5252))
                            )
                            Text(
                                text = "NEURAL INSIGHT",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = Color(0xFFFF5252),
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"The best way to predict the future is to create it.\"",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Cute 3D Cyber Robot Mascot
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1A1A28))
                            .border(1.dp, Color(0xFF2D2D44), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.myra_robot_mascot_1788283813378),
                            contentDescription = "MJ Neural Mascot",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // 9. Quick System Toggles (Flashlight, Permissions, Tools Guide)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    onClick = { viewModel.toggleFlashlight() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isFlashlightOn) MyraRedDark else Color(0xFF14141E),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF242436), Color(0xFF1E1E2E))))
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashlightOn,
                            contentDescription = null,
                            tint = if (isFlashlightOn) Color.White else MyraTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isFlashlightOn) "Torch ON" else "Torch",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }

                Surface(
                    onClick = { onNavigate(AppScreen.PERMISSIONS) },
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF14141E),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF242436), Color(0xFF1E1E2E))))
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = MyraNeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Permissions",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }

                Surface(
                    onClick = { onNavigate(AppScreen.TOOLS_GUIDE) },
                    modifier = Modifier.weight(1.1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF14141E),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF242436), Color(0xFF1E1E2E))))
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = MyraNeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "340Cr+ Tools (Auto)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    categoryTag: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("quick_action_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = Color(0x551C1226)),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                listOf(
                    Color(0xFFFF2A55).copy(alpha = 0.85f),
                    Color(0xFFFF2A55).copy(alpha = 0.35f),
                    Color(0xFF881122).copy(alpha = 0.5f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glowing Red Squircle Icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0x44FF2A55), Color(0x11FF2A55))
                            )
                        )
                        .border(1.dp, Color(0x66FF2A55), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Category Tag on Top Right
                if (categoryTag != null) {
                    Text(
                        text = categoryTag,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFFB0B0C4),
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = Color(0xFF9090A6)
                )
            }
        }
    }
}

@Composable
private fun QuickDirectiveCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("directive_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF13131E)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(Color(0xFF261822), Color(0xFF1E1E2C))
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2B0A14)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MyraRedGlow,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF8A8AA0)
                )
            }
        }
    }
}

