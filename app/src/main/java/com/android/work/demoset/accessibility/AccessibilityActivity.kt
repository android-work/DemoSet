package com.android.work.demoset.accessibility

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.R
import java.util.zip.Inflater

class AccessibilityActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_accessibility_layout)


    }

    fun startAccessibility(view: View) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    var count:Int = 0

    fun showDialog(view: View) {
        /*AlertDialog.Builder(this).setMessage("gvhbjnkml,bn").setNegativeButton("抢购1",DialogInterface.OnClickListener { _, _ ->
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }).setPositiveButton("测试",DialogInterface.OnClickListener{_,_ ->
            count++
            Toast.makeText(this@AccessibilityActivity,"点击了count:$count",0).show()
        }).show()*/

        count++
//        Toast.makeText(this@AccessibilityActivity,"点击了count:$count",0).show()
        Log.e("AccessibilityActivity","count:$count")
    }
}