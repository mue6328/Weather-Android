package com.example.weather.Receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Objects

class DeviceBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Objects.equals(intent!!.action, "android.intent.action.BOOT_COMPLETED")) {
            var alarmIntent = Intent(context, AlarmReceiver::class.java)
            var pendingIntent =
                PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

            var manager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            var sharedPreferences =
                context.getSharedPreferences("daily alarm", Context.MODE_PRIVATE)
            var millis = sharedPreferences.
            getLong("nextNotifyTime", Calendar.getInstance().timeInMillis)

            var current_calender = Calendar.getInstance()
            var nextNotifyTime = GregorianCalendar()
            nextNotifyTime.timeInMillis = sharedPreferences.getLong("nextNotifyTime", millis)

            if (current_calender.after(nextNotifyTime)) {
                nextNotifyTime.add(Calendar.DATE, 1)
            }

            var currentDateTime = nextNotifyTime.time

            if (manager != null) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, nextNotifyTime.timeInMillis
                    ,AlarmManager.INTERVAL_DAY, pendingIntent)
            }
        }
    }

}