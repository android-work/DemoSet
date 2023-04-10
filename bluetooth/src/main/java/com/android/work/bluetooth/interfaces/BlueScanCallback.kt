package interfaces

import data.BluetoothDeviceInfo

interface BlueScanCallback {

    /**
     * 扫描失败
     */
    fun scanFailed()

    /**
     * 扫描成功
     */
    fun scanResult(devices : List<BluetoothDeviceInfo>)

    /**
     * 返回单个设备
     */
    fun singleDevice(device:BluetoothDeviceInfo)
}