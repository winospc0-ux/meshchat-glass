package com.bitchat.android.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

// Standard UI semantics live in Material so stock components and custom Bitchat composables
// share one source of truth. LocalBitchatPalette below only supplies app-specific extra colors.
internal val DarkBitchatColorScheme = darkColorScheme(
    primary = Color(0xFF32D74B),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF163D1D),
    onPrimaryContainer = Color(0xFFB8F5C1),
    secondary = Color(0xFF0A84FF),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF082E54),
    onSecondaryContainer = Color(0xFFC2E0FF),
    tertiary = DarkBitchatPalette.accentOrange,
    onTertiary = Color.Black,
    // Near-black with a green cast: the backdrop the mesh motif and dark glass sit on.
    background = Color(0xFF0A0F0A),
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF0E150E),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF182118),
    onSurfaceVariant = Color(0xFF9AA69A),
    outline = Color(0xFF2A3A2A),
    outlineVariant = Color(0xFF1C271C),
    error = Color(0xFFFF453A),
    onError = Color.Black
)

internal val LightBitchatColorScheme = lightColorScheme(
    primary = Color(0xFF3390EC),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCEBFB),
    onPrimaryContainer = Color(0xFF0A3D66),
    secondary = Color(0xFF3390EC),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCEBFB),
    onSecondaryContainer = Color(0xFF0A3D66),
    tertiary = Color(0xFF4FAE4E),
    onTertiary = Color.White,
    // Telegram-style clean white base.
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF0F0F0F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F0F0F),
    surfaceVariant = Color(0xFFF4F4F4),
    onSurfaceVariant = Color(0xFF707579),
    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFEEEEEE),
    error = Color(0xFFE53935),
    onError = Color.White
)

@Composable
fun BitchatTheme(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    // App-level override from ThemePreferenceManager
    val themePref by ThemePreferenceManager.themeFlow.collectAsState(initial = ThemePreference.System)
    val shouldUseDark = when (darkTheme) {
        true -> true
        false -> false
        null -> when (themePref) {
            ThemePreference.Dark -> true
            ThemePreference.Light -> false
            ThemePreference.System -> isSystemInDarkTheme()
        }
    }

    val colorScheme = if (shouldUseDark) DarkBitchatColorScheme else LightBitchatColorScheme
    val palette = if (shouldUseDark) DarkBitchatPalette else LightBitchatPalette

    val view = LocalView.current
    SideEffect {
        (view.context as? Activity)?.window?.let { window ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    if (!shouldUseDark) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = if (!shouldUseDark) {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else 0
            }
            window.navigationBarColor = colorScheme.background.toArgb()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
    }

    CompositionLocalProvider(LocalBitchatPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
