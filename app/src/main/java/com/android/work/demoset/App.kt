package com.android.work.demoset

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.android.work.demoset.globle_crash.GlobleTryCache
import com.android.work.demoset.hot_fix.HotFixUtil
import java.lang.Exception

class App:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        fun getAppContext() = mContext
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        Thread.setDefaultUncaughtExceptionHandler(GlobleTryCache.getInstance(this))

        try {
            HotFixUtil.findDex(this)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}