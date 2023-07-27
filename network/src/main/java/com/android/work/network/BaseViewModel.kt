package com.android.work.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*

class BaseViewModelFactory(private val application: Application? = null) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val t = modelClass.newInstance()
        if (t is BaseViewModel){
            t.context = application
        }
        Log.d("TAG","-------------t:$t")
        return t
    }

}

open class BaseViewModel(var context: Application? = null) : AndroidViewModel(context!!) {
    private val TAG = "BaseViewModel"
    private val scope = MainScope()

    fun launch(
        block: suspend CoroutineScope.() -> Unit,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        complete: () -> Unit = {
            Log.d(TAG, "complete")
        },
        failed: (e: Throwable) -> Unit = {
            Log.d(TAG, "failed:${it.message}")
            Log.e(TAG, "e:${it.toString()}")
        },
        error: (e: Exception) -> Unit = {
            Log.d(TAG, "error:${it.message}")
        },
    ): Job {
        return ViewModelUtil.launch(
            start = start,
            block = block,
            failed = failed,
            error = error,
            complete = complete,
        )
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}