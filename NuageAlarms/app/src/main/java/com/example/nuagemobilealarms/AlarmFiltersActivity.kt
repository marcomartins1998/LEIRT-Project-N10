package com.example.nuagemobilealarms

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.nuagemobilealarms.adapter.FilterRecyclerViewAdapter

class AlarmFiltersActivity : AppCompatActivity() {
    val TAG = "AlarmFiltersActivity"
    //vars
    val entityList = ArrayList<String>()
    val filterList = ArrayList<String>()

    lateinit var addButton: Button
    lateinit var entityDropDown: Spinner
    lateinit var filterText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarmlist)
        Log.d(TAG, "onCreate: started")

        addButton = findViewById(R.id.addButton)
        entityDropDown = findViewById(R.id.entityDropDown)
        filterText = findViewById(R.id.filterText)

        val arradapter = ArrayAdapter.createFromResource(this, R.array.all_entities, android.R.layout.simple_spinner_item)
        arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        entityDropDown.adapter = arradapter

        initFilterRecyclerView()
    }

    fun initFilterRecyclerView() {
        Log.d(TAG, "initFiltersRecyclerView: init filters_recycler_view")
        val frv = findViewById<RecyclerView>(R.id.filterRecView)
        val adapter = FilterRecyclerViewAdapter(this, addButton, entityDropDown, filterText)
        frv.adapter = adapter
        frv.layoutManager = LinearLayoutManager(this)
    }

}
