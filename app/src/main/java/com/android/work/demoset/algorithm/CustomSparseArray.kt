package com.android.work.demoset.algorithm

import android.util.SparseArray

class CustomSparseArray<T>: SparseArray<T>() {

    private val keys:ArrayList<Int> = arrayListOf()
    fun containers(key:Int):Boolean = keys.contains(key)

    override fun put(key: Int, value: T) {
        super.put(key, value)
        if (!keys.contains(key)){
            keys.add(key)
        }
    }
}