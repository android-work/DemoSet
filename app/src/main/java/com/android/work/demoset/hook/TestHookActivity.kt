package com.android.work.demoset.hook

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
}