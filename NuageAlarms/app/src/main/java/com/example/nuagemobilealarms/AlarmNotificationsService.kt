package com.example.nuagemobilealarms

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.nuagemobilealarms.dto.EntityDto
import com.example.nuagemobilealarms.helper.FileHelper
import com.example.nuagemobilealarms.model.Alarm
import com.example.nuagemobilealarms.model.Entity
import com.example.nuagemobilealarms.model.Type
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AlarmNotificationsService : FirebaseMessagingService() {
    val fh = FileHelper(this)

    override fun onMessageReceived(rmsg: RemoteMessage?) {
        //super.onMessageReceived(rmsg)
        //TODO changed por testing purposes
        //val entity = fh.getEntityList().filter {it.id == rmsg?.data?.get("parentID")}
        val entity = arrayListOf(EntityDto("jkfbhjvgfhjf", null, "TestEnterprise", Type.ENTERPRISE, true))
        if(entity.isNotEmpty()) {
            val resultIntent = Intent(this, MainActivity::class.java)
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val notificationManager = NotificationManagerCompat.from(this)
            val notification = NotificationCompat.Builder(this, NuageAlarmsApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("${rmsg?.data?.get("severity")} Alarm in ${entity[0].name}")
                .setContentText(rmsg?.data?.get("description"))
                //.setContentTitle(rmsg?.data?.get("title"))
                //.setContentText(rmsg?.data?.get("description"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent)
                .build()
            //val alarmList = fh.getAlarmList()
            //alarmList.add(Alarm.mapToAlarm(rmsg!!.data, entity[0].type.name))
            //fh.putAlarmList(alarmList)
            notificationManager.notify(1, notification)
        }
        /*val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val notificationManager = NotificationManagerCompat.from(this)
        val notification = NotificationCompat.Builder(this, NuageAlarmsApp.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(rmsg?.data?.get("title"))
            .setContentText(rmsg?.data?.get("description"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(resultPendingIntent)
            .build()
        notificationManager.notify(1, notification)*/
    }

}