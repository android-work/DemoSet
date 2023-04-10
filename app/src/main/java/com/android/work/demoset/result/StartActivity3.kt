package com.android.work.demoset.result

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class StartActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val textView = TextView(this)
        textView.text = "startActivity3"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        textView.setTextColor(Color.RED)
        textView.setBackgroundColor(Color.GRAY)
        setContentView(textView, params)

        textView.setOnClickListener {
            val intent = Intent()
            intent.putExtra("3 -> 1",true)
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}