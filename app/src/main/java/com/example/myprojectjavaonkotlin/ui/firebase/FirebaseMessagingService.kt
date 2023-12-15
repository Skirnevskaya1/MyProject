package com.example.myprojectjavaonkotlin.ui.firebase

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.ui.CHANNEL_ID
import com.example.myprojectjavaonkotlin.ui.TAG_FIREBASE
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    private var notificationid = 0

    // получение сообщения
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: "Video notification"
        val body = message.notification?.body ?: "Сообщение"
        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.icon_video_color)
                setContentTitle(title)
                setContentText(body)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationid++
        val notification = notificationBuilder.build()
        notificationManager.notify(notificationid, notification)
    }

    // вывод на экран
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG_FIREBASE, token)
    }

}