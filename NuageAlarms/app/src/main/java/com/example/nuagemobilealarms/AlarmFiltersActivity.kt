package com.example.nuagemobilealarms

import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import com.example.nuagemobilealarms.adapter.AlarmRecyclerViewAdapter
import com.example.nuagemobilealarms.adapter.EntityGroupRecViewAdapter
import com.example.nuagemobilealarms.connect.VolleyHelper
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.model.Alarm
import com.example.nuagemobilealarms.model.Entity
import java8.util.concurrent.CompletableFuture

class AlarmFiltersActivity : AppCompatActivity() {
    val TAG = "AlarmFiltersActivity"
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper

    val enterpriseList: ArrayList<Entity> = arrayListOf()
    val domainList: ArrayList<Entity> = arrayListOf()
    val zoneList: ArrayList<Entity> = arrayListOf()
    val vportList: ArrayList<Entity> = arrayListOf()

    val alarmList: ArrayList<Alarm> = arrayListOf()

    // FRV -> FilterRecyclerView
    //lateinit var FRV: RecyclerView
    //lateinit var FRVAdapter: FilterRecyclerViewAdapter
    // ERV -> EntityRecyclerView
    //lateinit var ERV: RecyclerView
    //lateinit var ERVAdapter: EntityRecyclerViewAdapter
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
    lateinit var alarmRecAdapter: AlarmRecyclerViewAdapter
    lateinit var drawerLayout: DrawerLayout
    lateinit var menuButton: ImageButton
    lateinit var navigationView: NavigationView
    lateinit var filtersButton: Button
    lateinit var filtersConstraintLayout: ConstraintLayout
    lateinit var filtersSeveritySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarmlist)
        Log.d(TAG, "onCreate: started")

        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)
        val mainHandler = Handler()

        drawerLayout = findViewById(R.id.alarmlistDrawerLayout)
        menuButton = findViewById(R.id.menuButton)
        navigationView = findViewById(R.id.navigationView)
        filtersButton = findViewById(R.id.filterDropDown)
        filtersConstraintLayout = findViewById(R.id.filtersConstraintLayout)
        enterpriseDropDown = findViewById(R.id.enterprisesDropDown)
        domainDropDown = findViewById(R.id.domainsDropDown)
        zoneDropDown = findViewById(R.id.zonesDropDown)
        vportDropDown = findViewById(R.id.vportsDropDown)
        filtersSeveritySpinner = findViewById(R.id.severitySpinner)
        searchButton = findViewById(R.id.searchButton)

        enterpriseRecView = findViewById(R.id.enterprisesRecyclerView)
        domainRecView = findViewById(R.id.domainsRecyclerView)
        zoneRecView = findViewById(R.id.zonesRecyclerView)
        vportRecView = findViewById(R.id.vportsRecyclerView)
        alarmRecView = findViewById(R.id.alarmRecyclerView)

        navigationView.setNavigationItemSelectedListener {
            //it.isChecked = true
            drawerLayout.closeDrawer(navigationView)
            true
        }

        searchButton.setOnClickListener {
            Thread(Runnable {
                enterpriseList.map { vh.NuageGetAllEnterpriseAlarms(TAG, it.id) }.reduce { cf1, cf2 ->
                    cf1.thenCombine(cf2) { l1, l2 -> l1 + l2 }
                }.thenApply {
                    alarmList.clear()
                    alarmList.addAll(it)
                    alarmRecAdapter.notifyDataSetChanged()
                }
            }).start()
        }

        menuButton.setOnClickListener { drawerLayout.openDrawer(navigationView) }
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

        //initFRV()
        //initERV()
        initEntitiesRecView()
        initAlarmRecView()
    }

    fun initEntitiesRecView() {
        /*enterpriseRecAdapter = FilterRecyclerViewAdapter(this, arrayListOf(enterpriseList, domainList, zoneList, vportList), arrayListOf(enterpriseRecView, domainRecView, zoneRecView, vportRecView), 0, 4)
        enterpriseRecView.adapter = enterpriseRecAdapter
        enterpriseRecView.layoutManager = LinearLayoutManager(this)*/
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
        alarmRecAdapter = AlarmRecyclerViewAdapter(this, alarmList, enterpriseList + domainList + zoneList + vportList)
        alarmRecView.layoutManager = LinearLayoutManager(this.applicationContext)
        alarmRecView.adapter = alarmRecAdapter
    }

    fun setVisibilityOnAction(view: View) {
        when (view.visibility) {
            View.VISIBLE -> view.visibility = View.GONE
            View.GONE -> view.visibility = View.VISIBLE
        }
    }

    fun FRVinsertItem(entity: String, filter: String){
        /*if(!filterItemList.contains(Pair(entity,filter)) && filter != ""){
            filterItemList.add(Pair(entity,filter))
            FRVAdapter.notifyItemChanged(filterItemList.size-1)
        }*/
    }

    /*fun FRVremoveItem(position: Int, entity: String, filter: String){
        entityList.removeAt(position)
        filterList.removeAt(position)
        FRVAdapter.notifyItemRemoved(position)
    }*/

    fun initFRV() {
        /*Log.d(TAG, "initFilterRecyclerView: init filter_recycler_view")

        //Setup dropdown with entity names
        //val arradapter = ArrayAdapter.createFromResource(this, R.array.all_entities, android.R.layout.simple_spinner_item)
        //arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //entityDropDown.adapter = arradapter

        FRV = findViewById(R.id.filterRecView)
        FRVAdapter = FilterRecyclerViewAdapter(this, filterItemList)
        FRV.adapter = FRVAdapter
        FRV.layoutManager = LinearLayoutManager(this)*/
    }

    fun initERV(){
        /*Log.d(TAG, "initEntityRecyclerView: init entity_recycler_view")

        vh.NuageGetDomains(intent.extras!!.getString("url")!!){
            entityItemList = it
            ERV = findViewById(R.id.entityRecView)
            ERVAdapter = EntityRecyclerViewAdapter(this, entityItemList)
            ERV.adapter = ERVAdapter
            ERV.layoutManager = LinearLayoutManager(this)
        }*/
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: started")
        vs.requestQueue.cancelAll(TAG)
    }

}
