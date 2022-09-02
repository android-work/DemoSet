package com.android.work.demoset.sqlite

class SqliteException(var msg:String? = null,var code:Int = -1):Exception(msg) {

    override fun toString(): String {
        return "msg:$msg\n code:$code"
    }
}