package com.android.work.mvvm.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.work.mvvm.reporsity.Reporsity
import com.android.work.mvvm.viewmodel.base.BaseViewModel

class MvvmViewModel(application: Application) : BaseViewModel(application) {

    // 可设置默认值
    val liveData = MutableLiveData<String>("1111")

    // 数据源
    val observableField =  ObservableField<String>("2222")


    fun requestData(){
        launch({
           val data =  Reporsity.request()
            observableField.set(data?.toString())
            liveData.value = data?.datas?.size?.toString()
        })
    }
}