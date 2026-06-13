package com.cistercian.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.size
import java.util.Calendar

class CistercianClockWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { ClockContent() }
    }
}

@Composable
private fun ClockContent() {
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)
    val digits = decomposeTime(hour, minute)

    val bitmap = renderCistercian(digits, sizePx = 300)

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF111111)),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            provider = ImageProvider(bitmap),
            contentDescription = "%02d:%02d".format(hour, minute),
            modifier = GlanceModifier.size(120.dp),
        )
    }
}

private fun renderCistercian(digits: TimeDigits, sizePx: Int): Bitmap {
    val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    canvas.drawColor(android.graphics.Color.TRANSPARENT)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.parseColor("#E6E6E6")
        strokeWidth = sizePx * 0.025f
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }

    val cx = sizePx / 2f
    val cy = sizePx / 2f
    val staveH = sizePx * 0.8f
    val top = cy - staveH / 2f
    val bottom = cy + staveH / 2f
    val quadW = staveH * 0.36f
    val halfH = staveH * 0.40f

    // vertical stave
    canvas.drawLine(cx, top, cx, bottom, paint)

    for ((digit, quadrant) in listOf(
        digits.units     to Quadrant.UNITS,
        digits.tens      to Quadrant.TENS,
        digits.hundreds  to Quadrant.HUNDREDS,
        digits.thousands to Quadrant.THOUSANDS,
    )) {
        cistercianStrokes(digit, quadrant, cx, top, bottom, quadW, halfH).forEach { seg ->
            canvas.drawLine(seg.x1, seg.y1, seg.x2, seg.y2, paint)
        }
    }

    return bmp
}
