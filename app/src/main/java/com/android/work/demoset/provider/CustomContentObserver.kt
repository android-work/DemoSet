package com.android.work.demoset.provider

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.util.Log

class CustomContentObserver(private val handler:Handler): ContentObserver(handler) {
    private val TAG = "DemoSet_CustomContentObserver"

    override fun onChange(selfChange: Boolean) {
//        super.onChange(selfChange)
        Log.d(TAG,"onChange selfChange:$selfChange")
        handler.obtainMessage().sendToTarget()
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
//        super.onChange(selfChange, uri)
        Log.d(TAG,"onChange selfChange:$selfChange  uri:$uri")
        handler.obtainMessage().sendToTarget()
    }

    override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
//        super.onChange(selfChange, uri, flags)
        Log.d(TAG,"onChange selfChange:$selfChange  uri:$uri  flags:$flags")
        handler.obtainMessage().sendToTarget()
    }

    override fun onChange(selfChange: Boolean, uris: MutableCollection<Uri>, flags: Int) {
//        super.onChange(selfChange, uris, flags)
        uris.forEach {
            Log.d(TAG,"onChange selfChange:$selfChange  uri:$it  flags:$flags")
        }
        handler.obtainMessage().sendToTarget()
    }
}