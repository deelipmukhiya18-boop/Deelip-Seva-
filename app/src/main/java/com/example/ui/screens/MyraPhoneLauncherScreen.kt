package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppScreen
import com.example.ui.components.CyberAppDrawerDialog
import com.example.ui.components.CyberRotaryLauncherWheel
import com.example.ui.components.CyberSoundManager
import com.example.ui.theme.MyraRedGlow
import com.example.viewmodel.MyraViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MyraPhoneLauncherScreen(
    viewModel: MyraViewModel,
    onNavigate: (AppScreen) -> Unit,
    onExitLauncher: () -> Unit
) {
    val context = LocalContext.current
    var showAppDrawer by remember { mutableStateOf(false) }

    val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greetingTitle = remember(currentHour) {
        when (currentHour) {
            in 5..11 -> "GOOD MORNING"
            in 12..16 -> "GOOD AFTERNOON"
            in 17..21 -> "GOOD EVENING"
            else -> "GOOD NIGHT"
        }
    }
    val liveDateText = remember {
        val sdf = SimpleDateFormat("EEE, d MMM", Locale.ENGLISH)
        sdf.format(Date()).uppercase()
    }

    if (showAppDrawer) {
        CyberAppDrawerDialog(
            viewModel = viewModel,
            onDismiss = { showAppDrawer = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF040209),
                        Color(0xFF0C0716),
                        Color(0xFF05030A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Navigation & Mode Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Exit to Myra app button
                Surface(
                    onClick = {
                        viewModel.performHaptic()
                        CyberSoundManager.playDialTick()
                        onExitLauncher()
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0x33FF2A55),
                    border = BorderStroke(1.dp, Color(0xFFFF2A55).copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Exit to Myra App",
                            tint = Color(0xFFFF2A55),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "MYRA APP",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Phone Active Status indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676))
                    )
                    Text(
                        text = "PHONE OS ACTIVE",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.sp
                    )
                }
            }

            // 1. Top Cyber HUD (Greeting BOSS, Avatar, Weather)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left HUD: Dynamic Greeting & Quotes
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigate(AppScreen.AI_IDENTITY) }
                ) {
                    Text(
                        text = greetingTitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = Color(0xFFA0A0B2),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "BOSS",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "I'M ALWAYS WITH YOU",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = Color(0xFF00E5FF),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "“ IDEAS INTO REALITY ”",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        color = Color(0xFF8A8AA0)
                    )
                    Text(
                        text = "MYRA AI",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                        color = Color(0xFFFF2A55),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Center: Holographic Commander Avatar with glowing red chest core
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                listOf(Color(0xFFFF2A55), Color(0xFF00E5FF), Color(0xFFFF0055), Color(0xFFFF2A55))
                            ),
                            shape = CircleShape
                        )
                        .clickable {
                            viewModel.performHaptic()
                            onNavigate(AppScreen.AI_IDENTITY)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_myra_avatar),
                        contentDescription = "Myra Commander",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                    // Glowing Red Chest Reactor Emblem
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 2.dp)
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF0033))
                            .border(1.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("M", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                    }
                }

                // Right HUD: Date, Weather, Hyderabad
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = liveDateText,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFFFF2A55),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "Hyderabad",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color(0xFFC0C0D4)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = null,
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "24°C Cloudy",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "A SMARTER TOMORROW",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.5.sp),
                        color = Color(0xFF8A8AA0)
                    )
                }
            }

            // 2. Central Rotary Dial Wheel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CyberRotaryLauncherWheel(
                    viewModel = viewModel,
                    onCenterClick = {
                        viewModel.openDialog("VOICE_MODE")
                    }
                )
            }

            // 3. Quick Launcher Dock: APPS, THEMES, WALLPAPER, TOOLS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LauncherDockItem(
                    icon = Icons.Default.Apps,
                    label = "APPS",
                    color = Color(0xFF00E5FF),
                    onClick = {
                        viewModel.performHaptic()
                        CyberSoundManager.playLaunchSound()
                        showAppDrawer = true
                    }
                )
                LauncherDockItem(
                    icon = Icons.Default.RocketLaunch,
                    label = "THEMES",
                    color = Color(0xFFFF2A55),
                    onClick = {
                        viewModel.performHaptic()
                        CyberSoundManager.playDialTick()
                        onNavigate(AppScreen.AURA_CONTROL)
                    }
                )
                LauncherDockItem(
                    icon = Icons.Default.Image,
                    label = "WALLPAPER",
                    color = Color(0xFF00E676),
                    onClick = {
                        viewModel.performHaptic()
                        CyberSoundManager.playDialTick()
                        onNavigate(AppScreen.BG_COLOR_CUSTOMIZER)
                    }
                )
                LauncherDockItem(
                    icon = Icons.Default.Window,
                    label = "TOOLS",
                    color = Color(0xFFFFAB00),
                    onClick = {
                        viewModel.performHaptic()
                        CyberSoundManager.playDialTick()
                        onNavigate(AppScreen.MAYA_SUITE)
                    }
                )
            }

            // 4. MYRA AI Launcher Footer / Swipe indicator
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowUp,
                    contentDescription = "Swipe up for all apps",
                    tint = Color(0xFF00E5FF),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            viewModel.performHaptic()
                            CyberSoundManager.playLaunchSound()
                            showAppDrawer = true
                        }
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "MYRA AI LAUNCHER",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFF2A55),
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "•",
                        color = Color(0xFF666680)
                    )
                    Text(
                        text = "SMARTER • FASTER • FUTURISTIC",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = Color(0xFF9E9EB2),
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LauncherDockItem(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0x33140C20),
        border = BorderStroke(1.2.dp, color.copy(alpha = 0.55f)),
        modifier = Modifier
            .width(76.dp)
            .height(62.dp)
            .testTag("launcher_dock_$label")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
