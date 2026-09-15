package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrbSettings
import com.example.model.OrbStyle
import com.example.viewmodel.MyraViewModel
import kotlin.math.*

private val OrbDarkCanvasBg = Color(0xFF07070B)
private val OrbDarkCardBg = Color(0xFF10101A)
private val OrbDarkPillBg = Color(0xFF161622)
private val OrbVibrantRed = Color(0xFFFF002E)
private val OrbInactiveTrackColor = Color(0xFFECE6F0)
private val OrbMutedTextColor = Color(0xFF9E9EA8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrbCustomizeScreen(
    viewModel: MyraViewModel,
    orbSettings: OrbSettings,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStyle by remember(orbSettings) { mutableStateOf(orbSettings.style) }
    var orbSize by remember(orbSettings) { mutableFloatStateOf(orbSettings.sizeScale) }
    var colorHue by remember(orbSettings) { mutableFloatStateOf(orbSettings.colorHue) }
    var auraBorderMode by remember(orbSettings) { mutableStateOf(orbSettings.auraBorderMode) }
    var voiceVisualizer by remember(orbSettings) { mutableStateOf(orbSettings.voiceVisualizer) }
    val isFloating3DOrbActive by viewModel.isFloating3DOrbActive.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OrbDarkCanvasBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("orb_custom_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Orb Customization",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 22.sp
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .testTag("orb_customize_screen"),
            contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // 1. 3D Particle Cloud Orb Preview Canvas
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .testTag("orb_preview_card"),
                    shape = RoundedCornerShape(20.dp),
                    color = OrbDarkCardBg,
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1C1C2A))
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // 3D Texture Core Base
                        val previewPrimaryColor = Color(selectedStyle.primaryColorHex)
                        Box(
                            modifier = Modifier
                                .size((150 * orbSize).dp)
                                .clip(CircleShape)
                                .border(
                                    2.dp,
                                    Brush.radialGradient(
                                        listOf(previewPrimaryColor, previewPrimaryColor.copy(alpha = 0.4f), Color.Transparent)
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = selectedStyle.drawableResId),
                                contentDescription = selectedStyle.displayName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // 3D Rotating Particle Cloud Overlay
                        ParticleSphere3DPreview(
                            style = selectedStyle,
                            sizeScale = orbSize,
                            colorHue = colorHue,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // 2. 3D Hologram Cores Selection (All 11 Models)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "3D Hologram Cores",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x33FF2A55),
                            border = BorderStroke(1.dp, Color(0x66FF2A55))
                        ) {
                            Text(
                                text = "${OrbStyle.entries.size} CORES",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF4D77),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // 2-Column Responsive Grid for all 11 3D Styles
                    OrbStyle.entries.chunked(2).forEach { rowStyles ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowStyles.forEach { style ->
                                val isSelected = selectedStyle == style
                                val stylePrimary = Color(style.primaryColorHex)

                                Surface(
                                    onClick = {
                                        selectedStyle = style
                                        viewModel.performHaptic()
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) Color(0x33261224) else OrbDarkPillBg,
                                    border = BorderStroke(
                                        width = if (isSelected) 1.8.dp else 1.dp,
                                        brush = if (isSelected) {
                                            Brush.horizontalGradient(listOf(stylePrimary, stylePrimary.copy(alpha = 0.6f)))
                                        } else {
                                            Brush.horizontalGradient(listOf(Color(0xFF222232), Color(0xFF181824)))
                                        }
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("orb_type_${style.name.lowercase()}")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Miniature 3D Core Thumbnail
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color.Black)
                                                .border(
                                                    width = if (isSelected) 2.dp else 1.dp,
                                                    color = if (isSelected) stylePrimary else Color(0x44555577),
                                                    shape = CircleShape
                                                )
                                        ) {
                                            Image(
                                                painter = painterResource(id = style.drawableResId),
                                                contentDescription = style.displayName,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = style.displayName,
                                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.5.sp),
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color.White else Color(0xFFD4D4E0),
                                                maxLines = 1
                                            )
                                            Text(
                                                text = style.subtitle,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = if (isSelected) stylePrimary else OrbMutedTextColor,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                            if (rowStyles.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // 2.5 Phone Home Screen 3D Integration (Widget & Floating Orb)
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0x441E1138),
                    border = BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(Color(selectedStyle.primaryColorHex), Color(0xFF38BDF8)))),
                    modifier = Modifier.fillMaxWidth().testTag("customize_phone_3d_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Phone Screen 3D Setup",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0x3338BDF8)
                            ) {
                                Text(
                                    text = "3D LIVE",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Pin 3D Widget Button
                        Surface(
                            onClick = { viewModel.pin3DWidgetToPhoneHomeScreen() },
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x33000000),
                            border = BorderStroke(1.dp, Color(0x4438BDF8)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Add ${selectedStyle.displayName} Widget to Phone Screen",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Phone ke home screen par 3D Hologram core",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = OrbMutedTextColor
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.AddCircleOutline,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Floating 3D Orb Toggle
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x33000000),
                            border = BorderStroke(1.dp, Color(0x44FF2A55)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Floating 3D Hologram on Phone Screen",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Screen par draggable 3D Orb hamesha dikhega",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = OrbMutedTextColor
                                    )
                                }
                                Switch(
                                    checked = isFloating3DOrbActive,
                                    onCheckedChange = { viewModel.toggleFloating3DOrb(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(selectedStyle.primaryColorHex)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. Adjust Orb Size
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Adjust Orb Size",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    CustomStyledSlider(
                        value = ((orbSize - 0.5f) / 0.9f).coerceIn(0f, 1f),
                        onValueChange = { fraction ->
                            orbSize = (0.5f + fraction * 0.9f)
                        },
                        thumbColor = OrbVibrantRed,
                        activeColor = OrbVibrantRed,
                        testTag = "slider_orb_size"
                    )
                }
            }

            // 4. Adjust Color Hue & Color Bar
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Adjust Color Hue",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Slider for Hue
                    val hueFraction = (colorHue / 360f).coerceIn(0f, 1f)
                    val currentHueColor = remember(colorHue) {
                        val hsv = floatArrayOf(colorHue, 0.9f, 1.0f)
                        val colorInt = android.graphics.Color.HSVToColor(hsv)
                        Color(colorInt)
                    }

                    CustomStyledSlider(
                        value = hueFraction,
                        onValueChange = { fraction ->
                            colorHue = fraction * 360f
                        },
                        thumbColor = currentHueColor,
                        activeColor = OrbVibrantRed,
                        testTag = "slider_color_hue"
                    )

                    // Rainbow Hue Gradient Bar
                    RainbowGradientBar(
                        selectedHue = colorHue,
                        onHueSelected = { hue ->
                            colorHue = hue
                            viewModel.performHaptic()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .testTag("rainbow_gradient_bar")
                    )
                }
            }

            // 5. System Presence
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "System Presence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Aura Border Mode",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Shows a rotating border instead of the central orb",
                                style = MaterialTheme.typography.bodySmall,
                                color = OrbMutedTextColor
                            )
                        }

                        Switch(
                            checked = auraBorderMode,
                            onCheckedChange = {
                                auraBorderMode = it
                                viewModel.performHaptic()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = OrbVibrantRed,
                                uncheckedThumbColor = Color(0xFFA1A1AA),
                                uncheckedTrackColor = Color(0xFF2B2B3D)
                            ),
                            modifier = Modifier.testTag("switch_aura_border_mode")
                        )
                    }
                }
            }

            // 6. Voice Visualizer
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Voice Visualizer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Show Spectrum on Home",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = voiceVisualizer,
                            onCheckedChange = {
                                voiceVisualizer = it
                                viewModel.performHaptic()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = OrbVibrantRed,
                                uncheckedThumbColor = Color(0xFFA1A1AA),
                                uncheckedTrackColor = Color(0xFF2B2B3D)
                            ),
                            modifier = Modifier.testTag("switch_voice_visualizer")
                        )
                    }
                }
            }

            // 7. Save Changes Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        val newSettings = orbSettings.copy(
                            style = selectedStyle,
                            sizeScale = orbSize,
                            colorHue = colorHue,
                            auraBorderMode = auraBorderMode,
                            voiceVisualizer = voiceVisualizer
                        )
                        viewModel.updateOrbSettings(newSettings)
                        viewModel.performHaptic()
                        viewModel.showToast("Orb Customization Saved!")
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("btn_save_orb_changes"),
                    colors = ButtonDefaults.buttonColors(containerColor = OrbVibrantRed),
                    shape = RoundedCornerShape(27.dp)
                ) {
                    Text(
                        text = "Save Changes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

/**
 * Custom stylized slider track matching screenshot:
 * - Red bold active track
 * - Vertical bar thumb / indicator
 * - Soft light lilac inactive track with end dot
 */
@Composable
private fun CustomStyledSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    thumbColor: Color,
    activeColor: Color,
    modifier: Modifier = Modifier,
    testTag: String = "custom_styled_slider"
) {
    var widthPx by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .testTag(testTag)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (widthPx > 0) {
                        val fraction = (offset.x / widthPx).coerceIn(0f, 1f)
                        onValueChange(fraction)
                    }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    if (widthPx > 0) {
                        val fraction = (change.position.x / widthPx).coerceIn(0f, 1f)
                        onValueChange(fraction)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
        ) {
            widthPx = size.width
            val trackHeight = 12.dp.toPx()
            val cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f)
            val thumbX = (value * size.width).coerceIn(0f, size.width)

            // Draw full background / Inactive Track (light lilac)
            drawRoundRect(
                color = OrbInactiveTrackColor,
                topLeft = Offset(0f, (size.height - trackHeight) / 2f),
                size = Size(size.width, trackHeight),
                cornerRadius = cornerRadius
            )

            // Draw Active Track (Solid Red) up to thumb position
            if (thumbX > 0) {
                drawRoundRect(
                    color = activeColor,
                    topLeft = Offset(0f, (size.height - trackHeight) / 2f),
                    size = Size(thumbX, trackHeight),
                    cornerRadius = CornerRadius(trackHeight / 2f, trackHeight / 2f)
                )
            }

            // Draw vertical indicator bar (Thumb)
            val thumbBarWidth = 4.dp.toPx()
            val thumbBarHeight = 22.dp.toPx()
            drawRoundRect(
                color = thumbColor,
                topLeft = Offset(thumbX - thumbBarWidth / 2f, (size.height - thumbBarHeight) / 2f),
                size = Size(thumbBarWidth, thumbBarHeight),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            // Draw small red dot at the end of the inactive track
            val dotRadius = 3.dp.toPx()
            drawCircle(
                color = OrbVibrantRed,
                radius = dotRadius,
                center = Offset(size.width - trackHeight / 2f, size.height / 2f)
            )
        }
    }
}

