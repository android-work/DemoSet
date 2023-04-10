package com.android.work.demoset.plc.parcel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.work.aidlmodel.parcel.ParcelService
import com.android.work.demoset.R

class ParcelDemoActivity:AppCompatActivity() {

    private var count:Int = 0
    private var mIBinder: IBinder? = null
    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mIBinder = service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mIBinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parcel_layout)
    }

    fun bindService(view: View) {
        bindService(Intent(this, ParcelService::class.java),serviceConnection, Context.BIND_AUTO_CREATE)
    }
    fun getData(view: View) {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        data.writeString("$count")
        val result = mIBinder?.transact(1000,data,reply,0)
        Log.d("DemoSet_ParcelDemoActivity","getData result:$result")
        val resultStr = reply.readInt()
        Log.d("DemoSet_ParcelDemoActivity","getData resultStr:$resultStr")
        Toast.makeText(this,"Parcel 通信：$resultStr",0).show()
        data.recycle()
        reply.recycle()
    }
    fun sendData(view: View) {
        count ++
        Log.d("DemoSet_ParcelDemoActivity","count:$count")
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        data.writeString("$count")
        val result = mIBinder?.transact(1000,data,null,0)
        Log.d("DemoSet_ParcelDemoActivity","sendData result:$result")
        data.recycle()
        reply.recycle()
    }
}