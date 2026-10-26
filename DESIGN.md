# DESIGN.md — Mesh Glass

Design direction for the bitchat-android chat UI. Written under the anti-slop
filter (miqdadbadjuber/anti-slop, mode `during`, session override): anti-slop
removes generic patterns, it does not invent direction — this file is the
direction. Every major decision below carries its one-line reason (R-31).

## R-10 override (recorded per R-37)

Owner kept full glass, R-10 dose cap overridden by owner choice.

Anti-slop R-10 caps glassmorphism as an accent on 1–2 elements. The owner
explicitly chose full glass across the whole UI (option 1) over accent-only
glass, so the cap is overridden by owner choice and glass is the character of
this UI, not an accent. Nothing else in this file overrides a rule silently.

## Direction in one line

Frosted glass panes floating over a visible mesh: you see the network beneath
the chat, because the network is the product.

## Palette (R-29: 2 core + 1 accent + neutrals)

| Token | Dark | Light | Reason |
|---|---|---|---|
| Primary signal green | `0xFF32D74B` | `0xFF248A3D` | The mesh-alive signal, and the app's existing brand — not a new pick. |
| Secondary internet blue | `0xFF0A84FF` | `0xFF007AFF` | Functional only: marks internet/Nostr transport, never decoration. |
| Accent amber | `0xFFFF9F0A` | `0xFFFF9500` | The single attention color: unread badges, mentions, send in private chats. |
| Backdrop base | `0xFF0A0F0A` | `0xFFF6F4EF` | Near-black with a green cast / warm paper — not stock black or white. |
| Glass surface | `0xFF1A241A` | `0xFFFFFFFF` | Lifted off the backdrop so panes separate at 0.88 opacity. |

Not Telegram blue (`0x0088CC`), not generic purple, and no blue-to-purple or
purple-to-pink gradient anywhere (R-01, R-30: this UI must not clone Telegram
or any popular product). The only gradient in the app is the glass rim itself
— bright top edge to faint bottom edge — because a rim that catches light on
one side is what makes a pane read as glass rather than a flat tint.

Peer colors stay deterministic per peer (existing system): a peer's color is
identity, not decoration.

## Typography (R-06)

Bundled Geist Mono stays the single app typeface. Reason: it is the product's
established cross-client character (matches iOS) and ships in the APK, so the
design never depends on a device's fonts or a network fetch. R-06 names mono
as an AI default; here it is kept for that stated brand reason, not by
default, and body text keeps generous leading so long messages stay readable.
No uppercase labels with extreme letter-spacing anywhere.

## Glass treatment

- Every pane is translucent at **0.88** (`GlassAlpha`), light and dark alike.
- Contained panes (bubbles, cards, dialogs, FAB) get a full hairline rim:
  bright top edge, faint bottom edge, plus a soft shadow (bubbles 2.dp, cards
  4.dp).
- Full-width bars and rows sit flat (no shadow) and carry a single edge
  hairline where they meet content — header bottom, composer top.
- No blur effect anywhere: the app's min SDK is 26 and RenderEffect needs 31,
  so blur would crash or silently vanish on older devices; translucency +
  rim + shadow read as glass on every API level with no fallback branch.
- Implementation: `ui/theme/Glass.kt` (`glassPane`, `glassBar`,
  `glassBorderBrush`, `glassBubbleFill`, `MeshBackdrop`) and the `glass*`
  fields on `BitchatPalette`, defined for both light and dark (R-21, R-34).

## Backdrop

`MeshBackdrop` draws the mesh as decoration: a fixed-seed scatter of 42 nodes
joined by faint links that fade with distance, in the primary green at low
alpha over the backdrop base. Reason: seeing the mesh beneath the glass is
the product idea — this is a mesh chat app, not a generic chat skin. It draws
no real data, no location, no identity: pure motif, seeded so it never
reshuffles on recomposition.

## Radius system (R-11)

Deliberate variation, never everything pill-shaped: bubbles 16.dp with a 4.dp
tail corner (existing speaker-side system, kept); cards and rows 16.dp;
composer field 20.dp (changed from a full pill — a field is not a chip);
small chips 8.dp; icon discs stay circular because they are buttons, not cards.

## States (R-25, R-26, R-27)

- Text on glass stays onSurface (near-black / near-white); header titles and
  the nickname moved off primary green for contrast over the backdrop.
- Delivery states are unchanged: single grey check sent, double grey
  delivered, double blue read, double red failed — color is the signal.
- Unread = amber badge (already the accent); selected = green dot; the two
  never share a color, so selection and unread are never confused.
- Composer focus still lifts (0.88 → 0.95) and brightens its border;
  recording still turns the field into a neutral error-tinted slab.
- Avatars changed from a 0.16 color wash with colored initial to a solid
  peer-color disc with a white initial and a hairline ring, because the wash
  failed contrast over the backdrop.
- Bubbles keep their author-colored hairline; the fill is now glass tinted
  14% toward the author color, so author identity survives without washing
  the text out.

## Deliberately untouched

Mesh, Noise, Nostr, models, ViewModels, dependencies, package name, app name,
all user-facing strings, the CI workflow (`.github/workflows/android.yml`),
the shared `BitchatBottomSheet` container (so sheets show glass cards over
the sheet's own surface rather than the backdrop — a known limit, see below),
swipe-action backgrounds, and send-button colors.

## Known limits

- No local Android SDK exists on the machine that made this change, so
  compilation is NOT verified here; the existing GitHub Actions workflow is
  the verification step and must pass before this ships.
- True backdrop blur is approximated by translucency (see above); on
  API 31+ a real blur pass could deepen the frost later without changing
  any token.
- Sheet glass is card-deep only: making the backdrop show through sheets
  needs the shared bottom-sheet container to go translucent, which was
  avoided here because that component is shared across screens.
