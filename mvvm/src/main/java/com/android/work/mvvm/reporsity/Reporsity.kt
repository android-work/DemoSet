package com.android.work.mvvm.reporsity

import android.util.Log
import com.android.work.mvvm.api.APi
import com.android.work.mvvm.data.DataInfo
import com.android.work.network.NetManager

object Reporsity : BaseReporsity<DataInfo>() {

    suspend fun request():DataInfo?{
        val service = NetManager.create<APi>()
        val data = seriData(service.getData())

        return data
    }
}