# cistercian-clock

Android home-screen widget that displays the current time as a [Cistercian numeral](https://en.wikipedia.org/wiki/Cistercian_numerals) glyph. Updates every 15 minutes via WorkManager.

Built with Jetpack Glance (Compose-based widget API), targeting Pixel 10 / Android 16 (API 36).

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
- Android SDK 36 (install via SDK Manager)
- A Pixel 10 device or emulator (API 36)

## Build & install

1. Open the `cistercian-clock/` folder in Android Studio.
2. Let Gradle sync.
3. Run on a device/emulator: **Run ▶ app**.
4. Long-press the home screen → **Widgets** → **Cistercian Clock**.

## Project layout

```
app/src/main/java/com/cistercian/clock/
  Cistercian.kt            — stroke table, quadrant transforms, time decomposition
  CistercianClockWidget.kt — Glance composable + Bitmap renderer
  CistercianClockReceiver.kt — GlanceAppWidgetReceiver
  ClockUpdateWorker.kt     — WorkManager periodic job (every 15 min)
app/src/main/res/
  xml/cistercian_clock_widget_info.xml — widget metadata (2×2 cells)
  values/strings.xml
```
