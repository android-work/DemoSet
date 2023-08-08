package com.android.work.mvvm.api

import com.android.work.mvvm.data.BaseBean
import com.android.work.mvvm.data.DataInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface APi {

    @GET("/article/list/{index}/json")
    suspend fun getData(@Path(value = "index") index:Int = 0):BaseBean<DataInfo>?
}