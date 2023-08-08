package com.android.work.autorob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.work.autorob.viewmodel.AppInfo

class AppListAdapter(val list:MutableList<AppInfo>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var callback:(AppInfo) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_app,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ViewHolder).apply {
            item.logo?.let { logo.setImageDrawable(it) }
            item.name?.let { name.text = it }
            packName.text = item.packName?:""

            this.itemView.setOnClickListener {
                callback.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun setOnClick(callback:(AppInfo) -> Unit){
        this.callback = callback
    }

    fun updateData(list: MutableList<AppInfo>?){
        list?.let {
            this.list.clear()
            this.list.addAll(it)
            notifyDataSetChanged()
        }
    }


    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        val name: TextView = item.findViewById(R.id.name)
        val logo: ImageView = item.findViewById(R.id.logo)
        val packName: TextView = item.findViewById(R.id.packName)
    }

}