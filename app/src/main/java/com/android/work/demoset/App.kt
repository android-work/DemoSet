package com.android.work.demoset

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.android.work.common.RouteUtil
import com.android.work.demoset.globle_crash.GlobleTryCache
import com.android.work.demoset.hook.HookUtil
import com.android.work.demoset.hot_fix.HotFixUtil
import java.lang.Exception

class App:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context

        fun getAppContext() = mContext

        var v = false
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

        // 设置全局异常捕获
        Thread.setDefaultUncaughtExceptionHandler(GlobleTryCache.getInstance(this))
        // 自定义热更新异常
        try {
            HotFixUtil.findDex(this)
        }catch (e: Exception){
            e.printStackTrace()
        }

        HookUtil.hook()

//        ARouter.init(this)
//        ARouter.openLog();
//        ARouter.openDebug()

        RouteUtil.getInstance().init(this)

//        ProcessorHelper()
    }
}