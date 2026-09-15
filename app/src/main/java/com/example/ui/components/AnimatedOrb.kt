package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.OrbSettings
import com.example.model.OrbStyle
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun AnimatedOrb(
    settings: OrbSettings,
    isListening: Boolean,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "OrbInfinite")

    // Pulse core
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbPulse"
    )

    // Primary orbital ring rotation
    val ringRotation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing)
        ),
        label = "OrbRing1"
    )

    // Secondary orbital ring reverse rotation
    val ringRotation2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = LinearEasing)
        ),
        label = "OrbRing2"
    )

    // Tertiary orbital ring tilt
    val ringRotation3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing)
        ),
        label = "OrbRing3"
    )

    // Ambient 3D wobble tilt when idle
    val idleTiltX by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbTiltX"
    )
    val idleTiltY by infiniteTransition.animateFloat(
        initialValue = 5f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbTiltY"
    )

    // Dynamic wave amplitude when listening / speaking
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing)
        ),
        label = "WavePhase"
    )

    // User touch drag interactive 3D rotation
    var dragTiltX by remember { mutableFloatStateOf(0f) }
    var dragTiltY by remember { mutableFloatStateOf(0f) }

    val animatedDragX by animateFloatAsState(
        targetValue = dragTiltX + idleTiltX,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "animDragX"
    )
    val animatedDragY by animateFloatAsState(
        targetValue = dragTiltY + idleTiltY,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "animDragY"
    )

    val effectiveScale = settings.sizeScale * if (isListening) 1.15f else if (isSpeaking) 1.05f else 1.0f
    val orbSizeDp = (250 * effectiveScale).dp

    val baseHueColor = remember(settings.colorHue) {
        val hsv = floatArrayOf(settings.colorHue, 0.9f, 1.0f)
        val colorInt = android.graphics.Color.HSVToColor(hsv)
        Color(colorInt)
    }

    Box(
        modifier = modifier
            .testTag("animated_orb_container")
            .size(orbSizeDp)
            .graphicsLayer {
                rotationX = animatedDragX
                rotationY = animatedDragY
                cameraDistance = 16f * density
                scaleX = if (isListening) 1.04f else 1.0f
                scaleY = if (isListening) 1.04f else 1.0f
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragTiltY = (dragTiltY + dragAmount.x * 0.12f).coerceIn(-18f, 18f)
                        dragTiltX = (dragTiltX - dragAmount.y * 0.12f).coerceIn(-18f, 18f)
                    },
                    onDragEnd = {
                        dragTiltX = 0f
                        dragTiltY = 0f
                    },
                    onDragCancel = {
                        dragTiltX = 0f
                        dragTiltY = 0f
                    }
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 140.dp, color = baseHueColor),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        val activeStyle = settings.style
        val primaryColor = Color(activeStyle.primaryColorHex)
        val secondaryColor = Color(activeStyle.secondaryColorHex)

        // 1. Photographic 3D Vortex Reactor Core Texture
        Box(
            modifier = Modifier
                .size((210 * effectiveScale * pulse).dp)
                .clip(CircleShape)
                .border(
                    2.dp,
                    Brush.radialGradient(
                        listOf(primaryColor, primaryColor.copy(alpha = 0.3f), Color(0xFF101018))
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = activeStyle.drawableResId),
                contentDescription = "3D Holographic Core - ${activeStyle.displayName}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationZ = ringRotation1 * 0.25f
                    }
            )

            // Inner Vignette / Radial Light Scrim for realistic metallic vortex depth
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        // 2. Holographic HUD Canvas (Outer Calibration Arcs, Tick marks & Optical Flare)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("animated_orb_canvas")
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = (size.minDimension / 2f) * 0.44f * pulse

            // A. Deep Ambient Radial Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.55f * settings.glowIntensity),
                        secondaryColor.copy(alpha = 0.28f * settings.glowIntensity),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * 2.35f
                ),
                radius = baseRadius * 2.35f,
                center = center
            )

            // Special Geometric HUD Overlays per Style
            when (activeStyle) {
                OrbStyle.CYBER_MATRIX -> {
                    // Laser Crosshairs & Hexagonal Guide Lines (Image 2)
                    drawLine(
                        color = primaryColor.copy(alpha = 0.45f),
                        start = Offset(center.x - baseRadius * 2.1f, center.y),
                        end = Offset(center.x + baseRadius * 2.1f, center.y),
                        strokeWidth = 1.2f
                    )
                    drawLine(
                        color = primaryColor.copy(alpha = 0.45f),
                        start = Offset(center.x, center.y - baseRadius * 2.1f),
                        end = Offset(center.x, center.y + baseRadius * 2.1f),
                        strokeWidth = 1.2f
                    )
                    // Hexagonal Corner Markers
                    for (i in 0 until 6) {
                        val angle = Math.toRadians((i * 60 + ringRotation1 * 0.3f).toDouble())
                        val r = baseRadius * 1.85f
                        val pt = Offset((center.x + r * cos(angle)).toFloat(), (center.y + r * sin(angle)).toFloat())
                        drawCircle(color = secondaryColor, radius = 3.dp.toPx(), center = pt)
                    }
                }
                OrbStyle.QUANTUM_NEURAL -> {
                    // Starfield Constellation Nodes (Image 1)
                    val nodeCount = 12
                    for (i in 0 until nodeCount) {
                        val angle = Math.toRadians((i * 30 + ringRotation2 * 0.4f).toDouble())
                        val r = baseRadius * (1.55f + 0.25f * sin(i * 1.5f))
                        val pt = Offset((center.x + r * cos(angle)).toFloat(), (center.y + r * sin(angle)).toFloat())
                        drawCircle(color = if (i % 2 == 0) primaryColor else secondaryColor, radius = 3.5.dp.toPx(), center = pt)
                        drawCircle(color = Color.White, radius = 1.5.dp.toPx(), center = pt)
                    }
                }
                OrbStyle.GOLDEN_REACTOR -> {
                    // Radiating Audio Equalizer / Sacred Geometry (Image 3)
                    val rayCount = 16
                    for (i in 0 until rayCount) {
                        val angle = Math.toRadians((i * 22.5 + ringRotation1 * 0.2f).toDouble())
                        val innerR = baseRadius * 1.5f
                        val outerR = baseRadius * (1.75f + 0.15f * sin(wavePhase + i))
                        val start = Offset((center.x + innerR * cos(angle)).toFloat(), (center.y + innerR * sin(angle)).toFloat())
                        val end = Offset((center.x + outerR * cos(angle)).toFloat(), (center.y + outerR * sin(angle)).toFloat())
                        drawLine(color = primaryColor.copy(alpha = 0.7f), start = start, end = end, strokeWidth = 2f)
                    }
                }
                OrbStyle.HARMONIC_RIBBONS -> {
                    // Sinusoidal Contour Ripples (Image 4)
                    for (layer in 0..2) {
                        val layerRadius = baseRadius * (1.5f + layer * 0.16f)
                        val color = if (layer % 2 == 0) primaryColor else secondaryColor
                        drawCircle(
                            color = color.copy(alpha = 0.4f - layer * 0.1f),
                            radius = layerRadius,
                            center = center,
                            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
                else -> { /* Standard Cybernetic HUD */ }
            }

            // B. Outer HUD Calibrated Tick Mark Ring (48 futuristic radar ticks)
            val tickCount = 48
            val tickRadiusInner = baseRadius * 1.62f
            val tickRadiusOuter = baseRadius * 1.74f
            for (i in 0 until tickCount) {
                val angleDeg = i * (360f / tickCount) + ringRotation1 * 0.1f
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val isMajor = i % 6 == 0
                val innerR = if (isMajor) tickRadiusInner - 4f else tickRadiusInner
                val outerR = if (isMajor) tickRadiusOuter + 4f else tickRadiusOuter

                val start = Offset(
                    (center.x + innerR * cos(angleRad)).toFloat(),
                    (center.y + innerR * sin(angleRad)).toFloat()
                )
                val end = Offset(
                    (center.x + outerR * cos(angleRad)).toFloat(),
                    (center.y + outerR * sin(angleRad)).toFloat()
                )
                drawLine(
                    color = if (isMajor) primaryColor.copy(alpha = 0.85f) else secondaryColor.copy(alpha = 0.35f),
                    start = start,
                    end = end,
                    strokeWidth = if (isMajor) 2.2f else 1.2f,
                    cap = StrokeCap.Round
                )
            }

            // C. Segmented Outer Laser Arc 1
            rotate(ringRotation1, pivot = center) {
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(
                            primaryColor,
                            secondaryColor,
                            primaryColor.copy(alpha = 0.8f),
                            Color.Transparent,
                            primaryColor
                        ),
                        center = center
                    ),
                    startAngle = 10f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(center.x - baseRadius * 1.78f, center.y - baseRadius * 1.78f),
                    size = androidx.compose.ui.geometry.Size(baseRadius * 3.56f, baseRadius * 3.56f),
                    style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // High-intensity laser target pip
                drawArc(
                    color = Color.White,
                    startAngle = 195f,
                    sweepAngle = 18f,
                    useCenter = false,
                    topLeft = Offset(center.x - baseRadius * 1.78f, center.y - baseRadius * 1.78f),
                    size = androidx.compose.ui.geometry.Size(baseRadius * 3.56f, baseRadius * 3.56f),
                    style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // D. Counter-Rotating Tech Arc 2
            rotate(ringRotation2, pivot = center) {
                drawArc(
                    brush = Brush.linearGradient(
                        listOf(
                            secondaryColor,
                            primaryColor,
                            secondaryColor.copy(alpha = 0.2f)
                        )
                    ),
                    startAngle = 60f,
                    sweepAngle = 220f,
                    useCenter = false,
                    topLeft = Offset(center.x - baseRadius * 1.54f, center.y - baseRadius * 1.54f),
                    size = androidx.compose.ui.geometry.Size(baseRadius * 3.08f, baseRadius * 3.08f),
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Calibration Target Dot
                val dotAngle = Math.toRadians(60.0)
                val dotPos = Offset(
                    (center.x + baseRadius * 1.54f * cos(dotAngle)).toFloat(),
                    (center.y + baseRadius * 1.54f * sin(dotAngle)).toFloat()
                )
                drawCircle(color = primaryColor, radius = 4.dp.toPx(), center = dotPos)
                drawCircle(color = Color.White, radius = 2.dp.toPx(), center = dotPos)
            }

            // E. Diagonal Elliptical Orbit Ring 3
            rotate(ringRotation3, pivot = center) {
                drawOval(
                    brush = Brush.sweepGradient(
                        listOf(
                            primaryColor.copy(alpha = 0.9f),
                            secondaryColor.copy(alpha = 0.5f),
                            Color.Transparent
                        ),
                        center = center
                    ),
                    topLeft = Offset(center.x - baseRadius * 1.62f, center.y - baseRadius * 0.95f),
                    size = androidx.compose.ui.geometry.Size(baseRadius * 3.24f, baseRadius * 1.9f),
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // F. Audio Frequency Visualizer Waves (when listening / speaking)
            if ((isListening || isSpeaking) && settings.voiceVisualizer) {
                drawAudioSpectrum(center, baseRadius * 1.15f, wavePhase, isListening, primaryColor, secondaryColor)
            }

            // G. Center Core Glowing Lens Flare
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.85f),
                        primaryColor.copy(alpha = 0.7f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * 0.42f
                ),
                radius = baseRadius * 0.42f,
                center = center
            )
        }

        // 3. Center Glowing Microphone Icon
        Box(
            modifier = Modifier
                .size((52 * effectiveScale).dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        listOf(
                            primaryColor.copy(alpha = 0.95f),
                            secondaryColor.copy(alpha = 0.75f),
                            Color(0x330A0A14)
                        )
                    )
                )
                .border(1.5.dp, secondaryColor.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isListening || isSpeaking) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = if (isListening) "Stop listening" else "Speak to MJ",
                tint = Color.White,
                modifier = Modifier
                    .size((28 * effectiveScale).dp)
                    .testTag("orb_mic_icon")
            )
        }
    }
}

private fun DrawScope.drawAudioSpectrum(
    center: Offset,
    baseRadius: Float,
    phase: Float,
    isListening: Boolean,
    primaryColor: Color = MyraRedGlow,
    secondaryColor: Color = Color(0xFFFF7597)
) {
    val barCount = 36
    val angleStep = (2 * Math.PI / barCount).toFloat()

    for (i in 0 until barCount) {
        val angle = i * angleStep
        val wave = sin(angle * 3 + phase) * cos(angle * 2 - phase)
        val height = (abs(wave) * if (isListening) 32f else 20f) + 8f

        val startX = center.x + (baseRadius) * cos(angle)
        val startY = center.y + (baseRadius) * sin(angle)
        val endX = center.x + (baseRadius + height) * cos(angle)
        val endY = center.y + (baseRadius + height) * sin(angle)

        drawLine(
            brush = Brush.linearGradient(
                listOf(
                    primaryColor,
                    secondaryColor
                )
            ),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
    }
}

