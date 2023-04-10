package com.android.work.aidlmodel.parcel

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.util.Log
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.PriorityBlockingQueue

class ParcelService: Service() {
    private val linkedBlockingQueue = LinkedBlockingQueue<String>(30)
    private val mBinder = object: Binder(){
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            Log.d("DemoSet_ParcelService","code:$code")
            if(code == 1000){
                val str = data.readString()
                Log.d("DemoSet_ParcelService","str:$str   reply:$reply")
                if(reply != null) {
                    reply.writeInt(str?.toInt() ?: 0)
                    return true
                }
            }
            return super.onTransact(code, data, reply, flags)
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }
}