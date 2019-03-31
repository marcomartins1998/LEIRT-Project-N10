package com.example.nuagemobilealarms.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.nuagemobilealarms.R

class FilterRecyclerViewAdapter(context: Context, val filterItemList: ArrayList<Pair<String, String>>) : RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder>(){
    val TAG = "FilterRecViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_filteritem, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")

        val pair = filterItemList[position]
        holder.entityText.text = pair.first
        holder.filterText.text = pair.second
        holder.filterItemLayout.setOnClickListener {
            val p = filterItemList.indexOf(pair)
            filterItemList.removeAt(p)
            this.notifyItemRemoved(p)
        }
    }

    override fun getItemCount(): Int = filterItemList.size

    class ViewHolder(itemView: View,
                     val entityText: TextView = itemView.findViewById(R.id.entityText),
                     val filterText: TextView = itemView.findViewById(R.id.filterText),
                     val filterItemLayout: ConstraintLayout = itemView.findViewById(R.id.filterItemLayout)
    ) : RecyclerView.ViewHolder(itemView)

}