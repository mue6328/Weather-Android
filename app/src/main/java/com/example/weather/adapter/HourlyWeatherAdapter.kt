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
import com.example.weather.Model.Hour
import com.example.weather.databinding.HourlyWeatherItemBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List

class HourlyWeatherAdapter : RecyclerView.Adapter<HourlyWeatherAdapter.Holder>() {

    private var hourlyWeather = ArrayList<Hour>()

    fun setItem(list: ArrayList<Hour>) {
        this.hourlyWeather = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (hourlyWeather != null)
            hourlyWeather.size
        else
            0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.timeHourly.text = hourlyWeather[position].time
        //holder.binding.icon
        holder.binding.tempHourly.text = hourlyWeather[position].temp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            HourlyWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    class Holder(val binding: HourlyWeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {}
}