/**
 * Continuous Rainbow Gradient bar
 */
@Composable
private fun RainbowGradientBar(
    selectedHue: Float,
    onHueSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val rainbowColors = listOf(
        Color(0xFFFF0000), // Red
        Color(0xFFFFA500), // Orange
        Color(0xFFFFFF00), // Yellow
        Color(0xFF00FF00), // Green
        Color(0xFF00FFFF), // Cyan
        Color(0xFF0066FF), // Blue
        Color(0xFF8800FF), // Indigo
        Color(0xFFFF00FF), // Magenta
        Color(0xFFFF0000)  // Red
    )

    var barWidth by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (barWidth > 0) {
                        val hue = (offset.x / barWidth).coerceIn(0f, 1f) * 360f
                        onHueSelected(hue)
                    }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    if (barWidth > 0) {
                        val hue = (change.position.x / barWidth).coerceIn(0f, 1f) * 360f
                        onHueSelected(hue)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            barWidth = size.width
            drawRoundRect(
                brush = Brush.horizontalGradient(rainbowColors),
                size = size,
                cornerRadius = CornerRadius(size.height / 2f, size.height / 2f)
            )
        }
    }
}

/**
 * 3D Spherical Particle Cloud Preview
 * Implements a Fibonacci point cloud distributed on a sphere with 3D rotation,
 * perspective projection, and responsive particle styles.
 */
