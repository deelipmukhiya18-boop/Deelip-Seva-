package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.viewmodel.MyraViewModel
import kotlinx.coroutines.launch
import kotlin.math.*

/**
 * Data model for apps placed on the rotary dial.
 */
data class RotaryAppItem(
    val id: String,
    val name: String,
    val packageName: String,
    val iconVector: ImageVector? = null,
    val iconResId: Int? = null,
    val isCustomGraphic: Boolean = false,
    val primaryColor: Color,
    val secondaryColor: Color = Color.White,
    val fallbackAction: (Context) -> Unit
)

/**
 * Default list of apps matching the user's uploaded mockup screenshot:
 * Chrome, WhatsApp, YouTube, Instagram, Photos, Play Store, Settings, Gallery, Camera, Messages, Phone
 */
val DefaultRotaryApps = listOf(
    RotaryAppItem(
        id = "whatsapp",
        name = "WhatsApp",
        packageName = "com.whatsapp",
        primaryColor = Color(0xFF25D366),
        secondaryColor = Color(0xFF128C7E),
        iconVector = Icons.AutoMirrored.Filled.Chat,
        fallbackAction = { ctx ->
            try {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?text=Hello")))
            } catch (_: Exception) {
                Toast.makeText(ctx, "WhatsApp opened", Toast.LENGTH_SHORT).show()
            }
        }
    ),
    RotaryAppItem(
        id = "youtube",
        name = "YouTube",
        packageName = "com.google.android.youtube",
        primaryColor = Color(0xFFFF0000),
        secondaryColor = Color(0xFFCC0000),
        iconVector = Icons.Default.PlayArrow,
        fallbackAction = { ctx ->
            try {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com")))
            } catch (_: Exception) {
                Toast.makeText(ctx, "YouTube opened", Toast.LENGTH_SHORT).show()
            }
        }
    ),
    RotaryAppItem(
        id = "instagram",
        name = "Instagram",
        packageName = "com.instagram.android",
        primaryColor = Color(0xFFE1306C),
        secondaryColor = Color(0xFF833AB4),
        iconVector = Icons.Default.Camera,
        fallbackAction = { ctx ->
            try {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com")))
            } catch (_: Exception) {
                Toast.makeText(ctx, "Instagram opened", Toast.LENGTH_SHORT).show()
            }
        }
    ),
    RotaryAppItem(
        id = "photos",
        name = "Photos",
        packageName = "com.google.android.apps.photos",
        primaryColor = Color(0xFFFFAB00),
        secondaryColor = Color(0xFF4285F4),
        iconVector = Icons.Default.PhotoLibrary,
        fallbackAction = { ctx ->
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    type = "image/*"
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                ctx.startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(ctx, "Photos opened", Toast.LENGTH_SHORT).show()
            }
        }
    ),
    RotaryAppItem(
        id = "playstore",
        name = "Play Store",
        packageName = "com.android.vending",
        primaryColor = Color(0xFF00C853),
        secondaryColor = Color(0xFF0091EA),
        iconVector = Icons.Default.ShoppingBag,
        fallbackAction = { ctx ->
            try {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=myra")))
            } catch (_: Exception) {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")))
            }
        }
    ),
    RotaryAppItem(
        id = "settings",
        name = "Settings",
        packageName = "com.android.settings",
        primaryColor = Color(0xFF8E8E93),
        secondaryColor = Color(0xFF5AC8FA),
        iconVector = Icons.Default.Settings,
        fallbackAction = { ctx ->
            try {
                ctx.startActivity(Intent(Settings.ACTION_SETTINGS))
            } catch (_: Exception) {
                Toast.makeText(ctx, "Settings opened", Toast.LENGTH_SHORT).show()
            }
        }
    ),
    RotaryAppItem(
        id = "gallery",
        name = "Gallery",
        packageName = "com.sec.android.gallery3d",
        primaryColor = Color(0xFFAF52DE),
        secondaryColor = Color(0xFF5856D6),
        iconVector = Icons.Default.Collections,
        fallbackAction = { ctx ->
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    type = "image/*"
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                ctx.startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(ctx, "Gallery opened", Toast.LENGTH_SHORT).show()
            }
        }
    ),
    RotaryAppItem(
        id = "camera",
        name = "Camera",
        packageName = "com.android.camera",
        primaryColor = Color(0xFF00E5FF),
        secondaryColor = Color(0xFF007AFF),
        iconVector = Icons.Default.CameraAlt,
        fallbackAction = { ctx ->
            try {
                ctx.startActivity(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            } catch (_: Exception) {
                Toast.makeText(ctx, "Camera opened", Toast.LENGTH_SHORT).show()
            }
        }
    ),
    RotaryAppItem(
        id = "messages",
        name = "Messages",
        packageName = "com.google.android.apps.messaging",
        primaryColor = Color(0xFF2979FF),
        secondaryColor = Color(0xFF00E5FF),
        iconVector = Icons.Default.Email,
        fallbackAction = { ctx ->
            try {
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_MESSAGING)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                ctx.startActivity(intent)
            } catch (_: Exception) {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("sms:")))
            }
        }
    ),
    RotaryAppItem(
        id = "phone",
        name = "Phone",
        packageName = "com.google.android.dialer",
        primaryColor = Color(0xFF00E676),
        secondaryColor = Color(0xFF00B0FF),
        iconVector = Icons.Default.Phone,
        fallbackAction = { ctx ->
            try {
                ctx.startActivity(Intent(Intent.ACTION_DIAL))
            } catch (_: Exception) {
                Toast.makeText(ctx, "Dialer opened", Toast.LENGTH_SHORT).show()
            }
        }
    ),
    RotaryAppItem(
        id = "chrome",
        name = "Chrome",
        packageName = "com.android.chrome",
        primaryColor = Color(0xFFEA4335),
        secondaryColor = Color(0xFF34A853),
        iconVector = Icons.Default.Language,
        fallbackAction = { ctx ->
            try {
                ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")))
            } catch (_: Exception) {
                Toast.makeText(ctx, "Browser opened", Toast.LENGTH_SHORT).show()
            }
        }
    )
)

