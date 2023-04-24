package com.android.work.common.thread_pool

import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class ThreadPoolFactory {

    companion object{
        private var mThreadPoolFactory:ThreadPoolFactory? = null

        fun getInstance():ThreadPoolFactory{
            if (mThreadPoolFactory == null){
                synchronized(ThreadPoolFactory.javaClass){
                    if(mThreadPoolFactory == null){
                        mThreadPoolFactory = ThreadPoolFactory()
                    }
                }
            }
            return mThreadPoolFactory!!
        }
    }

    private val mThreadPool by lazy { Executors.newScheduledThreadPool(4) }

    fun executeTask(task:Runnable){
        mThreadPool.execute(task)
    }
}