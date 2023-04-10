package com.android.work.bluetooth

import adapter.BlueAdapter
import android.widget.Toast
import androidx.databinding.*
import androidx.recyclerview.widget.RecyclerView
import com.android.work.network.BaseViewModel
import data.BluetoothDeviceInfo
import interfaces.BlueScanCallback

class BlueViewModel: BaseViewModel() {
    val dataList by lazy { ObservableArrayList<BluetoothDeviceInfo>() }

    val isEmpty = ObservableBoolean(true)

    private val callback = object:BlueScanCallback{
        override fun scanFailed() {
            context?.let {
                Toast.makeText(it,"扫描失败",0).show()
            }
        }

        override fun scanResult(devices: List<BluetoothDeviceInfo>) {
//            dataList.clear()
//            dataList.addAll(devices)
//            isEmpty.set(devices.isEmpty())
        }

        override fun singleDevice(device: BluetoothDeviceInfo) {
            dataList.add(device)
            isEmpty.set(dataList.isEmpty())
        }
    }

    fun scanDevice() {
        launch({
            BluetoothManagerHelp.mBluetoothManagerHelp.scanDevice(callback)
        })
    }

    fun stopScan() {
        launch({
            BluetoothManagerHelp.mBluetoothManagerHelp.stopScan(callback)
        })
    }
}

@BindingAdapter("setData")
fun setData(recyclerView: RecyclerView,dataList: MutableList<BluetoothDeviceInfo>?){
    (recyclerView.adapter as BlueAdapter).update(dataList?: mutableListOf())
}