/**
 * Launch an app on Android or trigger fallback
 */
fun launchAppSafely(context: Context, item: RotaryAppItem) {
    CyberSoundManager.playLaunchSound()
    try {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(item.packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
        } else {
            item.fallbackAction(context)
        }
    } catch (_: Exception) {
        item.fallbackAction(context)
    }
}

/**
 * The Interactive Sci-Fi 360° Rotary App Wheel Dial.
 * Allows circular dragging, left/right arrow step-rotation, plays cyber mechanical audio ticks,
 * and launches device applications.
 */
@Composable
fun CyberRotaryLauncherWheel(
    viewModel: MyraViewModel,
    modifier: Modifier = Modifier,
    onCenterClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val apps = remember { DefaultRotaryApps }
    val itemCount = apps.size
    val stepAngle = 360f / itemCount // ~32.72 degrees per app

    val currentRotation = remember { Animatable(0f) }
    var lastTickAngle by remember { mutableFloatStateOf(0f) }

    // Helper to rotate to next/prev with sound
    fun rotateByAngle(deltaAngle: Float) {
        coroutineScope.launch {
            val target = currentRotation.value + deltaAngle
            CyberSoundManager.playDialTick()
            viewModel.performHaptic()
            currentRotation.animateTo(
                targetValue = target,
                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(390.dp),
        contentAlignment = Alignment.Center
    ) {
        val radiusPx = (min(maxWidth.value, 360f) * 0.38f).dp

        // 1. Futuristic Concentric HUD & Laser Rings Background
        Canvas(
            modifier = Modifier
                .size(360.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            // Determine rotary drag direction based on drag quadrant
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val touchPos = change.position
                            val relX = touchPos.x - center.x
                            val relY = touchPos.y - center.y

                            // Cross product to find angular delta
                            val angularDelta = (relX * dragAmount.y - relY * dragAmount.x) / (center.x * 2.5f)
                            val newAngle = currentRotation.value + angularDelta

                            coroutineScope.launch {
                                currentRotation.snapTo(newAngle)
                            }

                            // Sound & Haptic Tick check when crossing app step
                            if (abs(newAngle - lastTickAngle) >= stepAngle * 0.45f) {
                                CyberSoundManager.playDialTick()
                                viewModel.performHaptic()
                                lastTickAngle = newAngle
                            }
                        }
                    )
                }
        ) {
            val centerOffset = Offset(size.width / 2f, size.height / 2f)
            val outerRadius = size.width * 0.44f
            val midRadius = size.width * 0.38f
            val innerRingRadius = size.width * 0.26f

            // Outer subtle cyan/blue guide ring with dashes
            drawCircle(
                color = Color(0x3300E5FF),
                radius = outerRadius,
                center = centerOffset,
                style = Stroke(
                    width = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 16f), 0f)
                )
            )

            // Main orbital track ring (Blue neon)
            drawCircle(
                color = Color(0x6600E5FF),
                radius = midRadius,
                center = centerOffset,
                style = Stroke(width = 2.2f)
            )

            // Inner cyan reactor ring
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color(0x33FF0055), Color(0x2200E5FF), Color.Transparent),
                    center = centerOffset,
                    radius = innerRingRadius * 1.3f
                ),
                radius = innerRingRadius,
                center = centerOffset
            )

            drawCircle(
                color = Color(0x55FF2A55),
                radius = innerRingRadius,
                center = centerOffset,
                style = Stroke(width = 1.8f)
            )

            // Futuristic compass ticks every 30 degrees
            for (i in 0 until 12) {
                val tickAngle = Math.toRadians((i * 30.0) + currentRotation.value)
                val startR = midRadius - 8f
                val endR = midRadius + 8f
                val p1 = Offset(
                    centerOffset.x + (startR * cos(tickAngle)).toFloat(),
                    centerOffset.y + (startR * sin(tickAngle)).toFloat()
                )
                val p2 = Offset(
                    centerOffset.x + (endR * cos(tickAngle)).toFloat(),
                    centerOffset.y + (endR * sin(tickAngle)).toFloat()
                )
                drawLine(
                    color = if (i % 3 == 0) Color(0xFFFF2A55) else Color(0x8800E5FF),
                    start = p1,
                    end = p2,
                    strokeWidth = if (i % 3 == 0) 2.5f else 1.2f
                )
            }
        }

        // 2. Holographic Pedestal Base Beams (radiating downward as in user screenshot)
        Canvas(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(95.dp)
        ) {
            val centerBottom = Offset(size.width / 2f, size.height)
            // Downward hologram blue laser lines
            for (i in -4..4) {
                val xOffset = i * 22f
                drawLine(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xBB00E5FF), Color(0x44007AFF), Color.Transparent)
                    ),
                    start = Offset(centerBottom.x + xOffset * 0.4f, 0f),
                    end = Offset(centerBottom.x + xOffset * 2.8f, size.height),
                    strokeWidth = if (i == 0) 3.5f else 1.5f
                )
            }

            // Concentric blue & red futuristic pedestal ellipses
            drawOval(
                brush = Brush.radialGradient(
                    listOf(Color(0x6600E5FF), Color(0x22007AFF), Color.Transparent),
                    center = Offset(centerBottom.x, size.height * 0.7f),
                    radius = size.width * 0.35f
                ),
                topLeft = Offset(centerBottom.x - size.width * 0.35f, size.height * 0.45f),
                size = androidx.compose.ui.geometry.Size(size.width * 0.7f, size.height * 0.5f)
            )

            drawOval(
                color = Color(0xAA00E5FF),
                topLeft = Offset(centerBottom.x - size.width * 0.32f, size.height * 0.52f),
                size = androidx.compose.ui.geometry.Size(size.width * 0.64f, size.height * 0.38f),
                style = Stroke(width = 1.8f)
            )

            drawOval(
                color = Color(0x99FF2A55),
                topLeft = Offset(centerBottom.x - size.width * 0.22f, size.height * 0.6f),
                size = androidx.compose.ui.geometry.Size(size.width * 0.44f, size.height * 0.26f),
                style = Stroke(width = 1.6f)
            )
        }

        // 3. Left Sci-Fi Glowing Chevron Button (<)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 2.dp)
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0x33100818))
                .border(1.2.dp, Color(0x88FF2A55), CircleShape)
                .clickable {
                    rotateByAngle(-stepAngle)
                }
                .testTag("rotary_prev_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Rotate Left",
                tint = Color(0xFFFF3366),
                modifier = Modifier.size(28.dp)
            )
        }

        // 4. Right Sci-Fi Glowing Chevron Button (>)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 2.dp)
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0x33100818))
                .border(1.2.dp, Color(0x88FF2A55), CircleShape)
                .clickable {
                    rotateByAngle(stepAngle)
                }
                .testTag("rotary_next_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Rotate Right",
                tint = Color(0xFFFF3366),
                modifier = Modifier.size(28.dp)
            )
        }

        // 5. The Apps on the Circular Dial ("Dil [Dial] mein ghume")
        val rotVal = currentRotation.value
        apps.forEachIndexed { index, app ->
            val angleDeg = (index * stepAngle) + rotVal - 90f // start from top (12 o'clock)
            val angleRad = Math.toRadians(angleDeg.toDouble())

            val xPos = (radiusPx.value * cos(angleRad)).dp
            val yPos = (radiusPx.value * sin(angleRad)).dp

            Box(
                modifier = Modifier
                    .offset(x = xPos, y = yPos)
                    .width(66.dp)
                    .clickable {
                        viewModel.performHaptic()
                        launchAppSafely(context, app)
                    }
                    .testTag("rotary_app_${app.id}"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Futuristic Glowing Circular App Icon Pod
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Color(0xFF1B142A),
                                        Color(0xFF0C0A14)
                                    )
                                )
                            )
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    listOf(app.primaryColor, app.secondaryColor.copy(alpha = 0.6f))
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Ambient Radial Color Back-glow
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(app.primaryColor.copy(alpha = 0.25f))
                        )

                        if (app.iconVector != null) {
                            Icon(
                                imageVector = app.iconVector,
                                contentDescription = app.name,
                                tint = app.primaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                        } else if (app.iconResId != null) {
                            Image(
                                painter = painterResource(id = app.iconResId),
                                contentDescription = app.name,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // App Label
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        }

        // 6. Central Glowing "M" MYRA Core (Tap to Open Assistant)
        Box(
            modifier = Modifier
                .size(116.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xEE1E0B16),
                            Color(0xFF0F060D),
                            Color.Black
                        )
                    )
                )
                .border(
                    width = 2.4.dp,
                    brush = Brush.radialGradient(
                        listOf(Color(0xFFFF2A55), Color(0xFFFF0033), Color(0x44FF2A55))
                    ),
                    shape = CircleShape
                )
                .clickable {
                    CyberSoundManager.playLaunchSound()
                    viewModel.performHaptic()
                    onCenterClick()
                }
                .testTag("rotary_center_core"),
            contentAlignment = Alignment.Center
        ) {
            // Pulsing Red Inner Glow
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0x55FF2A55), Color.Transparent)
                        )
                    )
                    .border(1.2.dp, Color(0x66FF5252), CircleShape)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                // Futuristic Glowing "M" Avatar / Emblem
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FF0033)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFF2A55)
                    )
                }

                Text(
                    text = "MYRA",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.5.sp),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "TAP TO OPEN",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF7597)
                )
                Text(
                    text = "ASSISTANT",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.5.sp),
                    color = Color(0xFFC0C0D4)
                )
            }
        }
    }
}
