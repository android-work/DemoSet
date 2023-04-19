package com.android.work.demoset.apt.route

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.R
import com.android.work.module1.MainActivity

class ARouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_layout)
    }

    fun testRoute(view: View) {
//        ARouter.getInstance().build("/model1/mainActivity1").navigation()
        startActivity(Intent(this,MainActivity::class.java))
    }
}