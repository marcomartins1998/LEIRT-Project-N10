package com.example.nuagemobilealarms

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import com.example.nuagemobilealarms.adapter.FilterRecyclerViewAdapter
import com.example.nuagemobilealarms.connect.VolleyHelper
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.model.Enterprise

class AlarmFiltersActivity : AppCompatActivity() {
    val TAG = "AlarmFiltersActivity"
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper

    val filterItemList = ArrayList<Pair<String, String>>()
    var enterpriseList: List<Enterprise> = ArrayList()
    //var domainList: List<Domain> = ArrayList()
    //var zoneList: List<Zone> = ArrayList()
    //var vportList: List<VPort> = ArrayList()

    // FRV -> FilterRecyclerView
    //lateinit var FRV: RecyclerView
    //lateinit var FRVAdapter: FilterRecyclerViewAdapter
    // ERV -> EntityRecyclerView
    //lateinit var ERV: RecyclerView
    //lateinit var ERVAdapter: EntityRecyclerViewAdapter
    lateinit var enterpriseRecView: FilterRecyclerViewAdapter
    lateinit var enterpriseRecAdapter: FilterRecyclerViewAdapter
    //lateinit var domainRecView: FilterRecyclerViewAdapter
    //lateinit var domainRecAdapter: FilterRecyclerViewAdapter
    //lateinit var zoneRecView: FilterRecyclerViewAdapter
    //lateinit var zoneRecAdapter: FilterRecyclerViewAdapter
    //lateinit var vportRecView: FilterRecyclerViewAdapter
    //lateinit var vportRecAdapter: FilterRecyclerViewAdapter

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

        drawerLayout = findViewById(R.id.alarmlistDrawerLayout)
        menuButton = findViewById(R.id.menuButton)
        navigationView = findViewById(R.id.navigationView)
        filtersButton = findViewById(R.id.filterDropDown)
        filtersConstraintLayout = findViewById(R.id.filtersConstraintLayout)

        menuButton.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navigationView.setNavigationItemSelectedListener {
            //it.isChecked = true
            drawerLayout.closeDrawer(navigationView)
            true
        }

        filtersButton.setOnClickListener {
            when (filtersConstraintLayout.visibility) {
                View.VISIBLE -> filtersConstraintLayout.visibility = View.GONE
                View.GONE -> filtersConstraintLayout.visibility = View.VISIBLE
            }
        }

        val arradapter =
            ArrayAdapter.createFromResource(this, R.array.severity_items, android.R.layout.simple_spinner_item)
        arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filtersSeveritySpinner.adapter = arradapter
        /*addButton.setOnClickListener{
            FRVinsertItem(entityDropDown.selectedItem.toString(), filterText.text.toString())
        }*/

        initFRV()
        initERV()
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
        vs.requestQueue.cancelAll("cancelAll")
    }

}
