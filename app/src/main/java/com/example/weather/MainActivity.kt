package com.example.weather

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.Service.WeatherService
import com.example.weather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.location.Location
import android.location.LocationManager
import androidx.core.content.getSystemService
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.Model.*
import com.example.weather.adapter.DailyWeatherAdapter
import com.example.weather.adapter.HourlyWeatherAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent
import android.widget.TextView
import android.appwidget.AppWidgetManager
import android.graphics.Paint
import android.text.SpannableString
import android.text.style.UnderlineSpan

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var weatherService: WeatherService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var hourlyWeatherAdapter = HourlyWeatherAdapter()
        var dailyWeatherAdapter = DailyWeatherAdapter()

        var hourlyWeatherList = ArrayList<Hour>()
        var dailyWeatherList = ArrayList<Day>()

        var date: Date? = null
        var sunrise: Date? = null
        var sunset: Date? = null
        var dateFormat: String? = null
        var sunriseFormat: String? = null
        var sunsetFormat: String? = null
        var cal = Calendar.getInstance()


//        binding.change.setOnClickListener {
//            var intent = Intent(this, TextWidget::class.java)
//            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//            intent.putExtra("number", "" + binding.test.text)
//            sendBroadcast(intent)
//        }
        var content: SpannableString = SpannableString(binding.tempCategory.text.toString())
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        binding.tempCategory.text = content

        binding.hourRecyclerView.run {
            adapter = hourlyWeatherAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        binding.dailyRecyclerView.run {
            adapter = dailyWeatherAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        try {
            val lm: LocationManager? = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var location: Location? = null

            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this.applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    0)
            }
            else {
                location = lm!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

//                var latitude = location.latitude
//                var longitude = location.longitude

                var latitude = 37.5010881
                var longitude = 127.0342169

                var geocoder = Geocoder(applicationContext)
                geocoder.getFromLocation(latitude,
                longitude, 1)[0].getAddressLine(0)
                Log.i("TAG", "" + geocoder.getFromLocation(latitude,
                    longitude, 1) +
                        geocoder.getFromLocation(latitude,
                            longitude, 1)[0].getAddressLine(0))

//                var longitude = 128.568975
//                var latitude = 35.8438071

                WeatherService!!.getWeather(latitude, longitude, Utils.API_KEY).enqueue(object : Callback<Weather> {
                    override fun onFailure(call: Call<Weather>, t: Throwable) {
                        Log.i("error", t.toString())
                    }

                    override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                        Log.i("response", "" + (response.body()!!.main.temp - 273.15).toInt() + " " + response.body()!!.weather[0].main
                        + " " + response.body()!!.weather[0].description + " " + response.body()!!.sys.country + " " + response.body()!!.name
                        + "\n" + response.body())

                        binding.place.text = response.body()!!.sys.country + " " + response.body()!!.name
                        binding.weather.text = response.body()!!.weather[0].description
                        binding.temp.text = (response.body()!!.main.temp - 273.15).toInt().toString() + "℃"

                        sunrise = Date(response.body()!!.sys.sunrise.toLong() * 1000)
                        sunset = Date(response.body()!!.sys.sunset.toLong() * 1000)
//                        cal.time = sunri
//                        var hours = cal.get(Calendar.HOUR_OF_DAY)
                        sunriseFormat = SimpleDateFormat("오전 hh:mm").format(sunrise)
                        sunsetFormat = SimpleDateFormat("오후 KK:mm").format(sunset)

                        sunriseFormat = sunriseFormat!!.replace("0", "")
                        sunsetFormat = sunsetFormat!!.replace("0", "")

                        binding.sunriseTime.text = sunriseFormat
                        binding.sunsetTime.text = sunsetFormat

                        Log.i("date", date.toString() + " " + dateFormat.toString())
                    }
                })
                WeatherService.getWeatherTime(latitude, longitude, "current,minutely", Utils.API_KEY)
                    .enqueue(object : Callback<HourlyWeather>{
                        override fun onFailure(call: Call<HourlyWeather>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<HourlyWeather>,
                            response: Response<HourlyWeather>
                        ) {
                            Log.i("qqq", "" + response.body() + " " + response.code() + response.message())
                            if (response.body() != null)
                            Log.i("hourly", "" + response.body()!!.hourly[0].dt)
                            binding.tempMin.text = "최저 " + (response.body()!!.daily[0].temp.min - 273.15).toInt() + "℃ / "
                            binding.tempMax.text = "최고 " + (response.body()!!.daily[0].temp.max - 273.15).toInt() + "℃"


                            for (i in response.body()!!.hourly.indices) {
                                date = Date(response.body()!!.hourly[i].dt.toLong() * 1000)
                                cal.time = date
                                var hours = cal.get(Calendar.HOUR_OF_DAY)
                                if (hours >= 12) {
                                    dateFormat = SimpleDateFormat("오후 KK시").format(date)
                                    if (dateFormat!!.contains("00")) {
                                        dateFormat = "오후 12시"
                                    }
                                    if (dateFormat!!.contains("10")) {

                                    }
                                    else {
                                        dateFormat = dateFormat!!.replace("0", "")
                                    }

                                }
                                else {
                                    dateFormat = SimpleDateFormat("오전 hh시").format(date)
                                    if (dateFormat!!.contains("10")) {

                                    }
                                    else {
                                        dateFormat = dateFormat!!.replace("0", "")
                                    }
                                }

                                hourlyWeatherList.add(Hour(dateFormat!!, "", (response.body()!!.hourly[i].temp
                                        - 273.15).toInt().toString() + "℃"))
                            }

                            for (i in response.body()!!.daily.indices) {
                                date = Date(response.body()!!.daily[i].dt.toLong() * 1000)
                                cal.time = date
                                var dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                                var korDayOfWeek = ""
                                dateFormat = SimpleDateFormat("MM.dd").format(date)
                                if (dayOfWeek == 1) {
                                    korDayOfWeek = "일"
                                }
                                else if (dayOfWeek == 2) {
                                    korDayOfWeek = "월"
                                }
                                else if (dayOfWeek == 3) {
                                    korDayOfWeek = "화"
                                }
                                else if (dayOfWeek == 4) {
                                    korDayOfWeek = "수"
                                }
                                else if (dayOfWeek == 5) {
                                    korDayOfWeek = "목"
                                }
                                else if (dayOfWeek == 6) {
                                    korDayOfWeek = "금"
                                }
                                else if (dayOfWeek == 7) {
                                    korDayOfWeek = "토"
                                }
                                dailyWeatherList.add(Day(korDayOfWeek, dateFormat!!,Utils.iconURL +
                                    response.body()!!.daily[i].weather[0].icon + ".png",
                                    response.body()!!.daily[i].weather[0].main,
                                    (response.body()!!.daily[i].temp.min - 273.15).toInt().toString(),
                                    (response.body()!!.daily[i].temp.max - 273.15).toInt().toString()))
                            }
                            Log.d("eee", dateFormat + " " + response.body()!!.hourly.size)
                            hourlyWeatherAdapter.setItem(hourlyWeatherList)
                            dailyWeatherAdapter.setItem(dailyWeatherList)
                        }

                    })
            }

            //var latitude = location.latitude
            //var longitude = location.longitude


        } catch (e: SecurityException) {
            e.printStackTrace()
        }



    }
}
