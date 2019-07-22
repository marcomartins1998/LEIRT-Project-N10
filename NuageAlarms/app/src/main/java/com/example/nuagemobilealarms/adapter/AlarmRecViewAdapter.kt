package com.example.nuagemobilealarms.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.nuagemobilealarms.R
import com.example.nuagemobilealarms.helper.VolleyHelper
import com.example.nuagemobilealarms.model.Alarm
import com.example.nuagemobilealarms.model.Entity
import java.util.*

class AlarmRecViewAdapter(
    val context: Context,
    val vh: VolleyHelper,
    val tag: String,
    val alarmlist: List<Alarm>,
    val entitylist: List<Entity>
) :
    RecyclerView.Adapter<AlarmRecViewAdapter.ViewHolder>() {
    val TAG = "$tag:AlarmRecViewAdapter"

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
        val calendar = Calendar.getInstance()
        calendar.time = alarmlist[position].startDate
        var am_pm: String = "PM"
        if (calendar.get(Calendar.AM_PM) == 0) am_pm = "AM"
        val sufix =
            "on ${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.YEAR)} at ${calendar.get(
                Calendar.HOUR
            )}:${calendar.get(Calendar.MINUTE)} $am_pm"
        if (ls.isEmpty()) holder.originTextView.text =
            "${alarmlist[position].parenttype} $sufix"
        else holder.originTextView.text = "${ls[0].name} $sufix"
        holder.reasonTextView.text = alarmlist[position].reason
        if (alarmlist[position].acknowledged) holder.acknowledgedImageButton.setImageResource(R.drawable.ic_true)
        else {
            holder.acknowledgedImageButton.setOnClickListener {
                vh.NuageAcknowledgeAlarm(TAG, alarmlist[position].id).thenAccept {
                    holder.acknowledgedImageButton.setImageResource(R.drawable.ic_true)
                    holder.acknowledgedImageButton.setOnClickListener {}
                }
            }
        }
    }

    override fun getItemCount(): Int = alarmlist.size

    class ViewHolder(
        itemView: View,
        val warningImage: ImageView = itemView.findViewById(R.id.warningImage),
        val originTextView: TextView = itemView.findViewById(R.id.originTextView),
        val reasonTextView: TextView = itemView.findViewById(R.id.reasonTextView),
        val acknowledgedImageButton: ImageButton = itemView.findViewById(R.id.acknowledgedImageButton)
    ) : RecyclerView.ViewHolder(itemView)

}