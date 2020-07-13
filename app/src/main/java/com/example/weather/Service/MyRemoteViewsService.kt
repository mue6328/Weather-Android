package com.example.weather.Service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViewsService
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather.Model.Hour
import com.example.weather.Model.HourlyWeather
import com.example.weather.Utils
import com.example.weather.adapter.MyRemoteViews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        var list = ArrayList<Hour>()

        try {
            val lm: LocationManager? =
                this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var location: Location? = null

            var cal = Calendar.getInstance()
            var dateFormat: String? = null
            var date: Date? = null

            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this.applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    Activity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    0)
            }
            else {
                location = lm!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                //var latitude = location.latitude
                //var longitude = location.longitude

                var latitude = 37.5010881
                var longitude = 127.0342169

                WeatherService.getWeatherTime(
                    latitude,
                    longitude,
                    "current,minutely,daily",
                    Utils.API_KEY
                )
                    .enqueue(object : Callback<HourlyWeather> {
                        override fun onFailure(call: Call<HourlyWeather>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<HourlyWeather>,
                            response: Response<HourlyWeather>
                        ) {
                            Log.i("feel", "" + response.body())
                            for (i in 1..13 step 3) {
                                date = Date(response.body()!!.hourly[i].dt.toLong() * 1000)
                                cal.time = date
                                var hours = cal.get(Calendar.HOUR_OF_DAY)
                                if (hours >= 12) {
                                    dateFormat = SimpleDateFormat("오후 KK시").format(date)
                                    if (dateFormat!!.contains("00")) {
                                        dateFormat = "오후 12시"
                                    }
                                    if (dateFormat!!.contains("10")) {

                                    } else {
                                        dateFormat = dateFormat!!.replace("0", "")
                                    }

                                } else {
                                    dateFormat = SimpleDateFormat("오전 hh시").format(date)
                                    if (dateFormat!!.contains("10")) {

                                    } else {
                                        dateFormat = dateFormat!!.replace("0", "")
                                    }
                                }

                                list.add(Hour(dateFormat!!,
                                    "",
                                    (response.body()!!.hourly[i].temp - 273.15).toInt().toString() + "℃"))

//                        hourlyWeatherList.add(Hour(dateFormat!!,
//                            Utils.iconURL + response.body()!!.hourly[i].weather[0].icon + ".png",
//                            (response.body()!!.hourly[i].temp - 273.15).toInt().toString() + "℃"))
                            }
                        }
                    })
            }
        } catch (e: Exception) {

        }
//        list.add(Hour("3", "2", "3"))
//        list.add(Hour("4", "2", "3"))
//        list.add(Hour("5", "2", "3"))
//        list.add(Hour("6", "2", "3"))
//        list.add(Hour("7", "2", "3"))
        //Activity().findViewById<>()

        return MyRemoteViews(this.applicationContext, list)
    }
}