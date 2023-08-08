package com.android.work.network

import kotlinx.coroutines.*

object ViewModelUtil {

    fun launch(
        scope: CoroutineScope = MainScope(),
        block: suspend CoroutineScope.() -> Unit = {},
        complete: () -> Unit = {},
        failed:(e:Throwable) -> Unit,
        error:(e:Exception) -> Unit
    ): Job {
        return try {
            scope.launch(context = CoroutineExceptionHandler { _, throwable ->
                failed.invoke(throwable)
            }, block = block)
        }catch (e:Exception){
            error.invoke(e)
            Job()
        }finally {
            complete.invoke()
        }
    }
}