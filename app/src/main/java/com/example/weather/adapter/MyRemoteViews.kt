package com.example.weather.adapter

import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.content.Context
import com.example.weather.Model.Hour
import com.example.weather.R
import android.content.Intent

class MyRemoteViews : RemoteViewsService.RemoteViewsFactory {

    private var context: Context
    private var list = ArrayList<Hour>()

    constructor(context: Context) {
        this.context = context
    }

    private fun setData() {
        list.add(Hour("3시", "", ""))
        list.add(Hour("6시", "", ""))
        list.add(Hour("9시", "", ""))
    }

    override fun onCreate() {
        setData()
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
       // setData()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        var recyclerViewWidget = RemoteViews(context.packageName, R.layout.hourly_weather_item2)
        recyclerViewWidget.setTextViewText(R.id.timeHourly2, list[position].time)
//        recyclerViewWidget.setTextViewText(R.id.iconHourly2, list[position].icon)
//        recyclerViewWidget.setTextViewText(R.id.tempHourly2, list[position].temp)

        var intent = Intent()
        intent.putExtra("time", list[position].time)
        intent.putExtra("icon", list[position].icon)
        intent.putExtra("temp", list[position].temp)
        recyclerViewWidget.setOnClickFillInIntent(R.id.timeHourly2, intent)

        return recyclerViewWidget
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