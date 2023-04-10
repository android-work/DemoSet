package com.android.work.demoset.plc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.work.aidlmodel.parcel.ParcelService
import com.android.work.demoset.R
import com.android.work.demoset.plc.aidl.AidlDemoActivity
import com.android.work.demoset.plc.messenger.MessengerActivity
import com.android.work.demoset.plc.parcel.ParcelDemoActivity

class PLCDemoActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plc_layout)
    }

    fun intoParcel(view: View) {
        startActivity(Intent(this,ParcelDemoActivity::class.java))
    }
    fun intoAidl(view: View) {
        startActivity(Intent(this,AidlDemoActivity::class.java))
    }

    fun intoMessenger(view: View) {
        startActivity(Intent(this,MessengerActivity::class.java))
    }

}