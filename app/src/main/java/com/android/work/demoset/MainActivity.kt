package com.android.work.demoset

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.hot_fix.HotFixTest
import com.android.work.demoset.provider.ContentProviderTestActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun contentProvider(view: View){
        startActivity(Intent(this,ContentProviderTestActivity::class.java))
    }

    fun uncaughtExceptionHandler(view: android.view.View) {
        Thread(Runnable {
            3 / 0
        }).start()
    }

    fun hotFix(view: android.view.View) {
        HotFixTest().hotFix()
    }


}