# Cistercian Clock — Claude Context

Android home-screen widget displaying current date and time as paired Cistercian numeral glyphs.
Built with Jetpack Glance (Compose-based widget API), targeting Android 8+ (API 26).

## Key files

| File | Purpose |
|------|---------|
| `app/src/main/java/com/cistercian/clock/Cistercian.kt` | Stroke table, quadrant transforms, `decomposeTime`, `decomposeDate` |
| `app/src/main/java/com/cistercian/clock/CistercianClockWidget.kt` | Glance composable + `renderBothGlyphs` bitmap renderer |
| `app/src/main/java/com/cistercian/clock/CistercianClockReceiver.kt` | `GlanceAppWidgetReceiver` + `AlarmManager` for per-minute updates |
| `app/src/main/res/xml/cistercian_clock_widget_info.xml` | Widget metadata: 1×1 default, resizable, min 80dp |
| `docs/cistercian-reference.svg` | Digit and quadrant reference diagram |

## Widget layout

Two Cistercian glyphs are rendered into a single tight-fitting bitmap (2px boundary padding, 50px inter-glyph gap):

- **Left glyph (blue)**: date — thousands/hundreds = month, tens/units = day of month
- **Right glyph (red)**: time — thousands/hundreds = hour (24h), tens/units = minute

e.g. 14 June at 09:37 → blue glyph = 0614, red glyph = 0937

## Glyph geometry

All proportions are relative to `staveH` (the full height of the vertical stave):

```
quadW = halfH = staveH × 0.30
```

Quadrant mapping (from `Cistercian.kt`):

| Quadrant  | Position     | sx | y-origin | sy |
|-----------|--------------|----|----------|----|
| UNITS     | upper-right  | +1 | top      | +1 (downward) |
| TENS      | upper-left   | −1 | top      | +1 (downward) |
| HUNDREDS  | lower-right  | +1 | bottom   | −1 (upward)   |
| THOUSANDS | lower-left   | −1 | bottom   | −1 (upward)   |

## Build

System Java (25.0.2) breaks the Kotlin toolchain — use Android Studio's bundled JDK 21:

```bash
JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew assembleDebug
JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew assembleRelease
```

## Install to emulator

```bash
/Volumes/External/Users/jberman/Library/Android/sdk/platform-tools/adb install -r \
  app/build/outputs/apk/debug/app-debug.apk
```

The emulator typically shows up as `emulator-5554` in `adb devices`.
After reinstalling, remove and re-add the widget from the home screen widget picker.
