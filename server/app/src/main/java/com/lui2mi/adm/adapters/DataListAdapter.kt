package com.lui2mi.adm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lui2mi.adm.R

class DataListAdapter(val context: Context, val data: MutableMap<String,String>): RecyclerView.Adapter<DataListAdapter.DataListHolder>() {
    class DataListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val description: TextView = itemView.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_datalist, null, false)
        return DataListHolder(view)
    }

    override fun onBindViewHolder(holder: DataListHolder, position: Int) {
        val key = data.keys.elementAt(position)
        holder.title.setText(key)
        holder.description.setText(data.get(key))
    }

    override fun getItemCount(): Int = data.size
}