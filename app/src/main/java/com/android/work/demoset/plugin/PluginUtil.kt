package com.android.work.demoset.plugin

import android.content.Context
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Field

object PluginUtil {

    /**
     * 动态加载apk
     */

    fun loadApk(context: Context){
        val apkPath = String.format("%s%s%s",context.filesDir, File.separator,"pluginapp-debug.apk")
        try {
            // 先获取父类字节码对象
            val loaderClass = Class.forName("dalvik.system.BaseDexClassLoader")
            // 拿到父类加载器中的pathList字段
            val pathListField = getField("pathList",loaderClass)
            val pathList = getValue(pathListField,context.classLoader)
            // 获取pathList中的dexElements数组（是保存所有dex文件的）
            val dexElementsField = getField("dexElements",pathList?.javaClass)
            val dexElements = (getValue(dexElementsField, pathList)?: arrayOf<Any>()) as Array<*>


            // 创建插件类加载器，加载外部apk
            val pluginClassLoader = DexClassLoader(apkPath,null,null,null)
            // 拿到插件类加载器中的pathList字段
            val pluginPathListField = getField("pathList",loaderClass)
            val pluginPathList = getValue(pluginPathListField,pluginClassLoader)
            // 获取插件中pathList中的dexElements数组（是保存所有dex文件的）
            val pluginDexElementsField = getField("dexElements",pluginPathList?.javaClass)
            val pluginDexElements = (getValue(pluginDexElementsField,pluginPathList)?: arrayOf<Any>()) as Array<*>

            // 创建新数组，用于合并dex
            val newDexArray = arrayOfNulls<Any>(dexElements.size + pluginDexElements.size)
            System.arraycopy(dexElements,0,newDexArray,0,dexElements.size)
            System.arraycopy(pluginDexElements,0,newDexArray,dexElements.size,pluginDexElements.size)

            // 替换dex数组
            dexElementsField?.set(pathList,newDexArray)

        }catch (classNotFind:ClassNotFoundException){
            classNotFind.printStackTrace()
        }catch (fieldNotFind:NoSuchFieldException){
            fieldNotFind.printStackTrace()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun getField (fieldName:String,clazz:Class<*>?):Field?{
        val field = clazz?.getDeclaredField(fieldName)
        field?.isAccessible = true
        return field
    }

    private fun getValue(field:Field?,obj:Any?):Any? = field?.get(obj)

}