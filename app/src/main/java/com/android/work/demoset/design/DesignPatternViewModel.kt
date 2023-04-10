package com.android.work.demoset.design

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.work.network.BaseViewModel
import com.android.work.network.ViewModelUtil
import com.google.gson.Gson
import kotlinx.coroutines.*

class DesignPatternViewModel : BaseViewModel() {
    val dataStr by lazy { ObservableField<String>() }

    fun getData() {
        launch(
            {
                val baseBean = Reprosity.registerTest()
                val str = Gson().toJson(baseBean)
                dataStr.set(str)
            },
        )
    }


}


