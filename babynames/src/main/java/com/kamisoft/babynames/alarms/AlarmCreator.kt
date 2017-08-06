package com.kamisoft.babynames.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kamisoft.babynames.logger.Logger

object AlarmCreator {

    object RequestCode {
        const val ALARM_CHECK_VERSIONS = 4001
    }

    fun withContext(context: Context): ContextBuilder = ContextBuilder(context)

    class ContextBuilder(private val context: Context) {
        fun initAlarms() = createAlarmDaily(context)
    }

    private fun createAlarmDaily(context: Context) {
        val isAlarmUp = isCheckVersionsAlarmUp(context)
        if (!isAlarmUp) {
            val nowMillis = System.currentTimeMillis()

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmDaily::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, RequestCode.ALARM_CHECK_VERSIONS, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nowMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            Logger.debug("Daily alarm will fire at $nowMillis")
        }
    }

    private fun isCheckVersionsAlarmUp(context: Context): Boolean {
        return PendingIntent.getBroadcast(context, RequestCode.ALARM_CHECK_VERSIONS,
                Intent(context, AlarmDaily::class.java), PendingIntent.FLAG_NO_CREATE) != null
    }
}