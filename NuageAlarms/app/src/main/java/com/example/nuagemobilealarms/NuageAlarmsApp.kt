package com.example.nuagemobilealarms

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class NuageAlarmsApp : Application() {
    companion object {
        val CHANNEL_1_ID = "alarm_notification_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarmChannel = NotificationChannel(
                CHANNEL_1_ID,
                "Alarm Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            alarmChannel.description =
                "This channel is used to display incoming alarms from the user-selected entities."

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(alarmChannel)
        }
    }
}