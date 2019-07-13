package com.example.nuagemobilealarms

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.nuagemobilealarms.helper.FileHelper
import com.example.nuagemobilealarms.model.Filters
import com.google.firebase.messaging.FirebaseMessaging

class NuageAlarmsApp : Application() {
    companion object {
        val CHANNEL_1_ID = "alarm_notification_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        clearPreviousUnRememberedUserInfo()
        //subscribeToAlarms()
    }

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

    fun clearPreviousUnRememberedUserInfo() {
        val fh = FileHelper(this.applicationContext)
        if (!fh.getProperties().noneNullOrEmpty() && fh.getCurrentSubscription().isNotEmpty())
            FirebaseMessaging.getInstance().unsubscribeFromTopic(fh.getCurrentSubscription())
        fh.putFilters("NoIP", "NoUsername", Filters(null, null))
    }

    /*fun subscribeToAlarms(){
        val fm = FirebaseMessaging.getInstance()
        fm.unsubscribeFromTopic("Alarms")
    }*/
}