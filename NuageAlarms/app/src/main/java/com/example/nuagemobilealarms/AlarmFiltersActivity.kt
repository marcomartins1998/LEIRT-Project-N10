package com.example.nuagemobilealarms

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.example.nuagemobilealarms.adapter.EntityRecyclerViewAdapter
import com.example.nuagemobilealarms.adapter.FilterRecyclerViewAdapter
import com.example.nuagemobilealarms.connect.VolleyHelper
import com.example.nuagemobilealarms.connect.VolleySingleton
import com.example.nuagemobilealarms.dto.Domain

class AlarmFiltersActivity : AppCompatActivity() {
    val TAG = "AlarmFiltersActivity"
    lateinit var vs: VolleySingleton
    lateinit var vh: VolleyHelper

    val filterItemList = ArrayList<Pair<String, String>>()
    var entityItemList: List<Domain> = ArrayList()

    // FRV -> FilterRecyclerView
    lateinit var FRV: RecyclerView
    lateinit var FRVAdapter: FilterRecyclerViewAdapter
    // ERV -> EntityRecyclerView
    lateinit var ERV: RecyclerView
    lateinit var ERVAdapter: EntityRecyclerViewAdapter

    lateinit var addButton: Button
    lateinit var entityDropDown: Spinner
    lateinit var filterText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarmlist)
        Log.d(TAG, "onCreate: started")

        vs = VolleySingleton.getInstance(this.applicationContext)
        vh = VolleyHelper(this, intent, vs)

        addButton = findViewById(R.id.addButton)
        entityDropDown = findViewById(R.id.entityDropDown)
        filterText = findViewById(R.id.filterText)

        addButton.setOnClickListener{
            FRVinsertItem(entityDropDown.selectedItem.toString(), filterText.text.toString())
        }

        initFRV()
        initERV()
    }

    fun FRVinsertItem(entity: String, filter: String){
        if(!filterItemList.contains(Pair(entity,filter)) && filter != ""){
            filterItemList.add(Pair(entity,filter))
            FRVAdapter.notifyItemChanged(filterItemList.size-1)
        }
    }

    /*fun FRVremoveItem(position: Int, entity: String, filter: String){
        entityList.removeAt(position)
        filterList.removeAt(position)
        FRVAdapter.notifyItemRemoved(position)
    }*/

    fun initFRV() {
        Log.d(TAG, "initFilterRecyclerView: init filter_recycler_view")

        //Setup dropdown with entity names
        val arradapter = ArrayAdapter.createFromResource(this, R.array.all_entities, android.R.layout.simple_spinner_item)
        arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        entityDropDown.adapter = arradapter

        FRV = findViewById(R.id.filterRecView)
        FRVAdapter = FilterRecyclerViewAdapter(this, filterItemList)
        FRV.adapter = FRVAdapter
        FRV.layoutManager = LinearLayoutManager(this)
    }

    fun initERV(){
        Log.d(TAG, "initEntityRecyclerView: init entity_recycler_view")

        vh.NuageGetDomains(intent.extras!!.getString("url")!!){
            entityItemList = it
            ERV = findViewById(R.id.entityRecView)
            ERVAdapter = EntityRecyclerViewAdapter(this, entityItemList)
            ERV.adapter = ERVAdapter
            ERV.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onStop() {
        super.onStop()
        vs.requestQueue.cancelAll("cancelAll")
    }

}
