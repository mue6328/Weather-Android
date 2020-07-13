package com.example.weather.Widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.weather.R
import com.example.weather.Service.MyRemoteViewsService

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