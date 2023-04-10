package com.android.work.network

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object NetworkUtil {
    private val httpLoggingInterceptor = HttpLoggingInterceptor()
    private val okHttpClient by lazy {
        val builder = OkHttpClient
            .Builder()
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
        builder.addInterceptor {
            val request = it.request()
            return@addInterceptor it.proceed(request)
        }
        builder.addInterceptor(httpLoggingInterceptor.apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        builder.build()
    }

    fun a(){
        val request = Request.Builder().get().url("").build()
        okHttpClient.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
            }
        })
    }


    /**
     * retrofit对象的构建主要是对请求相关的参数设定
     *
     * retrofit执行流程：
     *      1、通过动态代理，创建出代理类API实现类
     *      2、当我们调用api接口方法时，发起请求，会执行代理类中的方法
     *      3、代理类中方法的执行流程：
     *          a、先获取到方法上的所有注解，主要是解析该方法是用哪个请求方式(POST、GET及对应的path路径)、以及是否有FromUrlEncode、MutablePart注解(打上对应标识)，
     *        保存在RetrofitFactory对象中
     *          b、接着处理方法内的所有注解，如：Path、Url、Query...并且对这些参数的用法是否恰当(Url注解，不允许与path、query同用等限制)
     *        同样将保存在RetrofitFactory对象中
     *          c、最后返回一个ServiceMethode,然后执行invoke方法，内部是执行okhttp的请求逻辑，通过RealCall.enqueue异步执行
     *
     *
     * okhttp：
     *      1、构建okhttpClient对象，主要是设置网络处理的相关设置如：连接、读、写超时，拦截器等
     *      2、构建request对象，主要是设置网络请求的相关参数如：请求头、请求方法、域名等
     *      3、通过okhttpClient.newCall()创建一个RealCall对象
     *      4、通过RealCall.enqueue(AsyncCall)，执行异步请求，并传入一个AsyncCall对象(就是一个Runnable对象)
     *      5、在enqueue内部最后执行的是Dispatcher.enqueue()
     *      6、先将任务添加到readyAsyncCalls的一个异步任务队列中，排队等待执行
     *      7、判断当前正在执行的任务数量是否超过64(同时最大请求不能超过64)
     *      8、判断同一域名的请求数量是否超过5(同一域名最大并发数不能超过5)
     *      9、检查当前同一域名都请求在执行任务以及待执行任务队列中是否有相同的，得意复用次数(目的就是保证每次任务AsyncCall执行时，存在相同域名的就++，复用之前的内部记录的count)
     *      10、准备执行任务，遍历readyAsyncCalls，将内部任务通过线程池执行(线程池可自定义，默认是一个无核心线程、非核心线程数为Int.maxValue、存活时间60s、拒绝策略是默认的泡异常)
     *      11、在AsyncCall.run()中，通过责任链模式执行我们自定义的拦截器+框架内部提供的拦截器
     *      12、先将拦截器进行整理，先将我们自定义的拦截器添加到集合中，其次是框架内部的(重试重定向拦截器、桥接拦截器、缓存拦截器、连接拦截器、服务回调拦截器)
     *      13、这里面的责任链式的执行逻辑：先创建一个RealInterceptorChain对象，执行RealInterceptorChain.proceed这个方法内部执行逻辑类似递归
     *      14、RealInterceptorChain.proceed：创建下一个RealInterceptorChain对象，获取当前的拦截器，并执行interceptor.proceed并将下一个RealInterceptorChain对象传入，
     *    这样就执行拦截器内部的逻辑，在拦截器中又调用RealInterceptorChain.proceed递归到最后一个拦截器
     *      15、看框架提供的拦截器大概的作用：
     *          a、RetryAndFollowUpInterceptor：
     *              1、在请求时，主要是对异常进行捕获并重新向下一个拦截器发起
     *              2、在响应时，主要是对一些需要进行重定向的code进行请求的更新，并重新向下一个拦截器发起
     *              3、对不需要处理的响应直接往上返回response结果
     *          b、BridgeInterceptor：
     *              1、在请求时，主要是对请求报文的处理(如需要压缩的报文进行压缩)，然后向下一个拦截器发起
     *              2、在响应时，主要是对存在需要解压的响应进行解压，然后往上返回response结果
     *          c、CacheInterceptor：
     *              1、在请求时，主要是按内部规则判断是走网络请求，还是直接从缓存中取车response，从缓存中取就直接往上返回response
     *              2、在响应时，是否需要缓存，需要则写入缓存并往上返回
     *          d、ConnectInterceptor：
     *              1、在请求时，主要是在连接池中寻找一个合适、可用的连接，如果不存在，则创建一个连接Socket，然后通过socket连接主机
     *              2、对响应不做任何处理，往上返回响应
     *          e、CallServerInterceptor：
     *              1、获取到服务器下发的响应，对响应进行解析，将响应头、响应body、code解析出来，封装到response中，返回到上一个拦截器
     *      16、最后通过Callback将结果返回到调用方
     */
    val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://www.wanandroid.com")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    inline fun <reified T> create():T{
        return retrofit.create(T::class.java)
    }
}