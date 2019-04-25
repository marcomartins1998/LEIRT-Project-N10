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

class LabelRecViewAdapter(val context: Context, val warningTypeList: List<Pair<String, Int>>) :
    RecyclerView.Adapter<LabelRecViewAdapter.ViewHolder>() {
    val TAG = "LabelRecViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelRecViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_warning_label, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")

        holder.warningImage.setColorFilter(ContextCompat.getColor(context, warningTypeList[position].second))
        holder.leftArrowImage.setColorFilter(ContextCompat.getColor(context, warningTypeList[position].second))
        holder.warningText.text = warningTypeList[position].first
        holder.warningText.setTextColor(ContextCompat.getColor(context, warningTypeList[position].second))
    }

    override fun getItemCount(): Int = warningTypeList.size

    class ViewHolder(
        itemView: View,
        val warningImage: ImageView = itemView.findViewById(R.id.warningImage),
        val leftArrowImage: ImageView = itemView.findViewById(R.id.leftArrowImage),
        val warningText: TextView = itemView.findViewById(R.id.warningText)
    ) : RecyclerView.ViewHolder(itemView)
}