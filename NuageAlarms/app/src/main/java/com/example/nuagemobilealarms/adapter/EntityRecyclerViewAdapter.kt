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
import com.example.nuagemobilealarms.dto.Domain

class EntityRecyclerViewAdapter(context: Context, val domainlist: List<Domain>) : RecyclerView.Adapter<EntityRecyclerViewAdapter.ViewHolder>(){
    val TAG = "EntityRecViewAdapter"
    val entityTypeDomain = context.resources.getString(R.string.entity_domain)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_entityitem, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")

        holder.entityType.text = entityTypeDomain
        holder.entityId.text = domainlist[position].id
        holder.entityName.text = domainlist[position].name
        holder.entityDescription.text = domainlist[position].description
    }

    override fun getItemCount(): Int = domainlist.size

    class ViewHolder(itemView: View,
                     val entityType: TextView = itemView.findViewById(R.id.entityType),
                     val entityId: TextView = itemView.findViewById(R.id.entityId),
                     val entityName: TextView = itemView.findViewById(R.id.entityName),
                     val entityDescription: TextView = itemView.findViewById(R.id.entityDescription)
    ) : RecyclerView.ViewHolder(itemView)
}