@Composable
private fun ParticleSphere3DPreview(
    style: OrbStyle,
    sizeScale: Float,
    colorHue: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "3DParticleSphere")

    val rotY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing)
        ),
        label = "rotY"
    )

    val rotX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing)
        ),
        label = "rotX"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Generate 600 points on sphere using Fibonacci spiral algorithm
    val pointCount = 650
    val points = remember {
        val pts = ArrayList<Triple<Float, Float, Float>>(pointCount)
        val goldenRatio = (1.0 + sqrt(5.0)) / 2.0
        val phi = 2.0 * Math.PI * (1.0 - 1.0 / goldenRatio)
        for (i in 0 until pointCount) {
            val y = 1.0f - (i.toFloat() / (pointCount - 1).toFloat()) * 2.0f
            val radiusAtY = sqrt(max(0.0f, 1.0f - y * y))
            val theta = (phi * i).toFloat()
            val x = cos(theta) * radiusAtY
            val z = sin(theta) * radiusAtY
            pts.add(Triple(x, y, z))
        }
        pts
    }

    // Base color from Hue
    val baseColor = remember(colorHue) {
        val hsv = floatArrayOf(colorHue, 0.85f, 1.0f)
        val colorInt = android.graphics.Color.HSVToColor(hsv)
        Color(colorInt)
    }

    val lightColor = remember(colorHue) {
        val hsv = floatArrayOf((colorHue + 15f) % 360f, 0.5f, 1.0f)
        val colorInt = android.graphics.Color.HSVToColor(hsv)
        Color(colorInt)
    }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val sphereRadius = (min(size.width, size.height) / 2f) * 0.72f * sizeScale * pulse
        val cameraDistance = 2.5f

        // 1. Subtle Outer Glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    baseColor.copy(alpha = 0.22f),
                    baseColor.copy(alpha = 0.06f),
                    Color.Transparent
                ),
                center = center,
                radius = sphereRadius * 1.5f
            ),
            radius = sphereRadius * 1.5f,
            center = center
        )

        // Rotation angles
        val cosY = cos(rotY)
        val sinY = sin(rotY)
        val cosX = cos(rotX)
        val sinX = sin(rotX)

        // Project and sort points for depth rendering
        for (pt in points) {
            // Rotate around Y
            val x1 = pt.first * cosY + pt.third * sinY
            val z1 = -pt.first * sinY + pt.third * cosY
            val y1 = pt.second

            // Rotate around X
            val y2 = y1 * cosX - z1 * sinX
            val z2 = y1 * sinX + z1 * cosX
            val x2 = x1

            // Perspective scale
            val perspective = cameraDistance / (cameraDistance + z2)
            val projX = center.x + x2 * sphereRadius * perspective
            val projY = center.y + y2 * sphereRadius * perspective

            // Depth opacity and size
            val depthAlpha = ((z2 + 1f) / 2f).coerceIn(0.15f, 1.0f)
            val pointSize = when (style) {
                OrbStyle.ENERGY -> 2.2.dp.toPx() * perspective * 1.3f
                OrbStyle.NEON -> 2.5.dp.toPx() * perspective * 1.2f
                else -> 1.8.dp.toPx() * perspective // Classic
            }

            val particleColor = if (z2 > 0.3f) lightColor.copy(alpha = depthAlpha) else baseColor.copy(alpha = depthAlpha * 0.9f)

            drawCircle(
                color = particleColor,
                radius = pointSize,
                center = Offset(projX, projY)
            )
        }

        // Additional stylistic effects
        if (style == OrbStyle.ENERGY) {
            // Rotating energy arc
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        baseColor.copy(alpha = 0.8f),
                        Color.Transparent,
                        lightColor.copy(alpha = 0.9f),
                        Color.Transparent,
                        baseColor.copy(alpha = 0.8f)
                    ),
                    center = center
                ),
                radius = sphereRadius * 1.15f,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
            )
        } else if (style == OrbStyle.NEON) {
            // Dual Neon rings
            drawOval(
                brush = Brush.horizontalGradient(
                    listOf(
                        baseColor.copy(alpha = 0.85f),
                        lightColor.copy(alpha = 0.95f),
                        baseColor.copy(alpha = 0.3f)
                    )
                ),
                topLeft = Offset(center.x - sphereRadius * 1.35f, center.y - sphereRadius * 0.55f),
                size = Size(sphereRadius * 2.7f, sphereRadius * 1.1f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx())
            )
        }
    }
}
