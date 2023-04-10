package com.android.work.demoset.hook

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log
import android.view.View

object HookUtil {
    private const val TAG = "DemoSet_HookUtil"

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    fun hook(isHook:Boolean = true) {
        try {
            val clazz = Class.forName("android.view.WindowManagerGlobal")
            val method = clazz.getDeclaredMethod("getInstance")
            val windowManagerGlobal = method.invoke(clazz)
            val field = clazz.getDeclaredField("mViews")
            field.isAccessible = true
            val mViews = field.get(windowManagerGlobal) as ArrayList<View>
            val paint = Paint()
            val cm = ColorMatrix()
            if(isHook){
                cm.setSaturation(0f)
            }else{
                cm.setSaturation(1f)
            }

            paint.colorFilter = ColorMatrixColorFilter(cm)

            val proxyArrayList = ProxyArrayList<View>()

            proxyArrayList.setArrayListAddListener(object:ArrayListAddListener<View>{

                override fun addListener(view: View) {
                    view.setLayerType(View.LAYER_TYPE_HARDWARE,paint)
                    Log.d(TAG,"-----------------------------")
                }
            })

            Log.d(TAG,"proxyArrayList:${proxyArrayList.size}")
            proxyArrayList.addAll(mViews)
            field.set(windowManagerGlobal,proxyArrayList)

        }catch (noSuchMethodException:NoSuchMethodException){
            noSuchMethodException.printStackTrace()
            Log.e(TAG,noSuchMethodException.toString())
        }catch (securityException:SecurityException){
            securityException.printStackTrace()
            Log.e(TAG,securityException.toString())
        }catch (classNotFoundException:ClassNotFoundException){
            classNotFoundException.printStackTrace()
            Log.e(TAG,classNotFoundException.toString())
        }catch (e: Exception){
            e.printStackTrace()
        }

    }
}