package com.example.weather.Receiver

import android.app.Activity
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log

import androidx.core.app.NotificationCompat;
import com.example.weather.Activity.MainActivity
import com.example.weather.R

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather.Model.HourlyWeather
import com.example.weather.Model.Weather
import com.example.weather.Service.WeatherService
import com.example.weather.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var notificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra("ddd", "qqq")

        notificationIntent.setFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_SINGLE_TOP
        )

        var pendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, 0)

        var extra: Bundle = intent!!.extras!!

        Log.i("dd", "" + notificationIntent.getStringExtra("dd"))

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
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher)
        }

        try {
            val lm: LocationManager? =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var location: Location? = null

            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    Activity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            }
            else {
                location = lm!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

//                var latitude = location.latitude
//                var longitude = location.longitude

                var longitude = 128.568975
                var latitude = 35.8438071

            WeatherService.getWeather(latitude, longitude, Utils.API_KEY)
                .enqueue(object : Callback<Weather> {
                    override fun onFailure(call: Call<Weather>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                        Log.d("response", "" + response.code() + response.body())
                        var geocoder = Geocoder(context, Locale.KOREA)
                        var title: String? = null
                        if (response.body()!!.weather[0].main == "Clear") {
                            title = "(" + geocoder.getFromLocation(
                                latitude,
                                longitude,
                                1
                            )[0].thoroughfare + ") " + (response.body()!!.main.temp - 273.15).toInt()
                                .toString() + "℃ 맑음"
                        } else if (response.body()!!.weather[0].main == "Clouds") {
                            title = "(" + geocoder.getFromLocation(
                                latitude,
                                longitude,
                                1
                            )[0].thoroughfare + ") " + (response.body()!!.main.temp - 273.15).toInt()
                                .toString() + "℃ 구름 조금"
                        } else if (response.body()!!.weather[0].main == "Rain") {
                            title = "(" + geocoder.getFromLocation(
                                latitude,
                                longitude,
                                1
                            )[0].thoroughfare + ") " +
                                    (response.body()!!.main.temp - 273.15).toInt()
                                        .toString() + "℃ 비"
                        } else if (response.body()!!.weather[0].main == "Haze") {
                            title = "(" + geocoder.getFromLocation(
                                latitude,
                                longitude,
                                1
                            )[0].thoroughfare + ") " +
                                    (response.body()!!.main.temp - 273.15).toInt().toString() + "℃ 안개"
                        }

                        WeatherService.getWeatherTime(
                            latitude,
                            longitude,
                            "current,minutely,hourly",
                            Utils.API_KEY
                        )
                            .enqueue(object : Callback<HourlyWeather> {
                                override fun onFailure(call: Call<HourlyWeather>, t: Throwable) {

                                }

                                override fun onResponse(
                                    call: Call<HourlyWeather>,
                                    response: Response<HourlyWeather>
                                ) {
                                    builder.setAutoCancel(true)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setWhen(System.currentTimeMillis())
                                        .setTicker("(dd)")
                                        .setContentTitle(title)
                                        .setContentText(
                                            "최저/최고: " +
                                                    (response.body()!!.daily[0].temp.min - 273.15).toInt() + "℃/" +
                                                    (response.body()!!.daily[0].temp.max - 273.15).toInt() + "℃"
                                        )
                                        .setContentInfo("Info")
                                        .setContentIntent(pendingIntent)

                                    if (notificationManager != null) {
                                        notificationManager.notify(1234, builder.build())

                                        var nextNotifyTime = Calendar.getInstance()

                                        nextNotifyTime.add(Calendar.DATE, 1)

                                        var editor = context.getSharedPreferences(
                                            "daily alarm",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                        editor.putLong(
                                            "nextNotifyTime",
                                            nextNotifyTime.timeInMillis
                                        )
                                        editor.apply()

                                        var currentDateTime = nextNotifyTime.time
                                        //var date_text = SimpleDateFormat("yyyy")
                                    }
                                }
                            })


                    }

                })
        }

        } catch (e: Exception) {

        }
    }
}