package com.android.work.demoset

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity:AppCompatActivity() {

    inline fun <reified T> startActivityFun(){
        startActivity(Intent(this,T::class.java))
    }
}