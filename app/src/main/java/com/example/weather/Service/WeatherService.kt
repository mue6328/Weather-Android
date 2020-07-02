package com.example.weather.Service

import com.example.weather.Model.Forecast
import com.example.weather.Model.HourlyWeather
import com.example.weather.Model.Weather
import com.example.weather.Utils
import retrofit2.Call
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
class WeatherService {
    interface WeatherServiceImpl {
        @GET("forecast/")
        fun getForecast(
            @Query("lat") lat: Number,
            @Query("lon") lon: Number,
            @Query("appid") appid: String
        ): Call<Forecast>

        @GET("weather/")
        fun getWeather(
            @Query("lat") lat: Number,
            @Query("lon") lon: Number,
            @Query("appid") appid: String
        ): Call<Weather>

        @GET("onecall")
        fun getWeatherTime(
            @Query("lat") lat: Number,
            @Query("lon") lon: Number,
            @Query("exclude") exclude: String,
            @Query("appid") appid: String
        ): Call<HourlyWeather>
    }

        companion object {
            fun getWeather(lat: Number, lon: Number, appid: String): Call<Weather> {
                return Utils.retrofit_Weather.create(WeatherServiceImpl::class.java).getWeather(lat, lon, appid)
            }

            fun getWeatherTime(lat: Number, lon: Number, exclude: String, appid: String): Call<HourlyWeather> {
                return Utils.retrofit_Weather.create(WeatherServiceImpl::class.java).getWeatherTime(lat, lon, exclude, appid)
            }
        }

}