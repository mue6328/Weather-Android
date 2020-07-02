package com.example.weather.Service

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.weather.adapter.MyRemoteViews

class MyRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return MyRemoteViews(this.applicationContext)
    }
}