package com.example.weather.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import java.util.*
import android.widget.ImageView
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.weather.Model.Day
import com.example.weather.Model.Hour
import com.example.weather.databinding.DailyWeatherItemBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List

class DailyWeatherAdapter : RecyclerView.Adapter<DailyWeatherAdapter.Holder>() {

    private var dailyWeather = ArrayList<Day>()

    fun setItem(list: ArrayList<Day>) {
        this.dailyWeather = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (dailyWeather != null)
            dailyWeather.size
        else
            0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.dayOfWeek.text = dailyWeather[position].dayOfWeek
        holder.binding.date.text = dailyWeather[position].date

        Glide.with(holder.itemView.context)
            .load(dailyWeather[position].icon)
            .into(holder.binding.icon)

        holder.binding.weatherInfo.text = dailyWeather[position].weatherInfo
        holder.binding.tempMin.text = dailyWeather[position].min + "℃"
        holder.binding.tempMax.text = dailyWeather[position].max + "℃"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            DailyWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    class Holder(val binding: DailyWeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {}
}