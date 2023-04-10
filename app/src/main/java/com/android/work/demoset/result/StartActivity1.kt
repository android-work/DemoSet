package com.android.work.demoset.result

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class StartActivity1 : AppCompatActivity() {
    private lateinit var launcher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val textView = TextView(this)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        textView.setTextColor(Color.RED)
        textView.setBackgroundColor(Color.GRAY)
        setContentView(textView, params)


        textView.setOnClickListener {
            launcher.launch(Intent(this,StartActivity2::class.java))
        }

        textView.setOnLongClickListener {
            launcher.launch(Intent(this,StartActivity3::class.java))
            true
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val intent = it.data
            if(it.resultCode == RESULT_OK && intent != null){
                val from2to1 = intent.getStringExtra("2 -> 1")
                val from3to1 = intent.getBooleanExtra("3 -> 1",false)
                Log.d("TAG","from2to1:$from2to1    from3to1:$from3to1")
            }
        }


    }
}