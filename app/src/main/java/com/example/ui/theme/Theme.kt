package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MyraDarkColorScheme = darkColorScheme(
    primary = MyraRed,
    onPrimary = MyraTextPrimary,
    primaryContainer = MyraRedDark,
    onPrimaryContainer = MyraTextPrimary,
    secondary = MyraRedGlow,
    onSecondary = MyraTextPrimary,
    tertiary = MyraNeonCyan,
    background = MyraBlack,
    onBackground = MyraTextPrimary,
    surface = MyraDarkBg,
    onSurface = MyraTextPrimary,
    surfaceVariant = MyraCardDark,
    onSurfaceVariant = MyraTextSecondary,
    outline = MyraCardBorder
)

@Composable
fun MYRATheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = MyraBlack.toArgb()
                window.navigationBarColor = MyraBlack.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = MyraDarkColorScheme,
        typography = Typography,
        content = content
    )
}
