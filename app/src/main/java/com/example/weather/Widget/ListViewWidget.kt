package com.example.weather.Widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather.Model.Hour
import com.example.weather.Model.HourlyWeather
import com.example.weather.R
import com.example.weather.Service.MyRemoteViewsService
import com.example.weather.Service.WeatherService
import com.example.weather.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ListViewWidget : AppWidgetProvider() {
    private fun updateAppWidget(context: Context?, appWidgetManager: AppWidgetManager,
    appWidgetId: Int) {
        var widgetText: CharSequence = context!!.getString(R.string.app_name)
        var views = RemoteViews(context.packageName,
            R.layout.listview_widget
        )
        //views.setTextViewText(R.id.testText, widgetText)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
//        for (appwidgetId in appWidgetIds!!) {
//            updateAppWidget(context, appWidgetManager!!, appwidgetId)
//        }
        var serviceIntent = Intent(context, MyRemoteViewsService::class.java)

        var widget = RemoteViews(context!!.packageName,
            R.layout.listview_widget
        )
        widget.setRemoteAdapter(R.id.listView, serviceIntent)

        appWidgetManager!!.updateAppWidget(appWidgetIds, widget)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}