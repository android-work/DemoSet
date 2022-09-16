package com.android.work.demoset

import android.content.Intent
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.provider.ContentProviderTestActivity
import okhttp3.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.addInterceptor(object: Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                return chain.proceed(chain.request())
            }
        })

        val call:Call? = null
        call?.enqueue()

    }

    fun contentProvider(view: View){
        startActivity(Intent(this,ContentProviderTestActivity::class.java))
    }
}