# cistercian-clock

Android home-screen widget that displays the current time as a [Cistercian numeral](https://en.wikipedia.org/wiki/Cistercian_numerals) glyph. Updates every minute via `AlarmManager`.

Built with Jetpack Glance (Compose-based widget API), targeting Android 8+ (API 26), optimised for Pixel / Android 16 (API 36).

## How it works

The current time is formatted as HHMM (0–2359) and decomposed into four digits — units, tens, hundreds, thousands — each rendered in its canonical quadrant of the stave:

| Quadrant    | Position     | Digit |
|-------------|--------------|-------|
| Upper-right | Units        | HHMM % 10 |
| Upper-left  | Tens         | (HHMM/10) % 10 |
| Lower-right | Hundreds     | (HHMM/100) % 10 |
| Lower-left  | Thousands    | HHMM/1000 |

e.g. 14:37 → 1437 → units=7, tens=3, hundreds=4, thousands=1.

## Prerequisites

- Android Studio Ladybug (2024.2) or newer
- Android SDK 26+ (API 36 recommended; install via SDK Manager)
- A physical device or emulator running Android 8.0+

## Build & install

1. Open the `cistercian-clock/` folder in Android Studio.
2. Let Gradle sync.
3. Run on a device/emulator: **Run ▶ app**.
4. Long-press the home screen → **Widgets** → **Cistercian Clock**.

## Project layout

```
app/src/main/java/com/cistercian/clock/
  Cistercian.kt              — stroke table, quadrant transforms, time decomposition
  CistercianClockWidget.kt   — Glance composable + Bitmap renderer
  CistercianClockReceiver.kt — GlanceAppWidgetReceiver + AlarmManager scheduling
app/src/main/res/
  xml/cistercian_clock_widget_info.xml — widget metadata (2×2 cells)
  values/strings.xml
```

## Attributions

**Cistercian numeral system**
The medieval Cistercian numeral system was devised by Cistercian monks in the early 13th century. For a thorough history and glyph survey see:
- David A. King, *The Ciphers of the Monks* (Franz Steiner Verlag, 2001)
- Wikipedia: [Cistercian numerals](https://en.wikipedia.org/wiki/Cistercian_numerals)

**Glyph geometry reference**
Stroke proportions and quadrant layout are derived from the **FRBCistercian** open-source font project by Fredrick R. Brennan:
- Repository: <https://github.com/ctrlcctrlv/FRBCistercian>
- License: The FRBCistercian font is released under the [SIL Open Font License 1.1](https://scripts.sil.org/OFL).

The glyph-drawing code in `Cistercian.kt` and `CistercianClockWidget.kt` is an independent implementation in Kotlin/Android Canvas; no font files or compiled assets from FRBCistercian are bundled in this app.

**Unicode proposal**
The Unicode encoding proposal for Cistercian numerals (L2/20-290) by Kirk Miller provided additional reference material for the stroke table.

**Libraries**
- [Jetpack Glance](https://developer.android.com/jetpack/androidx/releases/glance) — Apache 2.0
- [AndroidX Core KTX](https://developer.android.com/kotlin/ktx) — Apache 2.0
- [Jetpack Compose](https://developer.android.com/jetpack/compose) — Apache 2.0
