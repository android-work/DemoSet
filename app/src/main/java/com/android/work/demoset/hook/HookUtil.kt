package com.android.work.demoset.hook

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log
import android.view.View
import com.android.work.demoset.ProxyActivity
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object HookUtil {
    private const val TAG = "DemoSet_HookUtil"
    private const val INTENT_EXTRA = "INTENT_EXTRA"

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


    /**
     * 由于不需要进行插件activity的清单文件注册，通过hook技术在校验是否注册前将intent中的需要启动的activity进行替换
     *
     * 1、找到第一个hook点位置 ActivityTaskManager.getService().startActivity(whoThread,
                                who.getOpPackageName(), who.getAttributionTag(), intent,
                                intent.resolveTypeIfNeeded(who.getContentResolver()), token,
                                target != null ? target.mEmbeddedID : null, requestCode, 0, null, options);
                                checkStartActivityResult(result, intent);
        2、ActivityTaskManager.getService() --> IActivityTaskManager --> Singleton --> Singleton.mInstance(IActivityTaskManager)

     *  目标是拿到Singleton对象中的mInstance，反向推：获取Singleton对象，就是ActivityTaskManager.IActivityTaskManagerSingleton,获取后通过
     *  动态代理IActivityTaskManager.startActivity的方法
     */

    fun hookStartActivity(context:Context){
        try {
            // 先获取ActivityTaskManager的字节码
            val atmsClazz = Class.forName("android.app.ActivityTaskManager")
            // 获取Singleton对象
            val singletonField = atmsClazz.getDeclaredField("IActivityTaskManagerSingleton")
            singletonField.isAccessible = true
            // 因为是静态属性，所以反射获取无需对象
            val singleton = singletonField.get(null)

            // 获取Singleton的字节码
            val singletonClazz = Class.forName("android.util.Singleton")
            // 获取内部保存的mInstance即IActivityTaskManager对象
            val mInstanceField = singletonClazz.getField("mInstance")
            mInstanceField.isAccessible = true
            val mInstance = mInstanceField.get(singleton)

            val newProxyInstance = Proxy.newProxyInstance(
                context.classLoader,
                atmsClazz.classes
            ) { proxy, method, args ->
                Log.d("DemoSet_Hook", "invoke proxy:$proxy")
                // 过滤只有startActivity才进行替换
                if (method?.name == "startActivity") {
                    var index = -1
                    var argIntent: Intent? = null
                    for ((i, arg) in args.withIndex()) {
                        if (arg is Intent) {
                            index = i
                            argIntent = arg
                            break
                        }
                    }

                    // 替换插件activity信息为宿主中的代理activity,原来的intent需要保留，内部还有其他相关信息
                    val newIntent = Intent()
                    newIntent.component = ComponentName("com.android.work.demoset","com.android.work.demoset.ProxyActivity")
                    newIntent.putExtra(INTENT_EXTRA, argIntent)
                    if(index != -1) {
                        args[index] = newIntent
                    }
                }
                Log.d("DemoSet_Hook","mInstance:$mInstance")
                method?.invoke(mInstance, args)
            }
            Log.d("DemoSet_Hook","newProxyInstance:$newProxyInstance")
            mInstanceField.set(singleton,newProxyInstance)
        }catch (classNotFind:ClassNotFoundException){
            classNotFind.printStackTrace()
        }catch (fieldNotFind: NoSuchFieldException){
            fieldNotFind.printStackTrace()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    /**
     * hookStartActivity是为了规避清单文件校验的问题
     *
     * 下面还需要处理从ams中创建activity的时候换回插件activity
     *
     * 由于8.0之后的代码，ActivityThread.H的逻辑有变动，所以hook点更换为ActivityThread.startActivityNow,变更里面的intent值
     *
     * 目标是动态代理startActivityNow方法，变更参数的intent值
     *
     * 反推：目标是获取ActivityThread对象 -> ActivityThread.sCurrentActivityThread -> ActivityThread.class对象
     */

    fun hookActivityThread_getActivityClient(context: Context){

        try{
            val activityThreadClazz = Class.forName("android.app.ActivityThread")
            val activityThreadField = activityThreadClazz.getDeclaredField("sCurrentActivityThread")
            activityThreadField.isAccessible = true
            val activityThread = activityThreadField.get(null)

            // 动态代理ActivityThread.getActivityClient
            val activityThreadProxy = Proxy.newProxyInstance(context.classLoader,activityThreadClazz.interfaces) { proxy, method, args ->
                Log.d("DemoSet_Hook","hookActivityThread_getActivityClient proxy:$proxy")
                if (method?.name == "startActivityNow"){
                    var index = -1
                    var argIntent:Intent? = null
                    for ((i,arg) in args.withIndex()){
                        if(arg is Intent){
                            argIntent = arg
                            index = i
                            break
                        }
                    }
                    val intent = argIntent?.getParcelableExtra<Intent>(INTENT_EXTRA)
                    if (index != -1) {
                        args[index] = intent
                    }
                }
                method.invoke(activityThread, args)
            }
            activityThreadField.set(activityThread,activityThreadProxy)
        }catch (classNotFind:ClassNotFoundException){
            classNotFind.printStackTrace()
        }catch (fieldNotFind:NoSuchFieldException){
            fieldNotFind.printStackTrace()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    /**
     * hook处理所有的点击事件防抖动
     */
    fun hootClickEvent(){


    }

}