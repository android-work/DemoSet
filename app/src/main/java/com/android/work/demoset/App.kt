package com.android.work.demoset

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.android.work.common.RouteUtil
import com.android.work.demoset.algorithm.algorithm
import com.android.work.demoset.dynamic_change_skin.ChangeSkinUtil
import com.android.work.demoset.globle_crash.GlobleTryCache
import com.android.work.demoset.hook.HookUtil
import com.android.work.demoset.hot_fix.HotFixUtil
import com.android.work.demoset.plugin.PluginUtil
import java.lang.Exception
import java.util.concurrent.atomic.AtomicReference

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
            PluginUtil.loadApk(this)

            HookUtil.hookStartActivity(this)

            HookUtil.hookActivityThread_getActivityClient(this)
            val intent = Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            HookUtil.hook()
        }catch (e: Exception){
            e.printStackTrace()
        }

//        ARouter.init(this)
//        ARouter.openLog();
//        ARouter.openDebug()

        RouteUtil.getInstance().init(this)

        ChangeSkinUtil.initResource(this)

        // 算法测试
        algorithm()

//        ProcessorHelper()
    }
}