package com.android.work.demoset.hook

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class HookInstrumentation(private val mInstrumentation: Instrumentation) : Instrumentation() {
    private val TAG = "DemoSet_HookInstrumentation"

    fun execStartActivity(
        who: Context, contextThread: IBinder,
        token: IBinder, target: Activity, intent: Intent, options: Bundle
    ) {

        Log.d(TAG,"HookInstrumentation------>")
        val hookIntent = Intent(context,HookActivity::class.java)
        hookIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val clazz = Instrumentation::class.java
        val method = clazz.getDeclaredMethod(
            "execStartActivity",
            Context::class.java,
            IBinder::class.java,
            IBinder::class.java,
            Activity::class.java,
            Intent::class.java,
            Bundle::class.java
        )
        method.invoke(mInstrumentation,who,contextThread,token,target,hookIntent,options)
    }
}