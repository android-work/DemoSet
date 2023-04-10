package com.android.work.demoset.plc.aidl

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.work.aidlmodel.AidlService
import com.android.work.aidlmodel.IMyAidlInterface
import com.android.work.demoset.R

class AidlDemoActivity: AppCompatActivity() {

    private var aidl:IMyAidlInterface? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("AidlDemoActivity","onServiceConnected")
            aidl = IMyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl_layout)
    }

    fun bindService(view: View) {
        Log.d("AidlDemoActivity","bindService")
        val a = bindService(Intent(this,AidlService::class.java),serviceConnection, Service.BIND_AUTO_CREATE)
        Log.d("AidlDemoActivity","a:$a")
//        startService(Intent(this,AidlService::class.java))

//        val intent = Intent(this, AidlService::class.java)
//        bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE)
    }

    fun readStr(view: View) {
        aidl?.readStr("cnmd,wxxd,gnmd,wdaqqnl,qnmd,whswblt,gnmd,whswblt")
    }
    fun backStr(view: View) {
        val str = aidl?.backStr(10)
        Log.d("AidlDemoActivity","str:$str")
    }
    fun backBean(view: View) {
        val bean = aidl?.backBean()
        Log.d("AidlDemoActivity","a1:${bean?.a1},a2:${bean?.a2},a3:${bean?.a3},a4:${bean?.isA4}")
    }
    fun opration(view: View) {
        Log.d("AidlDemoActivity","opration start")
        aidl?.opration()
        Log.d("AidlDemoActivity","opration end")
    }
}