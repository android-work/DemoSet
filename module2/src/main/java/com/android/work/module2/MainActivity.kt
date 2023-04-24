package com.android.work.module2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/model2/mainActivity2")
class MainActivity : AppCompatActivity() {
//    @Autowired
    private var age = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        Log.d("TAG","age:$age")

        findViewById<TextView>(R.id.tv2).setOnClickListener {
           val navigtion = ARouter.getInstance().build("/model1/mainActivity1").navigation()
        }

    }
}