package com.android.work.mvvm.reporsity

import com.android.work.mvvm.data.BaseBean

open class BaseReporsity<T> {
    fun seriData(baseBean: BaseBean<T>?):T?{
        return baseBean?.data
    }

    fun seriListData(baseBean: BaseBean<MutableList<T>>?):MutableList<T>?{
        return baseBean?.data
    }
}
