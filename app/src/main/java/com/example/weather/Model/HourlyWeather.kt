package com.example.weather.Model

import android.os.Parcel
import android.os.Parcelable

data class HourlyWeather (
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int,
    var current: Current,
    var minutely: List<Minutely>,
    var hourly: List<Hourly>,
    var daily: List<Daily>
)

data class Hour (
    var time: String,
    var icon: String,
    var temp: String
)

data class Day(
    var dayOfWeek: String,
    var date: String,
    var icon: String,
    var weatherInfo: String,
    var min: String,
    var max: String
)

data class Current (
    var dt: Int,
    var sunrise: Int,
    var sunset: Int,
    var temp: Double,
    var feels_like: Double,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Double,
    var uvi: Double,
    var clouds: Int,
    var visibility: Int,
    var wind_speed: Double,
    var wind_deg: Int,
    var weather: List<HourWeather>,
    var rain: Rain
)

data class HourWeather (
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)

data class Rain(
    var lh: Double
)

data class Minutely(
    var dt: Int,
    var precipitation: Double
)

data class Hourly(
    var dt: Int,
    var temp: Double,
    var feels_like: Double,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Double,
    var clouds: Int,
    var wind_speed: Double,
    var wind_deg: Int,
    var weather: List<HourWeather>,
    var rain: Rain
)

data class Daily (
    var dt: Int,
    var sunrise: Int,
    var sunset: Int,
    var temp: Temp,
    var feels_like: FeelsLike,
    var pressure: Int,
    var humidity: Int,
    var dew_point: Double,
    var wind_speed: Double,
    var wind_deg: Int,
    var weather: List<HourWeather>,
    var clouds: Int,
    var rain: Double,
    var uvi: Double
)

data class Temp (
    var day: Double,
    var min: Double,
    var max: Double,
    var night: Double,
    var eve: Double,
    var morn: Double
)

data class FeelsLike (
    var day: Double,
    var night: Double,
    var eve: Double,
    var morn: Double
)