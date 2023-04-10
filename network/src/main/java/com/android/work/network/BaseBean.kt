package com.android.work.network

import com.google.gson.Gson

data class BaseBean<T>(
    val data:T,
    val errorCode:Int?,
    val errorMsg:String?,
    val isSuccessful:Boolean = errorCode == 0
){
    override fun toString(): String {
        return "data: ${Gson().toJson(data)} errorCode:$errorCode  errorMsg:$errorMsg"
    }
}
