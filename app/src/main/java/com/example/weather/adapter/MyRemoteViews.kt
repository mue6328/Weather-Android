package com.example.weather.adapter

import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.content.Context
import com.example.weather.Model.Hour
import com.example.weather.R
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.Canvas
import androidx.core.content.ContextCompat

class MyRemoteViews : RemoteViewsService.RemoteViewsFactory {

    private var context: Context
    private var list = ArrayList<Hour>()

    constructor(context: Context, list: ArrayList<Hour>) {
        this.context = context
        this.list = list
    }

    override fun onCreate() {
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
       //setData(list)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        var listViewWidget = RemoteViews(context.packageName, R.layout.hourly_weather_item2)
        lateinit var icon : Bitmap
        //listViewWidget.set
        listViewWidget.setTextViewText(R.id.timeHourly2, list[position].time)

//        var icon: ImageView = AppCompatActivity().findViewById(R.id.iconHourly2)
//        Glide.with(context)
//            .load(list[position].icon)
//            .into(icon)
//        listViewWidget.setImageViewUri(R.id.iconHourly2, Uri.parse(list[position].icon))
//        listViewWidget.setTextViewText(R.id.iconHourly2, list[position].icon)
        //listViewWidget.setTextViewText(R.id.test, "test")
        //context.getDrawable(context.resources, R.drawable.ic_wb_cloudy_black_24dp)
        var drawable: Drawable? = null
        var bitmap: Bitmap? = null


        if (list[position].icon == "Clear") {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_wb_sunny_black_24dp)
        }
        else if (list[position].icon == "Clouds") {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_wb_cloudy_black_24dp)
        }
        else if (list[position].icon == "Rain") {
            drawable = ContextCompat.getDrawable(context, R.drawable.rain)

        }
        else if (list[position].icon == "Haze") {
            drawable = ContextCompat.getDrawable(context, R.drawable.haze)
        }
        bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)

        var canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        //bitmap = (drawable as BitmapDrawable).bitmap

        listViewWidget.setImageViewBitmap(R.id.iconHourly2, bitmap)
        listViewWidget.setTextViewText(R.id.tempHourly2, list[position].temp)

        var intent = Intent()
        intent.putExtra("time", list[position].time)
        intent.putExtra("icon", list[position].icon)
        intent.putExtra("temp", list[position].temp)
        listViewWidget.setOnClickFillInIntent(R.id.timeHourly2, intent)

        return listViewWidget
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }
}