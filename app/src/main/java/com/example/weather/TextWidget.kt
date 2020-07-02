package com.example.weather

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.appwidget.AppWidgetManager
import kotlinx.android.synthetic.main.activiy_setting.*
import android.widget.RemoteViews
import android.content.ComponentName

class TextWidget: AppWidgetProvider() {
    var inputValues: String? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        inputValues = intent?.getStringExtra("number")
        context?.let {
            var appWidgetManager = AppWidgetManager.getInstance(context)
            var widgetName = ComponentName(context.packageName, TextWidget::class.java.name)
            var widgetIds = appWidgetManager.getAppWidgetIds((widgetName))
            var actionName = intent?.action

            actionName?.let {
                if (actionName.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                    widgetIds?.let {
                        if (widgetIds.size > 0) {
                            this.onUpdate(context, appWidgetManager, widgetIds)
                        }
                    }
                }
            }
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        context?.let {
            var views = RemoteViews(context.packageName, R.layout.text_widget)
            views.setTextViewText(R.id.testText, inputValues + "")
            appWidgetManager?.updateAppWidget(appWidgetIds, views)
        }
    }
}