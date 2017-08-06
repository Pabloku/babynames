package com.kamisoft.babynames.alarms


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kamisoft.babynames.domain.usecase.CheckAppVersions
import com.kamisoft.babynames.logger.Logger

class AlarmDaily : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Logger.debug("Alarm fired")
        Thread(CheckAppVersions(context)).start()
    }
}