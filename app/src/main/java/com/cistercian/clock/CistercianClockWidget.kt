package com.cistercian.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
    val month = cal.get(Calendar.MONTH) + 1
    val day = cal.get(Calendar.DAY_OF_MONTH)

    val bitmap = renderBothGlyphs(
        dateDigits = decomposeDate(month, day),
        timeDigits = decomposeTime(hour, minute),
    )

    // Bitmap aspect ratio is ~(2*glyphW + gap + 4) : bitmapH
    // At bitmapH=300: ~365 x 300, so display at 110 x 90 dp
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0x00000000)),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            provider = ImageProvider(bitmap),
            contentDescription = "%02d/%02d %02d:%02d".format(month, day, hour, minute),
            modifier = GlanceModifier.fillMaxSize(),
        )
    }
}

private fun renderBothGlyphs(dateDigits: TimeDigits, timeDigits: TimeDigits): Bitmap {
    val bitmapH = 300
    val padding = 2
    val gap = 50
    val staveH = (bitmapH - 2 * padding).toFloat()
    val quadW = staveH * 0.30f
    val halfH = staveH * 0.30f
    val glyphW = 2 * quadW
    val bitmapW = (2 * glyphW + gap + 2 * padding).toInt()

    val bmp = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    canvas.drawColor(android.graphics.Color.TRANSPARENT)

    val strokeWidth = bitmapH * 0.025f
    val top = padding.toFloat()
    val bottom = top + staveH

    val dateCx = padding + quadW
    val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.BLUE
        this.strokeWidth = strokeWidth
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }
    canvas.drawLine(dateCx, top, dateCx, bottom, datePaint)
    for ((digit, quadrant) in listOf(
        dateDigits.units     to Quadrant.UNITS,
        dateDigits.tens      to Quadrant.TENS,
        dateDigits.hundreds  to Quadrant.HUNDREDS,
        dateDigits.thousands to Quadrant.THOUSANDS,
    )) {
        cistercianStrokes(digit, quadrant, dateCx, top, bottom, quadW, halfH).forEach { seg ->
            canvas.drawLine(seg.x1, seg.y1, seg.x2, seg.y2, datePaint)
        }
    }

    val timeCx = padding + glyphW + gap + quadW
    val timePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.RED
        this.strokeWidth = strokeWidth
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }
    canvas.drawLine(timeCx, top, timeCx, bottom, timePaint)
    for ((digit, quadrant) in listOf(
        timeDigits.units     to Quadrant.UNITS,
        timeDigits.tens      to Quadrant.TENS,
        timeDigits.hundreds  to Quadrant.HUNDREDS,
        timeDigits.thousands to Quadrant.THOUSANDS,
    )) {
        cistercianStrokes(digit, quadrant, timeCx, top, bottom, quadW, halfH).forEach { seg ->
            canvas.drawLine(seg.x1, seg.y1, seg.x2, seg.y2, timePaint)
        }
    }

    return bmp
}
