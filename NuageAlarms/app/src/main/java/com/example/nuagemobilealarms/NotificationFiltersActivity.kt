package com.example.nuagemobilealarms

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.apache.activemq.ActiveMQConnectionFactory
import java.util.*
import javax.jms.MessageListener
import javax.jms.TopicConnection
import javax.jms.TopicSession
import javax.jms.TopicSubscriber


// TODO test with API level above and below 26
class NotificationFiltersActivity : AppCompatActivity() {
    val TAG = "NotificationFiltersAct"
    val JMS_USER = "jmsclient"
    val JMS_PASSWORD = "jmsclient12345"
    val JMS_URL = "tcp://124.252.253.219:61616"

    lateinit var notificationManager: NotificationManagerCompat
    lateinit var topicConnection: TopicConnection
    lateinit var topicSession: TopicSession
    lateinit var topicSubscriber: TopicSubscriber

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarmlist)
        Log.d(TAG, "onCreate: started")

        notificationManager = NotificationManagerCompat.from(this)
    }

    fun setupJMS() {

        topicConnection = ActiveMQConnectionFactory(JMS_USER, JMS_PASSWORD, JMS_URL).createTopicConnection()
        topicConnection.clientID = UUID.randomUUID().toString()
        topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE)
        topicConnection.start()
        topicSubscriber = topicSession.createSubscriber(topicSession.createTopic("jms/topic/CNAAlarms"))
        topicSubscriber.messageListener = MessageListener {

        }

        val notification = NotificationCompat
            .Builder(this, NuageAlarmsApp.CHANNEL_1_ID)
            //.setSmallIcon()
            .setContentTitle("Notification Test Title")
            .setContentText("Notification Test Text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: started")
        //vs.requestQueue.cancelAll(TAG)

        // unsubscribe to topic:
        if (topicSubscriber != null)
            topicSubscriber.close()
        // for durable clients this additional step must be called.
        //if (topicSession != null)
        //    topicSession.unsubscribe(clientName);
        if (topicConnection != null)
            topicConnection.stop()
        if (topicSession != null)
            topicSession.close()
        if (topicConnection != null)
            topicConnection.close()
        //if (jndiCtx != null)
        //    jndiCtx.close();
    }
}