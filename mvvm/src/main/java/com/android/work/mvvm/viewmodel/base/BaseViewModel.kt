package com.android.work.mvvm.viewmodel.base

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.android.work.network.ViewModelUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val mainScope = MainScope()

    fun launch(
        block: suspend CoroutineScope.() -> Unit = {},
        scope:CoroutineScope = mainScope,
        complete: () -> Unit = {},
        failed: (e: Throwable) -> Unit = {
            Toast.makeText(getApplication(), "${it.message}", 0).show()
        },
        error: (e: Exception) -> Unit = {
            Toast.makeText(getApplication(), "${it.message}", 0).show()
        }
    ): Job {
        return ViewModelUtil.launch(
            scope, block, complete, failed, error
        )
    }

    override fun onCleared() {
        super.onCleared()
        mainScope.cancel()
    }
}