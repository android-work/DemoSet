package com.android.work.aidlmodel

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.os.SystemClock
import android.util.Log
import com.android.work.aidlmodel.AidlBean
import java.io.FileInputStream

class AidlService:Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("AidlService","onCreate")
        /*val intent = Intent(this,ServerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)*/
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("AidlService","onBind")
        return object:IMyAidlInterface.Stub(){
            override fun readStr(str: String?) {
                Log.d("AidlService","readStr str:$str")
            }

            override fun backStr(value: Int): Int {
                val c = 3 + value
                Log.d("AidlService","value:$value     c:$c")
                return c
            }

            override fun backBean(): AidlBean {
                Log.d("AidlService","backBean")
                return AidlBean("a1","a2",3,false)
            }

            override fun opration() {
                var count = 0
                while(count < 5){
                    Log.d("AidlService","opration count:$count")
                    SystemClock.sleep(800)
                    count ++
                }
            }

            override fun sendData(pfd: ParcelFileDescriptor?) {
                val fileDescriptor = pfd?.fileDescriptor
                val fis = FileInputStream(fileDescriptor)
                val byte = fis.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(byte,0,byte.size)

            }

        }
    }
}