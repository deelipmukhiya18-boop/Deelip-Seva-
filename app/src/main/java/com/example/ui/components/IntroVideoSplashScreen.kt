package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.R
import com.example.model.OrbStyle
import com.example.viewmodel.MyraViewModel
import kotlinx.coroutines.delay

@Composable
fun IntroVideoSplashScreen(
    viewModel: MyraViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val orbSettings by viewModel.orbSettings.collectAsState()
    val activeStyle = orbSettings.style
    val primaryColor = Color(activeStyle.primaryColorHex)
    val secondaryColor = Color(activeStyle.secondaryColorHex)

    // Startup Presentation Mode: "3D_HOLOGRAPH" or "VIDEO"
    var startupMode by remember { mutableStateOf("3D_HOLOGRAPH") }

    var isVideoReady by remember { mutableStateOf(false) }
    var isVideoPlaying by remember { mutableStateOf(true) }
    var progressSeconds by remember { mutableStateOf(0) }
    var totalDurationSeconds by remember { mutableStateOf(10) }
    var videoViewInstance by remember { mutableStateOf<VideoView?>(null) }

    // Pulsing cyber glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "video_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val coreScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_scale"
    )

    // Auto-advance timer (5 seconds for 3D Hologram, or video length)
    LaunchedEffect(startupMode, isVideoPlaying) {
        progressSeconds = 0
        val maxDuration = if (startupMode == "3D_HOLOGRAPH") 6 else totalDurationSeconds
        while (progressSeconds < maxDuration) {
            delay(1000L)
            progressSeconds++
        }
        if (progressSeconds >= maxDuration) {
            onDismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07040D))
            .testTag("intro_video_splash_screen")
    ) {
        if (startupMode == "3D_HOLOGRAPH") {
            // ==========================================
            // 3D HOLOGRAPHIC LAUNCH SEQUENCE
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(
                                primaryColor.copy(alpha = 0.25f),
                                Color(0xFF0C0816),
                                Color(0xFF05030A)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Background Ambient Glow & Starburst Grid
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Space for Navigation Bar
                    Spacer(modifier = Modifier.height(50.dp))

                    // Central 3D Interactive Glowing Core
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.clickable {
                            viewModel.performHaptic()
                            onDismiss()
                        }
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0x551E1138),
                            border = BorderStroke(1.dp, primaryColor.copy(alpha = glowAlpha))
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
                                        .background(primaryColor)
                                )
                                Text(
                                    text = "3D NEURAL CORE: ${activeStyle.displayName.uppercase()}",
                                    color = Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        // Central 3D Animated Hologram
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .testTag("splash_3d_animated_orb"),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedOrb(
                                settings = orbSettings,
                                isListening = true,
                                isSpeaking = false,
                                onClick = {
                                    viewModel.performHaptic()
                                    onDismiss()
                                }
                            )
                        }

                        // Cyber Telemetry Readouts
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "MIRA • MJP",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 3.sp
                            )
                            Text(
                                text = "3D Holographic Core Online • Ready to Serve",
                                color = secondaryColor,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Tap core to enter immediately",
                                color = Color(0xFF8E8EA8),
                                fontSize = 11.5.sp
                            )
                        }
                    }

                    // Bottom Enter Button & Auto Progress
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                viewModel.performHaptic()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(26.dp),
                            border = BorderStroke(1.5.dp, secondaryColor.copy(alpha = 0.8f)),
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(52.dp)
                                .testTag("launch_3d_system_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RocketLaunch,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Launch 3D System / प्रवेश करें",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Progress Indicator
                        val progressFraction = (progressSeconds.toFloat() / 6f).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = primaryColor,
                            trackColor = Color.White.copy(alpha = 0.15f)
                        )
                    }
                }
            }
        } else {
            // ==========================================
            // VIDEO PLAYER MODE
            // ==========================================
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        val rawId = ctx.resources.getIdentifier("mj_intro_video", "raw", ctx.packageName)
                        if (rawId != 0) {
                            val videoUri = Uri.parse("android.resource://${ctx.packageName}/$rawId")
                            setVideoURI(videoUri)
                            setOnPreparedListener { mp ->
                                mp.isLooping = false
                                totalDurationSeconds = (mp.duration / 1000).coerceAtLeast(6)
                                isVideoReady = true
                                start()
                            }
                            setOnCompletionListener {
                                onDismiss()
                            }
                            setOnErrorListener { _, _, _ ->
                                isVideoReady = false
                                true
                            }
                        } else {
                            isVideoReady = false
                        }
                        videoViewInstance = this
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (!isVideoReady) {
                Image(
                    painter = painterResource(id = R.drawable.img_mj_intro_cinematic),
                    contentDescription = "MJ Empress Intro",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Video Controls Bottom Overlay
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "MJ • EMPRESS AI",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Neural Cybernetic Intelligence • Ready to Serve",
                        color = Color(0xFFD8B4FE),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                val progressFraction = (progressSeconds.toFloat() / totalDurationSeconds.toFloat()).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color(0xFFB026FF),
                    trackColor = Color.White.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "00:${if (progressSeconds < 10) "0$progressSeconds" else "$progressSeconds"} / 00:$totalDurationSeconds",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    TextButton(
                        onClick = {
                            viewModel.performHaptic()
                            try {
                                videoViewInstance?.stopPlayback()
                            } catch (_: Exception) {}
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = "App shuru karein →",
                            color = Color(0xFF00E5FF),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // ==========================================
        // TOP CONTROLS (Mode Switcher & Skip Button)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 18.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mode Toggle Pill: 3D Hologram vs Video
            Surface(
                color = Color(0xEE160F2B),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF382958))
            ) {
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        onClick = {
                            startupMode = "3D_HOLOGRAPH"
                            viewModel.performHaptic()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (startupMode == "3D_HOLOGRAPH") primaryColor else Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ViewInAr,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "3D Core",
                                color = Color.White,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        onClick = {
                            startupMode = "VIDEO"
                            viewModel.performHaptic()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (startupMode == "VIDEO") Color(0xFFB026FF) else Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Video",
                                color = Color.White,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Cancel / Skip Button
            Button(
                onClick = {
                    viewModel.performHaptic()
                    try {
                        videoViewInstance?.stopPlayback()
                    } catch (_: Exception) {}
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xDD2A142A),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0x66FF5252)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("cancel_intro_video_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = Color(0xFFFF6688),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Skip (छोड़ें)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
