package com.android.work.module1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.android.work.apt_annotation.CRoute
import com.android.work.common.RouteUtil


//@Route(path = "/model1/mainActivity1")
@CRoute(route = "MainActivity1")
class MainActivity : AppCompatActivity() {

//    @Autowired
    private var age = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        val textview = findViewById<TextView>(R.id.tv1)
        textview.setOnClickListener {
            /*try {
                val clazz = Class.forName("com.android.work.module2.MainActivity")
                startActivity(Intent(this,clazz))
            }catch (e:Exception){
                Log.e("TAG","e:${e.toString()}")
            }*/
            age = 29
            ARouter.getInstance().build("/model2/mainActivity2").withInt("age",age).navigation()
            RouteUtil.getInstance().jumpActivity("MainActivity2")
        }
    }
}