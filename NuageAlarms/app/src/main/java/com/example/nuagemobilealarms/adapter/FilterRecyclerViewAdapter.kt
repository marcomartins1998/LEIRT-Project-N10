package com.example.nuagemobilealarms.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.example.nuagemobilealarms.R
import com.example.nuagemobilealarms.helper.AndroidHelper

class FilterRecyclerViewAdapter(context: Context,
                                addButton: Button,
                                entityDropDown: Spinner,
                                filterText: TextView,
                                val entityTextList: ArrayList<String> = ArrayList(),
                                val filterTextList: ArrayList<String> = ArrayList()
                                ) : RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder>(){
    val TAG = "FilterRecViewAdapter"
    var size = 0
    init {
        addButton.setOnClickListener {
            if(entityDropDown.selectedItem == null || filterText.text == null) AndroidHelper().toastMessage(context, "Insufficient parameters.")
            else {
                entityTextList.add(entityDropDown.selectedItem as String)
                filterTextList.add(filterText.text.toString())
                size++
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_filteritem, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")

        holder.entityText.text = entityTextList[position]
        holder.filterText.text = filterTextList[position]
        //Add click listeners here if needed
    }

    override fun getItemCount(): Int = entityTextList.size

    class ViewHolder(val itemView: View,
                     val entityText: TextView = itemView.findViewById(R.id.entityText),
                     val filterText: TextView = itemView.findViewById(R.id.filterText)
    ) : RecyclerView.ViewHolder(itemView) {


    }

}