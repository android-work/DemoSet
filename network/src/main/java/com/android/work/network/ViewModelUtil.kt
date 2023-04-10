package com.android.work.network

import android.util.Log
import kotlinx.coroutines.*


object ViewModelUtil {
    private val TAG = "ViewModelUtil"

    fun launch(
        scope: CoroutineScope = MainScope(),
        block: suspend CoroutineScope.() -> Unit,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        complete: () -> Unit = {
            Log.d(TAG,"complete")
        },
        failed: (e:Throwable) -> Unit = {
            Log.d(TAG,"failed:${it.message}")
            Log.e(TAG,"e:${it.toString()}")
        },
        error: (e:Exception) -> Unit = {
            Log.d(TAG,"error:${it.message}")
        },
    ): Job {
        return try {
            scope.launch(context = CoroutineExceptionHandler{_,handler ->
                failed.invoke(handler)
            }, start = start, block = block)
        }catch (e:Exception){
            error.invoke(e)
            Job()
        }finally {
            complete.invoke()
        }
    }
}