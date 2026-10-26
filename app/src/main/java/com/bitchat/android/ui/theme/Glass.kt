package com.bitchat.android.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.hypot
import kotlin.random.Random

/**
 * Glass system for the mesh chat UI.
 *
 * Glass here is translucency + a hairline rim + a soft shadow, never a blur effect:
 * the app's min SDK is 26 and RenderEffect needs 31, so a blur dependency would either
 * crash or silently vanish on older devices. Translucent panes over the mesh backdrop
 * read as glass on every API level with no fallback branch to maintain.
 */

/** Translucency of every glass pane, light and dark alike (Solid Glass spirit: ~0.88). */
const val GlassAlpha: Float = 0.88f

/** Shadow under floating glass panes. Bars and rows sit flat so they never smear. */
val GlassBubbleShadow: Dp = 2.dp
val GlassCardShadow: Dp = 4.dp

/** Composer field radius: rounded, deliberately not a full pill (see DESIGN.md). */
val GlassComposerRadius: Dp = 20.dp

/** Top-highlight-to-bottom-hairline rim for a contained glass pane. */
@Composable
fun glassBorderBrush(): Brush {
    val palette = LocalBitchatPalette.current
    return remember(palette) {
        Brush.verticalGradient(
            0f to palette.glassHairlineTop,
            1f to palette.glassHairlineBottom
        )
    }
}

/** A contained glass pane: shadow, translucent fill, gradient hairline rim. */
fun Modifier.glassPane(
    fill: Color,
    borderBrush: Brush,
    shape: Shape,
    elevation: Dp = 0.dp,
): Modifier = this
    .shadow(elevation, shape)
    .background(fill, shape)
    .border(BorderStroke(1.dp, borderBrush), shape)

/** Which edge of a full-width bar carries its hairline (bars never get a full border). */
enum class GlassBarEdge { Top, Bottom }

/** A full-width glass bar: translucent fill plus one edge hairline, no side lines. */
fun Modifier.glassBar(
    fill: Color,
    edgeColor: Color,
    edge: GlassBarEdge,
): Modifier = this
    .background(fill)
    .drawBehind {
        val y = if (edge == GlassBarEdge.Top) 0f else size.height - 1.dp.toPx()
        drawLine(edgeColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
    }

/**
 * Bubble fill: the opaque glass surface tinted a whisper toward the author's color,
 * then made translucent. The tint keeps author identity; the glass keeps the backdrop.
 */
fun glassBubbleFill(author: Color, surface: Color): Color =
    lerp(surface, author, 0.14f).copy(alpha = GlassAlpha)

/**
 * The mesh itself, drawn as a backdrop: a fixed-seed scatter of nodes joined by faint
 * links that fade with distance. Seeded so the motif never reshuffles on recomposition,
 * and low-contrast so text on glass above it stays readable. Draws nothing interactive
 * and never touches location, identity, or network state — pure decoration.
 */
@Composable
fun MeshBackdrop(modifier: Modifier = Modifier) {
    val palette = LocalBitchatPalette.current
    val colorScheme = MaterialTheme.colorScheme
    val nodes = remember {
        val rnd = Random(42)
        List(42) { Offset(rnd.nextFloat(), rnd.nextFloat()) }
    }
    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(colorScheme.background)
        val w = size.width
        val h = size.height
        val pts = nodes.map { Offset(it.x * w, it.y * h) }
        val linkRadius = minOf(w, h) * 0.28f
        for (i in pts.indices) {
            for (j in i + 1 until pts.size) {
                val d = hypot(pts[i].x - pts[j].x, pts[i].y - pts[j].y)
                if (d < linkRadius) {
                    val fade = 1f - d / linkRadius
                    drawLine(
                        color = palette.backdropLink.copy(alpha = 0.08f + 0.14f * fade),
                        start = pts[i],
                        end = pts[j],
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
        }
        for (p in pts) {
            drawCircle(
                color = palette.backdropNode.copy(alpha = 0.30f),
                radius = 2.5.dp.toPx(),
                center = p
            )
        }
    }
}
