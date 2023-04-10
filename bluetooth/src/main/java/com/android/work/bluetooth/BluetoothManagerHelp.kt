package com.android.work.bluetooth

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import data.BluetoothDeviceInfo
import interfaces.BlueScanCallback
import java.util.*

class BluetoothManagerHelp private constructor() {
    private val bluetoothManager by lazy {
        BlueProvider.mContext!!.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
    private val bluetoothAdapter by lazy { bluetoothManager.adapter }

    private var blueScanCallback: BlueScanCallback? = null

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.d("TAG","address:${result?.device?.address}  name:${result?.device?.name}   alias:${result?.device?.alias}")
            result?.device?.uuids?.forEach {
                Log.d("TAG","uuid:${it.uuid.node()}")
            }
            blueScanCallback?.singleDevice(BluetoothDeviceInfo(result?.device))
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            blueScanCallback?.scanResult(results?.map { BluetoothDeviceInfo(it.device) }
                ?.toList() ?: mutableListOf())
        }

        override fun onScanFailed(errorCode: Int) {
            blueScanCallback?.scanFailed()
        }
    }

    companion object {
        val mBluetoothManagerHelp by lazy { BluetoothManagerHelp() }
    }

    /**
     * 蓝牙扫描
     */
    fun scanDevice(blueScanCallback: BlueScanCallback, params: String? = null) {
        bluetoothAdapter.bluetoothLeScanner?.apply {
            if (ActivityCompat.checkSelfPermission(
                    BlueProvider.mContext!!,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                this@BluetoothManagerHelp.blueScanCallback = blueScanCallback
                startScan(scanCallback)
            }else{
                Toast.makeText(BlueProvider.mContext!!,"扫描权限不足",0).show()
            }

        }
    }

    /**
     * 连接设备
     */
    fun connect(bluetoothDevice: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                BlueProvider.mContext!!,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothDevice.connectGatt(BlueProvider.mContext!!, false,
                object : BluetoothGattCallback() {
                    override fun onConnectionStateChange(
                        gatt: BluetoothGatt?,
                        status: Int,
                        newState: Int
                    ) {
                        if(status == BluetoothGatt.GATT_SUCCESS){
                            if (newState == BluetoothProfile.STATE_CONNECTED){
                                gatt?.connect()
                            }else{
                                Toast.makeText(BlueProvider.mContext!!,"连接断开",0).show()
                            }
                        }else{
                            Toast.makeText(BlueProvider.mContext!!,"连接失败",0).show()
                        }
                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onCharacteristicRead(
                        gatt: BluetoothGatt?,
                        characteristic: BluetoothGattCharacteristic?,
                        status: Int
                    ) {
                        if (status == BluetoothGatt.GATT_SUCCESS){
                            val content = String(Base64.getDecoder().decode(characteristic?.value?: byteArrayOf()))
                            Log.d("TAG","content:$content")
                            Toast.makeText(BlueProvider.mContext!!,content,0).show()
                        }
                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onCharacteristicWrite(
                        gatt: BluetoothGatt?,
                        characteristic: BluetoothGattCharacteristic?,
                        status: Int
                    ) {
                        if (status == BluetoothGatt.GATT_SUCCESS){
                            val content = String(Base64.getEncoder().encode(characteristic?.value?: byteArrayOf()))
                            Log.d("TAG","content:$content")
                        }
                    }

                    override fun onCharacteristicChanged(
                        gatt: BluetoothGatt?,
                        characteristic: BluetoothGattCharacteristic?
                    ) {
                        Toast.makeText(BlueProvider.mContext!!,"连接断开",0).show()
                    }
                }
            )
        }else{
            Toast.makeText(BlueProvider.mContext!!,"连接权限不足",0).show()
        }
    }

    /**
     * 停止扫描
     */
    fun stopScan(blueScanCallback: BlueScanCallback){
        this@BluetoothManagerHelp.blueScanCallback = blueScanCallback
        if (ActivityCompat.checkSelfPermission(
                BlueProvider.mContext!!,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
        }else{
            Toast.makeText(BlueProvider.mContext!!,"扫描权限不足",0).show()
        }
    }

}