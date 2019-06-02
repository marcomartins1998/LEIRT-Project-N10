package com.example.nuagemobilealarms

import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AlarmNotificationsService : FirebaseMessagingService() {
    override fun onMessageReceived(rmsg: RemoteMessage?) {
        //super.onMessageReceived(rmsg)
        println(rmsg?.data.toString())
        val notificationManager = NotificationManagerCompat.from(this)

        val notification = NotificationCompat.Builder(this, NuageAlarmsApp.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(rmsg?.notification?.title)
            .setContentText(rmsg?.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(1, notification)
    }

}