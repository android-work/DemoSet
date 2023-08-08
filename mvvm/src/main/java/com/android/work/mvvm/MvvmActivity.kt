package com.android.work.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.work.mvvm.databinding.ActivityMvvmBinding
import com.android.work.mvvm.viewmodel.MvvmViewModel

class MvvmActivity:AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(MvvmViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBindingUtil:ActivityMvvmBinding = DataBindingUtil.setContentView(this,R.layout.activity_mvvm)

        mBindingUtil.viewModel = viewModel

        viewModel.requestData()

        viewModel.liveData.observe(this){
            mBindingUtil.text.text = it
        }
    }


}