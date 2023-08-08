package com.android.work.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetManager {

    private val loggerInterceptor by lazy {
        val it = HttpLoggingInterceptor()
        it.level = HttpLoggingInterceptor.Level.BODY
        it
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(Interceptor {
            val request = it.request()
            //..拦截逻辑
            return@Interceptor it.proceed(request)
        }).addInterceptor(loggerInterceptor).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS).build()
    }

    val retrofit: Retrofit =
        Retrofit.Builder().client(okHttpClient).baseUrl("https://www.wanandroid.com")
            .addConverterFactory(GsonConverterFactory.create()).build()

    inline fun <reified T> create(): T{
        return retrofit.create(T::class.java)
    }
}