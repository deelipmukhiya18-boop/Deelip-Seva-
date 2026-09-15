package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.model.OrbStyle

class Myra3DWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val prefs = context.getSharedPreferences("myra_orb_prefs", Context.MODE_PRIVATE)
        val styleName = prefs.getString("selected_3d_style", OrbStyle.CLASSIC.name) ?: OrbStyle.CLASSIC.name
        val activeStyle = try {
            OrbStyle.valueOf(styleName)
        } catch (_: Exception) {
            OrbStyle.CLASSIC
        }

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, activeStyle)
        }
    }

    companion object {
        const val ACTION_OPEN_VOICE = "com.example.ACTION_OPEN_VOICE"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            style: OrbStyle
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_myra_3d)

            // Update 3D Holographic Core image & label
            views.setImageViewResource(R.id.widget_3d_core_image, style.drawableResId)
            views.setTextViewText(R.id.widget_core_name_text, "3D CORE: ${style.displayName.uppercase()}")
            views.setTextViewText(R.id.widget_status_text, "● ${style.subtitle} • ONLINE")

            // Tap root to open MainActivity
            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val openAppPendingIntent = PendingIntent.getActivity(
                context,
                0,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root_container, openAppPendingIntent)

            // Tap Mic button to open directly in 3D Voice Mode
            val voiceIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(ACTION_OPEN_VOICE, true)
            }
            val voicePendingIntent = PendingIntent.getActivity(
                context,
                1,
                voiceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_mic_action_btn, voicePendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun updateAllWidgets(context: Context, style: OrbStyle) {
            val prefs = context.getSharedPreferences("myra_orb_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("selected_3d_style", style.name).apply()

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, Myra3DWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            for (widgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, widgetId, style)
            }
        }

        fun pinWidgetToHomeScreen(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val myProvider = ComponentName(context, Myra3DWidgetProvider::class.java)
                return if (appWidgetManager.isRequestPinAppWidgetSupported) {
                    appWidgetManager.requestPinAppWidget(myProvider, null, null)
                    true
                } else {
                    false
                }
            }
            return false
        }
    }
}
