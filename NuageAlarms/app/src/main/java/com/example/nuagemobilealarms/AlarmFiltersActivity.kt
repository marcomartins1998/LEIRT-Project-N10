package com.example.nuagemobilealarms

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import com.example.nuagemobilealarms.adapter.AlarmRecViewAdapter
import com.example.nuagemobilealarms.adapter.EntityGroupRecViewAdapter
import com.example.nuagemobilealarms.adapter.LabelRecViewAdapter
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.helper.AndroidHelper
import com.example.nuagemobilealarms.helper.VolleyHelper
import com.example.nuagemobilealarms.model.Alarm
import com.example.nuagemobilealarms.model.Entity
import java8.util.concurrent.CompletableFuture

class AlarmFiltersActivity : AppCompatActivity() {
    val TAG = "AlarmFiltersActivity"
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper

    lateinit var warningLabelRecView: RecyclerView
    lateinit var warningLabelRecViewAdapter: LabelRecViewAdapter

    val enterpriseList: ArrayList<Entity> = arrayListOf()
    val domainList: ArrayList<Entity> = arrayListOf()
    val zoneList: ArrayList<Entity> = arrayListOf()
    val vportList: ArrayList<Entity> = arrayListOf()
    val alarmList: ArrayList<Alarm> = arrayListOf()

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

    lateinit var searchButton: Button
    lateinit var alarmRecView: RecyclerView
    lateinit var alarmRecAdapter: AlarmRecViewAdapter
    lateinit var drawerLayout: DrawerLayout
    lateinit var menuButton: ImageButton
    lateinit var navigationView: NavigationView
    lateinit var labelsButton: Button
    lateinit var labelsConstraintLayout: ConstraintLayout
    lateinit var filtersButton: Button
    lateinit var filtersConstraintLayout: ConstraintLayout
    lateinit var filtersSeveritySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_list)
        Log.d(TAG, "onCreate: started")

        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)

        drawerLayout = findViewById(R.id.alarmlistDrawerLayout)
        menuButton = findViewById(R.id.menuButton)
        navigationView = findViewById(R.id.navigationView)
        labelsButton = findViewById(R.id.labelDropDown)
        labelsConstraintLayout = findViewById(R.id.labelsConstraintLayout)
        filtersButton = findViewById(R.id.filterDropDown)
        filtersConstraintLayout = findViewById(R.id.filtersConstraintLayout)
        enterpriseDropDown = findViewById(R.id.enterprisesDropDown)
        domainDropDown = findViewById(R.id.domainsDropDown)
        zoneDropDown = findViewById(R.id.zonesDropDown)
        vportDropDown = findViewById(R.id.vportsDropDown)
        filtersSeveritySpinner = findViewById(R.id.severitySpinner)
        searchButton = findViewById(R.id.searchButton)

        warningLabelRecView = findViewById(R.id.warningLabelRecyclerView)
        enterpriseRecView = findViewById(R.id.enterprisesRecyclerView)
        domainRecView = findViewById(R.id.domainsRecyclerView)
        zoneRecView = findViewById(R.id.zonesRecyclerView)
        vportRecView = findViewById(R.id.vportsRecyclerView)
        alarmRecView = findViewById(R.id.alarmRecyclerView)

        AndroidHelper.setupDrawer(this, intent, navigationView, drawerLayout)
        initWarningLabelsRecView()

        searchButton.setOnClickListener {
            Thread(Runnable {
                enterpriseList.map { vh.NuageGetAllEnterpriseAlarms(TAG, it.id) }.reduce { cf1, cf2 ->
                    cf1.thenCombine(cf2) { l1, l2 -> l1 + l2 }
                }.thenApply {
                    alarmList.clear()
                    alarmList.addAll(it)
                    if (filtersSeveritySpinner.selectedItem != "ALL") {
                        alarmList.removeAll(alarmList.filter { it.severity != filtersSeveritySpinner.selectedItem })
                        val entityList = enterpriseList + domainList + zoneList + vportList
                        alarmList.removeAll(alarmList.filter { alarm -> entityList.any { it.id != alarm.parentid } })
                    }
                    alarmRecAdapter.notifyDataSetChanged()
                }
            }).start()
        }

        menuButton.setOnClickListener { drawerLayout.openDrawer(navigationView) }
        labelsButton.setOnClickListener { setVisibilityOnAction(labelsConstraintLayout) }
        filtersButton.setOnClickListener { setVisibilityOnAction(filtersConstraintLayout) }
        enterpriseDropDown.setOnClickListener { setVisibilityOnAction(enterpriseRecView) }
        domainDropDown.setOnClickListener { setVisibilityOnAction(domainRecView) }
        zoneDropDown.setOnClickListener { setVisibilityOnAction(zoneRecView) }
        vportDropDown.setOnClickListener { setVisibilityOnAction(vportRecView) }

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

        /*enterpriseList.addAll(arrayListOf(
            EnterpriseBuilder.createEnterprise(1, 2),
            EnterpriseBuilder.createEnterprise(2, 2),
            EnterpriseBuilder.createEnterprise(3, 2)
        ))
        domainList.addAll(ArrayList(enterpriseList.flatMap{it.childList!!}))
        zoneList.addAll(ArrayList(domainList.flatMap{it.childList!!}))
        vportList.addAll(ArrayList(zoneList.flatMap{it.childList!!}))*/

        initEntitiesRecView()
        initAlarmRecView()
    }

    fun initWarningLabelsRecView() {
        warningLabelRecViewAdapter = LabelRecViewAdapter(
            this, arrayListOf(
                "CRITICAL" to R.color.CRITICAL,
                "MAJOR" to R.color.MAJOR,
                "MINOR" to R.color.MINOR,
                "WARNING" to R.color.WARNING,
                "INFO" to R.color.INFO
            )
        )
        warningLabelRecView.layoutManager = LinearLayoutManager(this.applicationContext)
        warningLabelRecView.adapter = warningLabelRecViewAdapter
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

    fun initAlarmRecView() {
        alarmRecAdapter = AlarmRecViewAdapter(this, alarmList, enterpriseList + domainList + zoneList + vportList)
        alarmRecView.layoutManager = LinearLayoutManager(this)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(this.getDrawable(R.drawable.rec_view_divider)!!)
        alarmRecView.addItemDecoration(divider)
        alarmRecView.adapter = alarmRecAdapter
        alarmRecAdapter.notifyDataSetChanged()
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
