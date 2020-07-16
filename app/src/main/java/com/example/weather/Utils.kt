package com.example.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Utils {

    companion object {
        private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        var API_KEY = "ecbb48255f0505698c7de4fb3814b7e1"

        val retrofit_Weather = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
