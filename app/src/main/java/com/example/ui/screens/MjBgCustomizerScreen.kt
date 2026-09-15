package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MjBackgroundTheme
import com.example.viewmodel.MyraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MjBgCustomizerScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTheme by viewModel.mjBackgroundTheme.collectAsState()
    val playIntroOnStartup by viewModel.playIntroOnStartup.collectAsState()

    var showCustomHexDialog by remember { mutableStateOf(false) }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(currentTheme.bgPrimaryHex),
            Color(currentTheme.bgSecondaryHex)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "MJ Background & Color",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "बैकग्राउंड रंग और थीम कस्टमाइज़ करें",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(currentTheme.bgPrimaryHex).copy(alpha = 0.95f)
                )
            )
        },
        containerColor = Color.Transparent,
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
            .testTag("mj_bg_customizer_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // 1. Current Active Background Live Preview Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(currentTheme.surfaceHex)),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(currentTheme.accentGlowHex))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(currentTheme.accentGlowHex))
                            )
                            Text(
                                text = "CURRENT ACTIVE THEME",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(currentTheme.accentGlowHex),
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = currentTheme.displayName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Hindi: ${currentTheme.hindiName}",
                            fontSize = 14.sp,
                            color = Color(0xFFD1D5DB)
                        )

                        // Color Palette Swatches inside
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ColorSwatchPill("Primary", Color(currentTheme.bgPrimaryHex))
                            ColorSwatchPill("Secondary", Color(currentTheme.bgSecondaryHex))
                            ColorSwatchPill("Accent", Color(currentTheme.accentGlowHex))
                            ColorSwatchPill("Surface", Color(currentTheme.surfaceHex))
                        }
                    }
                }
            }

            // 2. Select Background Themes Header
            item {
                Text(
                    text = "Select Background Color / Theme (थीम चुनें)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 3. Grid / List of All Background Themes
            items(MjBackgroundTheme.entries) { theme ->
                val isSelected = theme == currentTheme
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setMjBackgroundTheme(theme)
                        }
                        .testTag("theme_card_${theme.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(theme.surfaceHex)),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Color(theme.accentGlowHex) else Color(theme.borderHex)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Theme Color Indicator Circle with Glow
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                Color(theme.accentGlowHex),
                                                Color(theme.bgPrimaryHex)
                                            )
                                        )
                                    )
                                    .border(1.5.dp, Color(theme.accentGlowHex), CircleShape)
                            )

                            Column {
                                Text(
                                    text = theme.displayName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = theme.hindiName,
                                    fontSize = 13.sp,
                                    color = Color(0xFFA0A0B0)
                                )
                            }
                        }

                        // Selection Checkmark
                        if (isSelected) {
                            Surface(
                                shape = CircleShape,
                                color = Color(theme.accentGlowHex),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.Black,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = { viewModel.setMjBackgroundTheme(theme) },
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                            ) {
                                Text("Apply", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // 4. Intro Video Controls Section
            item {
                Text(
                    text = "Intro Video Settings (शुरुआती वीडियो सेटिंग्स)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(currentTheme.surfaceHex)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(currentTheme.borderHex))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Toggle Intro on App Open
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Play Video on App Open",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "ऐप खुलते ही MJ का इंट्रो वीडियो चलाएं",
                                    fontSize = 12.sp,
                                    color = Color(0xFFA0A0B0)
                                )
                            }
                            Switch(
                                checked = playIntroOnStartup,
                                onCheckedChange = { viewModel.setPlayIntroOnStartup(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(currentTheme.accentGlowHex)
                                )
                            )
                        }

                        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                        // Replay Intro Video Button
                        Button(
                            onClick = {
                                viewModel.replayIntroVideo()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(currentTheme.accentGlowHex),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Video"
                                )
                                Text(
                                    text = "Replay Intro Video Now (अभी वीडियो चलाएं)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.ColorSwatchPill(label: String, color: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        modifier = Modifier.weight(1f).height(32.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (color == Color.White || color.red + color.green + color.blue > 2.0f) Color.Black else Color.White
            )
        }
    }
}
