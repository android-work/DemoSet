package com.android.work.demoset.hook

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation

object HookUtil {
    @SuppressLint("DiscouragedPrivateApi")
    fun hook(activity:Activity){
        try {
//            val clazz = Class.forName("android.app.Activity")
            // TODO mInstrumentation framework层高版本不让反射mInstrumentation字段
            val field = Activity::class.java.getDeclaredField("mInstrumentation")
            field.isAccessible = true
            val mInstrumentation = field.get(activity) as Instrumentation
            val hookInstrumentation = HookInstrumentation(mInstrumentation = mInstrumentation)
            field.set(activity,hookInstrumentation)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}