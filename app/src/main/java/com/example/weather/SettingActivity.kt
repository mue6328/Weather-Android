package com.example.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.appwidget.AppWidgetManager
import kotlinx.android.synthetic.main.activiy_setting.*
import android.widget.RemoteViews
import com.example.weather.databinding.ActiviySettingBinding
import android.content.Intent
import android.app.Activity
import android.widget.Button

class SettingActivity: AppCompatActivity() {
    //var binding: Activity
    lateinit var binding: ActiviySettingBinding

    var widgetid: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActiviySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bundle = intent.extras
        bundle?.let {
            widgetid = it.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        setUi()
    }

    private fun setUi() {
        binding.change.setOnClickListener {
            val widgetManager = AppWidgetManager.getInstance(this)
            val remoteView = RemoteViews(packageName, R.layout.text_widget)
            //remoteView.setTextViewText(R.id.testText, "QQQ")
            widgetManager?.updateAppWidget(widgetid!!, remoteView)

            var intent = Intent()
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetid)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}