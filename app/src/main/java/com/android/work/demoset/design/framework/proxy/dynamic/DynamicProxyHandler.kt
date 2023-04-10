package com.android.work.demoset.design.framework.proxy.dynamic

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 动态代理可做到代理新类时，无需定义新接口，但是被代理类需要实现接口，否则无法被代理
 */
class DynamicProxyHandler(private val obj:Any,private val function:(method:Method?,args:Array<out Any>?)->Any?) : InvocationHandler{
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        /*Log.d("DynamicProxyHandler","=============执行代理类之前===========$obj   ${args?.size}")
        val result = method?.invoke(obj,*(args?: arrayOf()))
        Log.d("DynamicProxyHandler","=============代理类执行完毕===========$result")*/
        return function(method,args)
    }

    companion object{
        fun <T> proxy(clazz: Class<T>,obj:Any,function:(method:Method?,args:Array<out Any>?)->Any?):Any{
            val proxy = Proxy.newProxyInstance(clazz.classLoader,clazz.interfaces,DynamicProxyHandler(obj,function))
            return proxy
        }
    }
}