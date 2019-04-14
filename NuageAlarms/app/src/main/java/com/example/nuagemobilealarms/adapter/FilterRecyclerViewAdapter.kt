package com.example.nuagemobilealarms.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.example.nuagemobilealarms.R
import com.example.nuagemobilealarms.model.Entity

class FilterRecyclerViewAdapter(
    context: Context,
    val entityItemList: ArrayList<ArrayList<Entity>>,
    val recyclerViewList: ArrayList<RecyclerView>,
    val count: Int,
    val limit: Int
) : RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder>() {
    private val TAG = "FiltersRecViewAdapter"
    private val countaux = count + 1
    private lateinit var childRecViewAdapter: FilterRecyclerViewAdapter

    init {
        if (count < limit - 1) {
            childRecViewAdapter = FilterRecyclerViewAdapter(context, entityItemList, recyclerViewList, countaux, limit)
            recyclerViewList[countaux].adapter = childRecViewAdapter
            recyclerViewList[countaux].layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_entityswitch, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")

        holder.entitySwitch.text = entityItemList[count][position].name
        holder.entitySwitch.isChecked = entityItemList[count][position].checked
        holder.entitySwitch.setOnClickListener {
            entityItemList[count][position].checked = holder.entitySwitch.isChecked
            when (holder.entitySwitch.isChecked) {
                true -> propagateAdd(entityItemList[count][position].childList)
                false -> propagateRemove(entityItemList[count][position].childList)
            }
            /*if (count < limit - 1) {
                if (holder.entitySwitch.isChecked) {
                    entityItemList[count][position].propagateChecked()
                    entityItemList[countaux].addAll(entityItemList[count][position].childList!!)
                } else entityItemList[countaux].removeAll(entityItemList[count][position].childList!!)

                childRecViewAdapter.notifyDataSetChanged()
            }*/

        }
    }

    //TODO maybe change to notifyItemRangeChanged for optimization later on
    fun propagateAdd(ls: List<Entity>?) {
        if (count < limit - 1) {
            entityItemList[countaux].addAll(ls!!)
            childRecViewAdapter.propagateAdd(ls.flatMap { it.childList!! })
            childRecViewAdapter.notifyDataSetChanged()
        }
    }

    fun propagateRemove(ls: List<Entity>?) {
        if (count < limit - 1) {
            entityItemList[countaux].removeAll(ls!!)
            childRecViewAdapter.propagateRemove(ls.flatMap { it.childList!! })
            childRecViewAdapter.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = entityItemList[count].size

    class ViewHolder(itemView: View,
                     val entitySwitch: Switch = itemView.findViewById(R.id.entitySwitch)
    ) : RecyclerView.ViewHolder(itemView)

}