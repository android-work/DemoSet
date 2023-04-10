package com.android.work.demoset.design.framework.proxy.static

import android.util.Log
import com.android.work.demoset.design.framework.proxy.IProxy


/**
 * 静态代理，需要实现被代理类实现代理接口，繁琐，不灵活变通
 */
class StaticProxy(private val proxy:IProxy):IProxy {

    override fun proxy(params: Int): Int {
        Log.d("","静态代理类执行之前params：$params")
        val result = proxy.proxy(params)
        Log.d("","静态代理类执行之后result：$result")
        return result
    }
}