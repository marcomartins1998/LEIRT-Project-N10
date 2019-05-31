package com.example.nuagemobilealarms

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.nuagemobilealarms.NuageAlarmsApp.Companion.CHANNEL_1_ID
import com.example.nuagemobilealarms.NuageAlarmsApp.Companion.CHANNEL_SERVICE_ID
import com.example.nuagemobilealarms.helper.FileHelper
import com.example.nuagemobilealarms.mock.AlarmBuilder
import com.example.nuagemobilealarms.model.Alarm
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class AlarmListenerService : Service() {
    lateinit var fh: FileHelper
    lateinit var notificationManager: NotificationManagerCompat
    lateinit var severityMappings: Map<String, Int>

    override fun onCreate() {
        super.onCreate()
        fh = FileHelper(this)
        notificationManager = NotificationManagerCompat.from(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val serviceNotification = NotificationCompat.Builder(this, CHANNEL_SERVICE_ID)
            .setContentTitle("Alarm Listener")
            .setContentText("Listening on Nuage alarms.")
            .build()

        val ses = Executors.newScheduledThreadPool(1)
        val throwAlarm = {
            val entityList = fh.getEntityList()
            val newAlarm = AlarmBuilder.mockAlarm(entityList)
            sendOnNotificationChannel(newAlarm)
            fh.addToAlarmList(newAlarm)
        }
        ses.scheduleAtFixedRate(throwAlarm, 5, 10, TimeUnit.SECONDS)
        /*Thread(Runnable {

        })*/
        startForeground(1, serviceNotification)
        return START_STICKY
    }

    fun sendOnNotificationChannel(alarm: Alarm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            severityMappings = mapOf(
                "info" to NotificationManager.IMPORTANCE_MIN,
                "warning" to NotificationManager.IMPORTANCE_LOW,
                "minor" to NotificationManager.IMPORTANCE_DEFAULT,
                "major" to NotificationManager.IMPORTANCE_HIGH,
                "critical" to NotificationManager.IMPORTANCE_MAX
            )
        } else {
            severityMappings = mapOf(
                "info" to NotificationCompat.PRIORITY_MIN,
                "warning" to NotificationCompat.PRIORITY_LOW,
                "minor" to NotificationCompat.PRIORITY_DEFAULT,
                "major" to NotificationCompat.PRIORITY_HIGH,
                "critical" to NotificationCompat.PRIORITY_MAX
            )
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_warning)
            .setContentTitle("${alarm.name} : ${alarm.id}")
            .setContentText(alarm.reason)
            .setPriority(severityMappings.getValue(alarm.severity))
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        notificationManager.notify(2, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}