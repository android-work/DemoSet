package com.android.work.demoset.displayCutout

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.work.apt_annotation.BindView
import com.android.work.demoset.R
import com.android.work.demoset.apt.AptActivity
import com.android.work.demoset.apt.`AptActivity$$Util`

class DisplayCutoutActivity : AppCompatActivity() {

    @BindView(R.id.text)
    lateinit var text:TextView

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displaycutout_layout)

        `DisplayCutoutActivity$$Util`().findViewById(this)

        window?.decorView?.rootView?.setOnApplyWindowInsetsListener { v, insets ->
            Log.d("DisplayCutoutActivity","setOnApplyWindowInsetsListener:$insets")
            val displayCutout = insets.displayCutout
            Log.d("DisplayCutoutActivity","left:${displayCutout?.safeInsetLeft},top:${displayCutout?.safeInsetTop},right:${displayCutout?.safeInsetTop},bottom:${displayCutout?.safeInsetBottom}")
            text.text = "刘海屏安全区域：left:${displayCutout?.safeInsetLeft},top:${displayCutout?.safeInsetTop},right:${displayCutout?.safeInsetTop},bottom:${displayCutout?.safeInsetBottom}"
            insets!!
        }
    }
}