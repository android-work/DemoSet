package com.example.bluetoothdemo.databases

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.R

class DatabaseActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_layout)
    }

    fun toRoom(view: View) {

    }
    fun toSqlite(view: View) {}
}