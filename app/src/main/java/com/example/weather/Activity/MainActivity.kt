package com.example.weather.Activity

import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.Model.Day
import com.example.weather.Model.Hour
import com.example.weather.Model.HourlyWeather
import com.example.weather.Model.Weather
import com.example.weather.Service.WeatherService
import com.example.weather.adapter.DailyWeatherAdapter
import com.example.weather.adapter.HourlyWeatherAdapter
import com.example.weather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import android.widget.Toast
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Intent
import android.content.ComponentName
import com.example.weather.R
import com.example.weather.Receiver.AlarmReceiver
import com.example.weather.Receiver.DeviceBootReceiver
import com.example.weather.Utils
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var weatherService: WeatherService? = null

    var nHourDay: Int? = null
    var nMinute: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var hourlyWeatherAdapter = HourlyWeatherAdapter()
        var dailyWeatherAdapter = DailyWeatherAdapter()

        var hourlyWeatherList = ArrayList<Hour>()
        var dailyWeatherList = ArrayList<Day>()

        var date: Date? = null
        var sunrise: Date
        var sunset: Date
        var dateFormat: String? = null
        var sunriseFormat: String? = null
        var sunsetFormat: String? = null
        var cal = Calendar.getInstance()

//        var dd = intent.extras
//        Log.d("ddd", dd!!.getString("ddd"))


//        binding.change.setOnClickListener {
//            var intent = Intent(this, TextWidget::class.java)
//            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//            intent.putExtra("number", "" + binding.test.text)
//            sendBroadcast(intent)
//        }

//        var content: SpannableString = SpannableString(binding.tempCategory.text.toString())
//        content.setSpan(UnderlineSpan(), 0, content.length, 0)
//        binding.tempCategory.text = content

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

