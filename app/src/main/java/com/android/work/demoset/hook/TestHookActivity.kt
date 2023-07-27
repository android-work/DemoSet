package com.android.work.demoset.hook

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.R

class TestHookActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook_layout)

        HookUtil.hook(intent.getBooleanExtra("isHook",false))
    }

    fun hookStartPluginActivity(view: View) {
        val intent = Intent()
        intent.component = ComponentName("com.android.work.pluginapp","com.android.work.pluginapp.MainActivity")
        startActivity(intent)
    }
}