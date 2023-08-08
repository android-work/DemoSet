package com.android.work.mvvm.data

data class BaseBean<T>(
    val errorCode: Int?,
    val errorMsg: String?,
    val data: T?,
)