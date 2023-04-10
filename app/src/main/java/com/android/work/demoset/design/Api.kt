package com.android.work.demoset.design

import com.android.work.demoset.data.ShareData
import com.android.work.network.BaseBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("/banner/{json}")
    suspend fun banner(@Path("json") json:String):BaseBean<MutableList<ShareData>>

    @GET("/article/list/0/json")
    suspend fun list(@Query("cid")cid:Int):BaseBean<MutableList<ShareData>>

}