package com.android.work.demoset.globle_crash

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * 异常可以抓的住：
 *  主线程中：出现异常后，会造成主线程anr
 *  子线程中：出现异常可以抓住，不会崩溃
 */
class GlobleTryCache private constructor(private val context: Context): Thread.UncaughtExceptionHandler {
    private val TAG = "DemoSet_GlobleTryCache"

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var mGlobleTryCache: GlobleTryCache? = null

        fun getInstance(context: Context):GlobleTryCache{
            if (mGlobleTryCache == null){
                synchronized(this){
                    if (mGlobleTryCache == null){
                        mGlobleTryCache = GlobleTryCache(context)
                    }
                }
            }
            return mGlobleTryCache!!
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!t.name.contains("main")){
            Toast.makeText(context,"子线程异常捕获成功",0).show()
        }else{
            Toast.makeText(context,"主线程异常捕获到，即将ANR",0).show()
        }
        Log.d(TAG,"程序crash thread:${t.name}  throwable:${e.printStackTrace()}")
    }
}