package com.example.nuagemobilealarms.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.nuagemobilealarms.R
import com.example.nuagemobilealarms.model.Alarm
import com.example.nuagemobilealarms.model.Entity

class AlarmRecViewAdapter(val context: Context, val alarmlist: List<Alarm>, val entitylist: List<Entity>) :
    RecyclerView.Adapter<AlarmRecViewAdapter.ViewHolder>() {
    val TAG = "AlarmRecViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_alarm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")

        when (alarmlist[position].severity) {
            "CRITICAL" -> holder.warningImage.setColorFilter(ContextCompat.getColor(context, R.color.CRITICAL))
            "MAJOR" -> holder.warningImage.setColorFilter(ContextCompat.getColor(context, R.color.MAJOR))
            "MINOR" -> holder.warningImage.setColorFilter(ContextCompat.getColor(context, R.color.MINOR))
            "WARNING" -> holder.warningImage.setColorFilter(ContextCompat.getColor(context, R.color.WARNING))
            "INFO" -> holder.warningImage.setColorFilter(ContextCompat.getColor(context, R.color.INFO))
        }

        val ls = entitylist.filter { it.id == alarmlist[position].parentid }
        if (ls.isEmpty()) holder.originTextView.text = alarmlist[position].parenttype
        else holder.originTextView.text = ls[0].name
        holder.reasonTextView.text = alarmlist[position].reason
    }

    override fun getItemCount(): Int = alarmlist.size

    class ViewHolder(
        itemView: View,
        val warningImage: ImageView = itemView.findViewById(R.id.warningImage),
        val originTextView: TextView = itemView.findViewById(R.id.originTextView),
        val reasonTextView: TextView = itemView.findViewById(R.id.reasonTextView)
    ) : RecyclerView.ViewHolder(itemView)

}