package com.android.work.autorob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.work.apt_annotation.BindView
import com.android.work.autorob.viewmodel.ThirdViewModel

class MainActivity : AppCompatActivity() {

    @BindView(R.id.edit)
    lateinit var edit:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        `MainActivity$$Util`().findViewById(this)
    }

    fun save(view: View) {
        val value = edit.text.toString()
        contents.add(value)
        Toast.makeText(this,"保存成功${value}",0).show()
    }
    fun slipSave(view: View) {
        val content = edit.text.toString()
        if(content.contains("、")){
            val splits = content.split("、")
            contents.addAll(splits.toList())
        }
        Toast.makeText(this,"分割保存成功${contents}",0).show()
    }

    fun over(view: View) {
        if(contents.isEmpty()){
            Toast.makeText(this,"请输入流程步骤",0).show()
            return
        }
        val intent = Intent(this,SecondActivity::class.java)
        startActivity(intent)
    }

    fun clear(view: View) {
        edit.setText("")
        contents.clear()

//        startActivity(Intent(this,MvvmActivity::class.java))
    }

}