package com.example.nuagemobilealarms

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging

class NuageAlarmsApp : Application() {
    companion object {
        val CHANNEL_SERVICE_ID = "alarm_listener_channel"
        val CHANNEL_1_ID = "alarm_notification_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        subscribeToAlarms()
    }

    /*fun createServiceChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_SERVICE_ID,
                "Alarm Listener Channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }*/

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarmChannel = NotificationChannel(
                CHANNEL_1_ID,
                "Alarm Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            alarmChannel.description =
                "This channel is used to display incoming alarms from the user-selected entities."

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(alarmChannel)
        }
    }

    fun subscribeToAlarms(){
        val fm = FirebaseMessaging.getInstance()
        fm.subscribeToTopic("Alarms")
    }
}