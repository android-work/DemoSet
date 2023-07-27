package com.android.work.demoset.dynamic_change_skin

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import dalvik.system.DexClassLoader

object ChangeSkinUtil {
    private var drawableClazz: Class<*>? = null
    private var pluginResource: Resources? = null
    private var packageName:String? = null
    private var resource:Resources? = null

    fun initResource(context:Context){
        val dexPath = "${context.cacheDir}/app-debug.apk"
        /*val dexClassLoader = DexClassLoader(dexPath,context.cacheDir.absolutePath,null,context.classLoader)
        drawableClazz = dexClassLoader.loadClass("com.android.work.demoset.R\$drawable")*/

        resource = context.resources
        val packageArchiveInfo = context.packageManager.getPackageArchiveInfo(dexPath,PackageManager.GET_ACTIVITIES)
        packageName = packageArchiveInfo?.packageName

        // 获取resource
        val assetManager = AssetManager::class.java.newInstance()
        val method = AssetManager::class.java.getDeclaredMethod("addAssetPath",String::class.java)
        method.invoke(assetManager,dexPath)
        pluginResource = Resources(assetManager,resource!!.displayMetrics,resource!!.configuration)
    }

    fun getResourceId(name:String,type:String):Int{
        /*val entryName = resource?.getResourceEntryName(resourceId)
        val resourceName = resource?.getResourceName(resourceId)
        val typeName = resource?.getResourceTypeName(resourceId)*/
//        Log.d("TAG","entryName:$entryName   resourceName:$resourceName   typeName:$typeName   resourceId:$resourceId")
        val id = pluginResource?.getIdentifier(name,type, packageName)?:0
        Log.d("TAG","id:$id")
        return id
    }

    fun getDrawableSource(id:Int):Drawable{
        return pluginResource?.getDrawable(id,pluginResource?.newTheme())?: ColorDrawable()
    }
}