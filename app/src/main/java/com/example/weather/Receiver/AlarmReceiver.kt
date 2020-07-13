package com.example.weather.Receiver

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import com.example.weather.Activity.MainActivity
import com.example.weather.R

import java.util.Calendar;

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var notificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notificationIntent = Intent(context, MainActivity::class.java)

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
            or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        var pendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, 0)

        var builder = NotificationCompat.Builder(context, "default")

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)

            var channelName = "channelName"
            var description = "description"
            var importance = NotificationManager.IMPORTANCE_HIGH

            var channel = NotificationChannel("default", channelName, importance)
            channel.description = description


            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
            }
        }
        else {
            builder.setSmallIcon(R.mipmap.ic_launcher)
        }

        builder.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())

            .setTicker("(dd)")
            .setContentTitle("상태바 드래그 타이틀")
            .setContentText("서브타이틀")
            .setContentInfo("Info")
            .setContentIntent(pendingIntent)

        if (notificationManager != null) {
            notificationManager.notify(1234, builder.build())

            var nextNotifyTime = Calendar.getInstance()

            nextNotifyTime.add(Calendar.DATE, 1)

            var editor = context.getSharedPreferences("daily alarm",
            Context.MODE_PRIVATE).edit()
            editor.putLong("nextNotifyTime", nextNotifyTime.timeInMillis)
            editor.apply()

            var currentDateTime = nextNotifyTime.time
            //var date_text = SimpleDateFormat("yyyy")
        }
    }
}