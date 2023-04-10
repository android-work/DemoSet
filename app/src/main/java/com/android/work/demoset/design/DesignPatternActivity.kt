package com.android.work.demoset.design

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.work.demoset.R
import com.android.work.demoset.databinding.ActivityDesignPatternLayoutBinding
import com.android.work.demoset.design.create.SingleDesign
import com.android.work.demoset.design.framework.proxy.IProxy
import com.android.work.demoset.design.framework.proxy.ProxyClass
import com.android.work.demoset.design.framework.proxy.dynamic.DynamicProxyHandler
import com.android.work.demoset.design.framework.proxy.static.StaticProxy
import java.lang.reflect.Proxy

class DesignPatternActivity:AppCompatActivity() {

    private lateinit var binding:ActivityDesignPatternLayoutBinding
    private val viewModel by lazy { ViewModelProvider.NewInstanceFactory().create(DesignPatternViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_design_pattern_layout)
        binding.viewModel = viewModel
    }

    fun mvvm(view: View) = viewModel.getData()

    fun singleDesign(view: View) {
        SingleDesign.getInstance().getDes()
        SingleDesign.getSingleDesign().getDes()
        SingleDesign.getInstance2().getDes()
    }

    val proxy = DynamicProxyHandler.proxy(ProxyClass::class.java,ProxyClass()){
            method,args ->
        Log.d("DynamicProxyHandler","=============执行代理类之前===========${args?.size}")
        val result = method?.invoke(ProxyClass(),*(args?: arrayOf()))
        Log.d("DynamicProxyHandler","=============代理类执行完毕===========$result")
        result
    } as IProxy

    var a = 22
    fun proxy(view: View) {

        val dynamicProxyResult = proxy.proxy(a)
        a++

        val staticProxy = StaticProxy(ProxyClass())
        val staticProxyResult = staticProxy.proxy(a)
        Toast.makeText(this,"动态代理结果：$dynamicProxyResult    静态代理结果：$staticProxyResult",0).show()


    }
}