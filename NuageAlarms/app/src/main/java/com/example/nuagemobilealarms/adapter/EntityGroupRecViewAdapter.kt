package com.example.nuagemobilealarms.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.example.nuagemobilealarms.R
import com.example.nuagemobilealarms.model.Entity

class EntityGroupRecViewAdapter(
    context: Context,
    val entityItemList: ArrayList<Entity>,
    val childItemList: ArrayList<Entity>? = null,
    val childRecViewAdapter: EntityGroupRecViewAdapter? = null
) : RecyclerView.Adapter<EntityGroupRecViewAdapter.ViewHolder>() {
    private val TAG = "FiltersRecViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_entityswitch, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")

        holder.entitySwitch.text = entityItemList[position].name
        holder.entitySwitch.isChecked = entityItemList[position].checked
        holder.entitySwitch.setOnClickListener {
            entityItemList[position].checked = holder.entitySwitch.isChecked
            if (!entityItemList[position].childList.isNullOrEmpty()) when (holder.entitySwitch.isChecked) {
                true -> propagateAdd(entityItemList[position].childList!!)
                false -> propagateRemove(entityItemList[position].childList!!)
            }
        }
    }

    //TODO maybe change to notifyItemRangeChanged for optimization later on
    fun propagateAdd(ls: List<Entity>) {
        Log.d(TAG + "PA", ls.toString())
        if (ls.isNullOrEmpty()) return
        childItemList?.addAll(ls)
        childRecViewAdapter?.propagateAdd(ls.flatMap {
            it.childList ?: arrayListOf()
        })
        childRecViewAdapter?.notifyDataSetChanged()
    }

    fun propagateRemove(ls: List<Entity>) {
        Log.d(TAG + "PR", ls.toString())
        if (ls.isNullOrEmpty()) return
        childItemList?.removeAll(ls)
        childRecViewAdapter?.propagateRemove(ls.flatMap {
            it.childList ?: arrayListOf()
        })
        childRecViewAdapter?.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = entityItemList.size

    class ViewHolder(
        itemView: View,
        val entitySwitch: Switch = itemView.findViewById(R.id.entitySwitch)
    ) : RecyclerView.ViewHolder(itemView)

}