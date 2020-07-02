package com.example.weather.Model

data class Forecast(
    var cod: String,
    var message: Int,
    var cnt: Int,
    var list: List<ForecastList>,
    var city: City
)

data class Weather (
    var coord: Coord,
    var weather: List<WeatherInfo>,
    var base: String,
    var main: MainWeather,
    var wind: Wind,
    var clouds: Cloud,
    var dt: Long,
    var sys: SysWeather,
    var timezone: Int,
    var id: Int,
    var name: String,
    var cod: Int
)

data class ForecastList(
    var dt: Long,
    var main: MainForecast,
    var weather: List<WeatherInfo>,
    var clouds: Cloud,
    var wind: Wind,
    var sys: SysForecast,
    var dt_txt: String
)

data class MainForecast(
    var temp: Double,
    var feels_like: Number,
    var temp_min: Number,
    var temp_max: Number,
    var pressure: Int,
    var sea_level: Int,
    var grnd_level: Int,
    var humidity: Int,
    var temp_kf: Number
)


data class MainWeather(
    var temp: Double,
    var feels_like: Number,
    var temp_min: Double,
    var temp_max: Double,
    var pressure: Int,
    var humidity: Int
)

data class WeatherInfo(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)

data class Cloud(
    var all: Int
)

data class Wind(
    var speed: Number,
    var deg: Number
)

data class SysForecast(
    var pod: String
)

data class SysWeather(
    var type: Int,
    var id: Int,
    var message: Number,
    var country: String,
    var sunrise: Int,
    var sunset: Int
)

data class City(
    var id: Int,
    var name: String,
    var coord: Coord,
    var country: String,
    var timezone: Int,
    var sunrise: Int,
    var sunset: Int
)

data class Coord(
    var lat: Number,
    var lon: Number
)
