package com.example.weather

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.weather.Service.MyRemoteViewsService

class RecyclerViewWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

//        for (int i..appWidgetIds) {
//            upda
//        }
        var serviceIntent = Intent(context, MyRemoteViewsService::class.java)
        var widget = RemoteViews(context!!.packageName, R.layout.text_widget)
        widget.setRemoteAdapter(R.id.listView, serviceIntent)

        appWidgetManager!!.updateAppWidget(appWidgetIds, widget)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}