//                var latitude = 37.5010881
//                var longitude = 127.0342169

                var longitude = 128.568975
                var latitude = 35.8438071

                var geocoder = Geocoder(applicationContext, Locale.KOREA)
                geocoder.getFromLocation(latitude,
                longitude, 1)[0].getAddressLine(0)
                Log.i("TAG", "" + geocoder.getFromLocation(latitude,
                    longitude, 1) +
                        geocoder.getFromLocation(latitude,
                            longitude, 1)[0].getAddressLine(0))






                WeatherService!!.getWeather(latitude, longitude,
                    Utils.API_KEY
                ).enqueue(object : Callback<Weather> {
                    override fun onFailure(call: Call<Weather>, t: Throwable) {
                        Log.i("error", t.toString())
                    }

                    override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                        Log.i("response", "" + (response.body()!!.main.temp - 273.15).toInt() + " " + response.body()!!.weather[0].main
                        + " " + response.body()!!.weather[0].description + " " + response.body()!!.sys.country + " " + response.body()!!.name
                        + "\n" + response.body())

                        var array = geocoder.getFromLocation(latitude,
                            longitude, 1)[0].getAddressLine(0).split(" ")

                        binding.place.text = (geocoder.getFromLocation(latitude, longitude, 1)[0].adminArea) + " " + array[2] + " " +
                                geocoder.getFromLocation(latitude, longitude, 1)[0].thoroughfare
                        Log.i("weatherInfo", "" + response.body()!!.weather[0].main
                        + "")
                        binding.temp.text = (response.body()!!.main.temp - 273.15).toInt().toString() + "℃"

                        //var uri = Uri.parse(Utils.iconURL + response.body()!!.weather[0].icon + ".png")
                        //binding.weatherIcon.setImageURI(uri)

                        sunrise = Date(response.body()!!.sys.sunrise.toLong() * 1000)
                        sunset = Date(response.body()!!.sys.sunset.toLong() * 1000)
//                        cal.time = sunri
//                        var hours = cal.get(Calendar.HOUR_OF_DAY)
//                        var sunriseFormatCheck: String
//                        var sunsetFormatCheck: String

                        sunriseFormat = SimpleDateFormat("오전 hh:mm").format(sunrise)
                        sunsetFormat = SimpleDateFormat("오후 KK:mm").format(sunset)

//                        sunriseFormatCheck = SimpleDateFormat("mm").format(sunrise)
//                        sunsetFormatCheck = SimpleDateFormat("mm").format(sunset)

//                        if (sunriseFormatCheck.toInt() % 10 == 0) {
//
//                        }
//                        else if (sunriseFormatCheck.toInt() % 10 != 0) {
//                            sunriseFormat = sunriseFormat!!.replace("0", "")
//                        }
//
//                        if (sunsetFormatCheck.toInt() % 10 == 0) {
//
//                        }
//                        else if (sunsetFormatCheck.toInt() % 10 != 0){
//                            sunsetFormat = sunsetFormat!!.replace("0", "")
//                        }

                        binding.sunriseTime.text = sunriseFormat
                        binding.sunsetTime.text = sunsetFormat

                        Log.i("date", date.toString() + " " + dateFormat.toString())
                    }
                })
                WeatherService.getWeatherTime(latitude, longitude, "current,minutely",
                    Utils.API_KEY
                )
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

                            if (response.body()!!.hourly[0].weather[0].main == "Clouds") {
                                binding.weather.text = "구름 조금"
                                binding.weatherIcon.setImageResource(R.drawable.ic_wb_cloudy_black_24dp)
                            }
                            else if (response.body()!!.hourly[0].weather[0].main == "Clear") {
                                binding.weather.text = "맑음"
                                binding.weatherIcon.setImageResource(R.drawable.ic_wb_sunny_black_24dp)
                            }
                            else if (response.body()!!.hourly[0].weather[0].main == "Rain") {
                                binding.weather.text = "비"
                                binding.weatherIcon.setImageResource(R.drawable.rain)
                            }
                            else if (response.body()!!.hourly[0].weather[0].main == "Haze") {
                                binding.weather.text = "안개"
                                binding.weatherIcon.setImageResource(R.drawable.haze)
                            }


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

                                hourlyWeatherList.add(Hour(dateFormat!!, response.body()!!.hourly[i].weather[0].main, (response.body()!!.hourly[i].temp
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
                                dailyWeatherList.add(Day(korDayOfWeek, dateFormat!!,
                                    response.body()!!.daily[i].weather[0].main,
                                    response.body()!!.daily[i].weather[0].main,
                                    (response.body()!!.daily[i].temp.min - 273.15).toInt().toString(),
                                    (response.body()!!.daily[i].temp.max - 273.15).toInt().toString()))
                            }
                            Log.d("eee", dateFormat + " " + response.body()!!.hourly.size)
                            hourlyWeatherAdapter.setItem(hourlyWeatherList)
                            dailyWeatherAdapter.setItem(dailyWeatherList)
                        }

                    })

                binding.tempFeelsCategory.setOnClickListener {
                    WeatherService.getWeatherTime(latitude, longitude, "current,minutely,daily",
                        Utils.API_KEY
                    )
                        .enqueue(object : Callback<HourlyWeather>{
                            override fun onFailure(call: Call<HourlyWeather>, t: Throwable) {

                            }

                            override fun onResponse(
                                call: Call<HourlyWeather>,
                                response: Response<HourlyWeather>
                            ) {
                                Log.i("feel", "" + response.body())
                                hourlyWeatherList.clear()
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

                                    hourlyWeatherList.add(Hour(dateFormat!!,
                                        response.body()!!.hourly[i].weather[0].main,
                                        (response.body()!!.hourly[i].feels_like - 273.15).toInt().toString() + "℃"))
                                }
                                hourlyWeatherAdapter.setItem(hourlyWeatherList)
                            }
                        })
                }

                binding.tempCategory.setOnClickListener {
                    WeatherService.getWeatherTime(latitude, longitude, "current,minutely,daily",
                        Utils.API_KEY
                    )
                        .enqueue(object : Callback<HourlyWeather>{
                            override fun onFailure(call: Call<HourlyWeather>, t: Throwable) {

                            }

                            override fun onResponse(
                                call: Call<HourlyWeather>,
                                response: Response<HourlyWeather>
                            ) {
                                Log.i("feel", "" + response.body())
                                hourlyWeatherList.clear()
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

                                    hourlyWeatherList.add(Hour(dateFormat!!,
                                        response.body()!!.hourly[i].weather[0].main,
                                        (response.body()!!.hourly[i].temp - 273.15).toInt().toString() + "℃"))
                                }
                                hourlyWeatherAdapter.setItem(hourlyWeatherList)
                            }
                        })
                }

                binding.humidityCategory.setOnClickListener {
                    WeatherService.getWeatherTime(latitude, longitude, "current,minutely,daily",
                        Utils.API_KEY
                    )
                        .enqueue(object : Callback<HourlyWeather>{
                            override fun onFailure(call: Call<HourlyWeather>, t: Throwable) {

                            }

                            override fun onResponse(
                                call: Call<HourlyWeather>,
                                response: Response<HourlyWeather>
                            ) {
                                Log.i("feel", "" + response.body())
                                hourlyWeatherList.clear()
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

                                    hourlyWeatherList.add(Hour(dateFormat!!, "Humidity", (response.body()!!.hourly[i].humidity.toString() + "%")))
                                }
                                hourlyWeatherAdapter.setItem(hourlyWeatherList)
                            }
                        })
                }
            }


        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_actions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var cal = Calendar.getInstance()
        when(item.itemId) {
            R.id.action_notification -> {
//                Toast.makeText(this, "dd", Toast.LENGTH_SHORT).show()
//                var dialog = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener
//                { datePicker, i, i2, i3 ->
//
//                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
//                dialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
//                dialog.show()
                var dialog = TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    var sharedPreferences = getSharedPreferences("daily alarm", Context.MODE_PRIVATE)
                    var millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().timeInMillis)

                    var nextNotifyTime = GregorianCalendar()
                    nextNotifyTime.timeInMillis = millis

                    var nextDate = nextNotifyTime.time

                    var currentTime = nextNotifyTime.time

                    var calendar = Calendar.getInstance()
                    var hour: Int
                    var minute = timePicker.minute
                    if (timePicker.hour > 12) {
                        hour = timePicker.hour
                    }
                    else {
                        hour = timePicker.hour
                    }

                    calendar.timeInMillis = System.currentTimeMillis()
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)

                    if (calendar.before(Calendar.getInstance())) {
                        calendar.add(Calendar.DATE, 1)
                    }

                    var currentDateTime = calendar.time

                    var editor =
                        getSharedPreferences("daily alarm", Context.MODE_PRIVATE).edit()
                    editor.putLong("nextNotifyTime", calendar.timeInMillis)
                    editor.apply()

                    if (hour > 12) {
                        hour -= 12
                        Toast.makeText(this, "알람이 오후 " + hour + "시 " + minute + "분으로 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "알람이 오전 " + hour + "시 " + minute + "분으로 설정되었습니다.", Toast.LENGTH_SHORT).show()
                    }


                    diaryNotification(calendar)
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
                dialog.setTitle("dd")
                dialog.show()


            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun diaryNotification(calendar: Calendar) {
        var dailyNotify = true

        var pm = this.packageManager
        var receiver = ComponentName(this, DeviceBootReceiver::class.java)
        var alarmIntent = Intent(this, AlarmReceiver::class.java)
        var pendingIntent = PendingIntent
            .getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        var alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager



        if (dailyNotify) {
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntent)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }

        pm.setComponentEnabledSetting(receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP)
    }
}
