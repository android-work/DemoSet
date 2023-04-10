package com.android.work.demoset.design

import com.android.work.demoset.data.ShareData
import com.android.work.network.NetworkUtil

object Reprosity {
    private val service = NetworkUtil.create<Api>()

    /**
     * 注册接口测试
     */
    suspend fun registerTest(): MutableList<ShareData> {
//        val baseBean = service.banner("json")
        val baseBean = service.list(1)
        return baseBean.data
    }
}