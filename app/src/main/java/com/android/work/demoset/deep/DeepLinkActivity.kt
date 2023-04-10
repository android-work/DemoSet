package com.android.work.demoset.deep

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.App
import com.android.work.demoset.R

class DeepLinkActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_deep_link_layout)
        getH5JumpParams()

    }

    private fun getH5JumpParams() {
        val uri = intent.data
        Log.d("TAG","uri:${uri?.toString()}   v:${App.v}")
        uri?.apply {
            val a = getQueryParameter("a")
            val b = getQueryParameter("b")

            Log.d("TAG","a:$a   b:$b")
        }
    }
}