package com.android.work.demoset

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class App:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        fun getAppContext() = mContext
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}