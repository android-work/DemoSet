package com.android.work.common

import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.work.apt_processor.IRoute
import com.android.work.common.thread_pool.ThreadPoolFactory
import dalvik.system.DexFile
import java.util.*
import kotlin.collections.HashMap

class RouteUtil private constructor(){

    companion object{
        private var mRouteUtil: RouteUtil? = null

        fun getInstance():RouteUtil{
            if (mRouteUtil == null){
                mRouteUtil = RouteUtil()
            }
            return mRouteUtil!!
        }
    }

    private lateinit var mContext: Context

    private val routeMap = HashMap<String,String>()

    fun init(context:Context){
        mContext = context
        ThreadPoolFactory.getInstance().executeTask(Runnable {
            val path = context.packageManager.getPackageInfo(context.packageName,0).applicationInfo.sourceDir
            val dexFile = DexFile(path)
            val entries: Enumeration<String> = dexFile.entries()
            for (name:String in entries){
                if (name.contains("com.android.work.apt_processor")){
                    try {
                        val clazz = Class.forName(name)
                        val obj = clazz.newInstance() as IRoute
                        routeMap.putAll(obj.putActivity())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            Log.d("TAG","App routeMap:$routeMap")
        })
    }


    fun jumpActivity(route:String){
        if (routeMap.containsKey(route)){
            val clazz = routeMap[route]?: ""
            val intent = Intent(mContext,Class.forName(clazz))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mContext.startActivity(intent)
        }
    }
}