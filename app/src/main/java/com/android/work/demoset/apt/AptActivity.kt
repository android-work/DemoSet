package com.android.work.demoset.apt

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.work.apt_annotation.BindView
import com.android.work.demoset.R

class AptActivity:AppCompatActivity() {

    @BindView(R.id.textView)
    lateinit var textView: TextView

    @BindView(R.id.button)
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apt_layout)

        `AptActivity$$Util`().findViewById(this)
        Log.d("TAG","$textView")
        textView.text = "12344567800987654"
        button.visibility = View.GONE
    }
}