package com.example.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyraNotificationListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        instance = this
        _isListening.value = true
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        _isListening.value = false
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.notification?.extras?.let { extras ->
            val title = extras.getCharSequence("android.title")?.toString() ?: ""
            val text = extras.getCharSequence("android.text")?.toString() ?: ""
            val pkg = sbn.packageName ?: ""
            if (title.isNotBlank() || text.isNotBlank()) {
                _lastNotificationText.value = "$pkg: $title - $text"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (instance == this) {
            instance = null
            _isListening.value = false
        }
    }

    companion object {
        var instance: MyraNotificationListenerService? = null
            private set

        private val _isListening = MutableStateFlow(false)
        val isListening = _isListening.asStateFlow()

        private val _lastNotificationText = MutableStateFlow("")
        val lastNotificationText = _lastNotificationText.asStateFlow()
    }
}
