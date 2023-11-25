package ru.dima.firstproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class ApplicationClass : Application() {

    companion object {
        const val CHANNEL_ID_1 = "CHANNEL_1"
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_ID_1,
                "Channel(1)",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "Channel 1 Description"

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }
}