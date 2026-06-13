package com.cistercian.clock

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class CistercianClockReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = CistercianClockWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // ACTION_TIME_TICK fires every minute while the screen is on; we rely
        // on the WorkManager job (below) for background / screen-off updates.
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        ClockUpdateWorker.schedule(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        ClockUpdateWorker.cancel(context)
    }
}
