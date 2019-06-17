package com.example.nuagemobilealarms

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.example.nuagemobilealarms.adapter.EntityGroupRecViewAdapter
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.dto.EntityDto
import com.example.nuagemobilealarms.helper.AndroidHelper
import com.example.nuagemobilealarms.helper.FileHelper
import com.example.nuagemobilealarms.helper.VolleyHelper
import com.example.nuagemobilealarms.model.Entity
import com.google.firebase.messaging.FirebaseMessaging
import java8.util.concurrent.CompletableFuture


// TODO test with API level above and below 26
class NotificationFiltersActivity : AppCompatActivity() {
    val TAG = "NotificationFiltersAct"

    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper
    lateinit var fh: FileHelper
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
    lateinit var activateNotifications: Switch

    lateinit var helpButton: Button
    lateinit var helpConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_notifications)
        Log.d(TAG, "onCreate: started")

        notificationManager = NotificationManagerCompat.from(this)
        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)
        fh = FileHelper(this)


        drawerLayout = findViewById(R.id.alarmlistDrawerLayout)
        menuButton = findViewById(R.id.menuButton)
        navigationView = findViewById(R.id.navigationView)
        filtersButton = findViewById(R.id.filterDropDown)
        filtersConstraintLayout = findViewById(R.id.filtersConstraintLayout)
        helpButton = findViewById(R.id.helpDropDown)
        helpConstraintLayout = findViewById(R.id.helpConstraintLayout)
        enterpriseDropDown = findViewById(R.id.enterprisesDropDown)
        domainDropDown = findViewById(R.id.domainsDropDown)
        zoneDropDown = findViewById(R.id.zonesDropDown)
        vportDropDown = findViewById(R.id.vportsDropDown)
        filtersSeveritySpinner = findViewById(R.id.severitySpinner)
        saveButton = findViewById(R.id.saveButton)
        activateNotifications = findViewById(R.id.activateNotifications)

        enterpriseRecView = findViewById(R.id.enterprisesRecyclerView)
        domainRecView = findViewById(R.id.domainsRecyclerView)
        zoneRecView = findViewById(R.id.zonesRecyclerView)
        vportRecView = findViewById(R.id.vportsRecyclerView)

        AndroidHelper.setupDrawer(this, intent, navigationView, drawerLayout)

        menuButton.setOnClickListener { drawerLayout.openDrawer(navigationView) }
        filtersButton.setOnClickListener { setVisibilityOnAction(filtersConstraintLayout) }
        helpButton.setOnClickListener { setVisibilityOnAction(helpConstraintLayout) }
        enterpriseDropDown.setOnClickListener { setVisibilityOnAction(enterpriseRecView) }
        domainDropDown.setOnClickListener { setVisibilityOnAction(domainRecView) }
        zoneDropDown.setOnClickListener { setVisibilityOnAction(zoneRecView) }
        vportDropDown.setOnClickListener { setVisibilityOnAction(vportRecView) }
        saveButton.setOnClickListener {
            val arrList = ArrayList<EntityDto>()
            arrList.addAll((enterpriseList + domainList + zoneList + vportList).map { it.toEntityDto() })
            fh.putEntityList(arrList)
        }

        activateNotifications.setOnClickListener {
            if (activateNotifications.isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("Alarms-${intent?.extras?.getString("ip")}")
            } else FirebaseMessaging.getInstance().unsubscribeFromTopic("Alarms-${intent?.extras?.getString("ip")}")
        }

        val arradapter =
            ArrayAdapter.createFromResource(this, R.array.severity_items, android.R.layout.simple_spinner_item)
        arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filtersSeveritySpinner.adapter = arradapter

        Thread(Runnable {
            val cpe = vh.NuageGetEnterprises(TAG).thenApply { ArrayList(it) }
            val cpd = vh.NuageGetDomains(TAG).thenApply { ArrayList(it) }
            val cpz = vh.NuageGetZones(TAG).thenApply { ArrayList(it) }
            val cpv = cpd.thenCompose {
                it.map {
                    vh.NuageGetVPorts(TAG, it.id)
                }.reduce { a, b -> a.thenCombine(b) { l1, l2 -> l1 + l2 } }
            }.thenApply {
                ArrayList(it)
            }
            CompletableFuture.allOf(cpe, cpd, cpz, cpv).thenApply {
                cpz.join().forEach { it.storeVPorts(cpv.join()) }
                cpd.join().forEach { it.storeZones(cpz.join()) }
                cpe.join().forEach { it.storeDomains(cpd.join()) }
                vportList.addAll(cpv.join())
                zoneList.addAll(cpz.join())
                domainList.addAll(cpd.join())
                enterpriseList.addAll(cpe.join())
                vportRecAdapter.notifyDataSetChanged()
                zoneRecAdapter.notifyDataSetChanged()
                domainRecAdapter.notifyDataSetChanged()
                enterpriseRecAdapter.notifyDataSetChanged()
            }
        }).start()

        initEntitiesRecView()

        Thread(Runnable {
            //setupNotifications()
        }).start()
    }



    fun initEntitiesRecView() {
        vportRecAdapter = EntityGroupRecViewAdapter(this, vportList)
        vportRecView.layoutManager = LinearLayoutManager(this.applicationContext)
        vportRecView.adapter = vportRecAdapter

        zoneRecAdapter = EntityGroupRecViewAdapter(this, zoneList, vportList, vportRecAdapter)
        zoneRecView.layoutManager = LinearLayoutManager(this.applicationContext)
        zoneRecView.adapter = zoneRecAdapter

        domainRecAdapter = EntityGroupRecViewAdapter(this, domainList, zoneList, zoneRecAdapter)
        domainRecView.layoutManager = LinearLayoutManager(this.applicationContext)
        domainRecView.adapter = domainRecAdapter

        enterpriseRecAdapter = EntityGroupRecViewAdapter(this, enterpriseList, domainList, domainRecAdapter)
        enterpriseRecView.layoutManager = LinearLayoutManager(this.applicationContext)
        enterpriseRecView.adapter = enterpriseRecAdapter
    }

    fun setVisibilityOnAction(view: View) {
        when (view.visibility) {
            View.VISIBLE -> view.visibility = View.GONE
            View.GONE -> view.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: started")
        vs.requestQueue.cancelAll(TAG)
    }
}