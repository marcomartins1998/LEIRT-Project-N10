package com.example.nuagemobilealarms

import android.os.Bundle
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import com.example.nuagemobilealarms.adapter.EntityGroupRecViewAdapter
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.helper.VolleyHelper
import com.example.nuagemobilealarms.model.Entity

//import org.apache.activemq.*
//import javax.jms.*


// TODO test with API level above and below 26
class NotificationFiltersActivity : AppCompatActivity() {
    val TAG = "NotificationFiltersAct"
    /*val JMS_USER = "jmsclient"
    val JMS_PASSWORD = "jmsclient12345"
    val JMS_URL = "tcp://124.252.253.219:61616"*/
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper
    lateinit var notificationManager: NotificationManagerCompat

    val enterpriseList: ArrayList<Entity> = arrayListOf()
    val domainList: ArrayList<Entity> = arrayListOf()
    val zoneList: ArrayList<Entity> = arrayListOf()
    val vportList: ArrayList<Entity> = arrayListOf()

    lateinit var enterpriseDropDown: Button
    lateinit var enterpriseRecView: RecyclerView
    lateinit var enterpriseRecAdapter: EntityGroupRecViewAdapter

    lateinit var domainDropDown: Button
    lateinit var domainRecView: RecyclerView
    lateinit var domainRecAdapter: EntityGroupRecViewAdapter

    lateinit var zoneDropDown: Button
    lateinit var zoneRecView: RecyclerView
    lateinit var zoneRecAdapter: EntityGroupRecViewAdapter

    lateinit var vportDropDown: Button
    lateinit var vportRecView: RecyclerView
    lateinit var vportRecAdapter: EntityGroupRecViewAdapter

    lateinit var saveButton: Button
    lateinit var drawerLayout: DrawerLayout
    lateinit var menuButton: ImageButton
    lateinit var navigationView: NavigationView
    lateinit var filtersButton: Button
    lateinit var filtersConstraintLayout: ConstraintLayout
    lateinit var filtersSeveritySpinner: Spinner
    /*lateinit var topicConnection: TopicConnection
    lateinit var topicSession: TopicSession
    lateinit var topicSubscriber: TopicSubscriber*/

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_list)
        Log.d(TAG, "onCreate: started")

        notificationManager = NotificationManagerCompat.from(this)
    }

    fun setupJMS() {

        //topicConnection = ActiveMQConnectionFactory(JMS_USER, JMS_PASSWORD, JMS_URL).createTopicConnection()
        /*topicConnection.clientID = UUID.randomUUID().toString()
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

        notificationManager.notify(1, notification)*/
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: started")
        //vs.requestQueue.cancelAll(TAG)

        /*
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
        */
    }
}