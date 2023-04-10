package adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.work.bluetooth.R
import data.BluetoothDeviceInfo

class BlueAdapter(private var dataList:MutableList<BluetoothDeviceInfo>): RecyclerView.Adapter<BlueAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlueAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_blue_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BlueAdapter.ViewHolder, position: Int) {

        holder.textView.text = "address:${dataList[position].bluetoothDevice?.address} name:${dataList[position].bluetoothDevice?.name}"
    }

    override fun getItemCount() = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataList: MutableList<BluetoothDeviceInfo>){
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val textView = itemView.findViewById<TextView>(R.id.textView)
    }
}