package com.android.work.demoset.design.framework.proxy

import java.lang.reflect.Proxy

class ProxyClass : IProxy {

    override fun proxy(params:Int):Int{
        print("我是被代理类，我需要被代理 $params")
        return params
    }
}