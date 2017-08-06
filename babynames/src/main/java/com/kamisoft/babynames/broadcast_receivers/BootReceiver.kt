package com.kamisoft.babynames.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kamisoft.babynames.alarms.AlarmCreator

internal class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        AlarmCreator.withContext(context).initAlarms()
    }
}
