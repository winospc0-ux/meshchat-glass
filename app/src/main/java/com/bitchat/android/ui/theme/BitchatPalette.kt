package com.bitchat.android.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Bitchat-specific color tokens that do not have a faithful Material 3 semantic role.
 *
 * Standard backgrounds, surfaces, text, outlines, primary/secondary accents, and errors belong
 * to [androidx.compose.material3.MaterialTheme.colorScheme]. Keeping only the extra app semantics
 * here lets Material components inherit correct defaults without losing Bitchat's identity.
 */
@Immutable
data class BitchatPalette(
    // MARK: - Form controls
    /**
     * Resting border for text inputs. Deliberately a neutral grey rather than the green-tinted
     * Material outline: the composer is the one surface the user stares at while typing.
     */
    val inputOutline: Color,
    /** Border for a focused text input. A step brighter, still neutral. */
    val inputOutlineFocused: Color,
    /**
     * Fill for text inputs. Near-black / near-white and completely untinted, for the same reason
     * as [inputOutline] — and because the composer sits on top of a green-tinted scrim, so any
     * tint of its own compounds into something muddy.
     */
    val inputSurface: Color,
    /** Fill for a focused text input. A barely perceptible lift. */
    val inputSurfaceFocused: Color,
    /** Resting disc behind the composer's action glyphs. Neutral grey. */
    val inputButton: Color,

    // MARK: - Extra semantics
    /** Timestamps, placeholders, section labels, disabled states. */
    val textTertiary: Color,
    /** Self, mentions targeting you, unread DMs. */
    val accentOrange: Color,
    /** Nostr reachability. */
    val accentPurple: Color,

    // MARK: - Glass surfaces
    /**
     * Opaque base for glass panes, used at [GlassAlpha] so the mesh backdrop shows
     * through. Light: plain white; dark: a lifted green-tinted surface so panes
     * separate from the near-black backdrop.
     */
    val glassSurface: Color,
    /** Bright top edge of a glass pane — the light catching the rim. */
    val glassHairlineTop: Color,
    /** Faint bottom edge of a glass pane. */
    val glassHairlineBottom: Color,
    /** Mesh motif dots on the backdrop (opaque base; alpha applied at draw time). */
    val backdropNode: Color,
    /** Mesh motif links on the backdrop (opaque base; alpha applied at draw time). */
    val backdropLink: Color,

    // MARK: - Deterministic peer colors
    /**
     * Saturation/value applied after deriving a peer's stable hue. Swap this when adding a
     * new theme — see [PeerColorStyle] for contrast guidelines.
     */
    val peerColors: PeerColorStyle,
)

val DarkBitchatPalette = BitchatPalette(
    inputOutline = Color(0xFF333635),
    inputOutlineFocused = Color(0xFF5A605D),
    inputSurface = Color(0xFF0B0B0B),
    inputSurfaceFocused = Color(0xFF151515),
    inputButton = Color(0xFF1E1E1E),
    textTertiary = Color(0xFF6B776B),
    accentOrange = Color(0xFFFF9F0A),
    accentPurple = Color(0xFFBF5AF2),
    glassSurface = Color(0xFF1A241A),
    glassHairlineTop = Color.White.copy(alpha = 0.22f),
    glassHairlineBottom = Color.Black.copy(alpha = 0.45f),
    backdropNode = Color(0xFF32D74B),
    backdropLink = Color(0xFF32D74B),
    peerColors = PeerColorStyle.Dark,
)

val LightBitchatPalette = BitchatPalette(
    inputOutline = Color(0xFFCFD3D1),
    inputOutlineFocused = Color(0xFF8E9490),
    inputSurface = Color(0xFFFAFAFA),
    inputSurfaceFocused = Color(0xFFF2F2F2),
    inputButton = Color(0xFFE8E8E8),
    textTertiary = Color(0xFF757F75),
    accentOrange = Color(0xFFFF9500),
    accentPurple = Color(0xFFAF52DE),
    glassSurface = Color(0xFFFFFFFF),
    glassHairlineTop = Color.White.copy(alpha = 0.90f),
    glassHairlineBottom = Color.Black.copy(alpha = 0.10f),
    backdropNode = Color(0xFF248A3D),
    backdropLink = Color(0xFF248A3D),
    peerColors = PeerColorStyle.Light,
)

val LocalBitchatPalette = staticCompositionLocalOf { DarkBitchatPalette }

/**
 * Motion tokens. The redesign leans on short, snappy transitions: long durations read as
 * sluggish on a chat surface where the user is scanning quickly.
 */
object BitchatMotion {
    /** Icon tints, text colors, small fills. */
    const val QUICK_MS = 120

    /** Tab indicators, pill growth, chip reveals. */
    const val STANDARD_MS = 180

    /** Sheet-level fades and scroll-driven top bars. */
    const val EMPHASIZED_MS = 240
}
