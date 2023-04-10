package com.android.work.demoset.hook

import android.view.View


class ProxyArrayList<T>: ArrayList<T>() {

    private var mArrayListAddListener:ArrayListAddListener<T>? = null

    override fun add(element: T): Boolean {
        val isAdd = super.add(element)
        mArrayListAddListener?.addListener(element)
        return isAdd
    }

    fun setArrayListAddListener(arrayListAddListener: ArrayListAddListener<T>){
        mArrayListAddListener = arrayListAddListener
    }
}

interface ArrayListAddListener<T> {
    fun addListener(view:T)
}