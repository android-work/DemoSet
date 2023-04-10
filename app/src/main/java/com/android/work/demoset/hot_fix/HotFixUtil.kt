package com.android.work.demoset.hot_fix

import android.content.Context
import android.util.Log
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File
import java.lang.Exception

object HotFixUtil {
    private const val TAG = "DemoSet_HotFixUtil"

    fun findDex(context:Context){
        // 获取修复dex目录
        val dexPath = String.format("%s%s%s",context.filesDir, File.separator,"hotFixDex")
        Log.d(TAG,"findDex dexPath:$dexPath")
        val dexPathFile = File(dexPath)
        if (!dexPathFile.exists()){
            dexPathFile.mkdir()
        }

        // 获取程序运行的BaseDexClassLoader class
//        val classLoader = context.classLoader
        // 遍历目录下所有dex
        dexPathFile.listFiles()?.forEach {
            Log.d(TAG,"file:${it.name}")
            if (it.name.endsWith(".dex")){
                // 加载dex
                loadDex(it.absolutePath,context)
            }
        }
    }

    private fun loadDex(file:String,context: Context){
        val classZ = Class.forName("dalvik.system.BaseDexClassLoader")
        Log.d(TAG,"loadDex file:$file  classZ:$classZ")
//        classLoader.loadClass(file)
        // 加载修复dex文件
        val dexClassLoader = DexClassLoader(file,context.cacheDir.absolutePath,null,context.classLoader)
        // 获取修复dex加载类中的pathList对象
        val dexPathList = getPathList(dexClassLoader,classZ)
        // 获取程序运行加载类中的pathList对象
        val classPathList = getPathList(context.classLoader,classZ)
        // 从pathList中获取Elements
        val dexElements = getElementList(dexPathList,dexPathList?.javaClass)
        val classElements = getElementList(classPathList,classPathList?.javaClass)

        // 合并dex
        val newElements = mergeElements(dexElements,classElements) ?: return

        // 更改程序运行的PathList的elements  奶火萝铃球草火   棉白玉树棉
        val classZ1 = Class.forName("dalvik.system.BaseDexClassLoader")
        val pathList = getPathList(context.classLoader,classZ1)
        setField(pathList,"dexElements",newElements)
    }

    private fun mergeElements(dexElements:Any?,classElements:Any?):Any?{
        if (dexElements == null || classElements == null){
            return null
        }
        val componentType = dexElements.javaClass.componentType
        val dexElementsSize = java.lang.reflect.Array.getLength(dexElements)
        val classElementsSize = java.lang.reflect.Array.getLength(classElements)
        val resultElements = java.lang.reflect.Array.newInstance(componentType,dexElementsSize + classElementsSize)
        System.arraycopy(dexElements,0,resultElements,0,dexElementsSize)
        System.arraycopy(classElements,0,resultElements,dexElementsSize,classElementsSize)
        return resultElements
    }

    private fun getPathList(obj:Any?,classZ:Class<*>):Any?{
        return getField(obj,classZ,"pathList")
    }

    private fun getField(obj:Any?,classZ: Class<*>?,fieldName:String): Any? {
        try {
            Log.d(TAG, "getField classZ:$classZ")
            val field = classZ?.getDeclaredField(fieldName)
            Log.d(TAG, "getField field:$field")
            if (field?.isAccessible != true) {
                field?.isAccessible = true
            }
            return field?.get(obj)
        } catch (e: Exception) {
            Log.e(TAG, "getField e:$e")
        }
        return null
    }

    private fun setField(pathList: Any?,fieldName: String,value:Any?){
        if (pathList == null || value == null){
            return
        }
        val classZ = pathList.javaClass
        Log.d(TAG, "setField classZ:$classZ")
        val field = classZ.getDeclaredField(fieldName)
        Log.d(TAG, "setField field:$field")
        if (!field.isAccessible){
            field.isAccessible = true
        }
        field.set(pathList,value)
    }

    private fun getElementList(obj:Any?,classZ:Class<*>?):Any?{
        return getField(obj,classZ,"dexElements")
    }
}