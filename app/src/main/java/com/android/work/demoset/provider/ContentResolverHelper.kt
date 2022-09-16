package com.android.work.demoset.provider

import android.content.ContentValues
import android.database.ContentObserver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.android.work.demoset.App
import com.android.work.demoset.data.UserInfo
import com.android.work.demoset.sqlite.SqliteOperateHelper
import com.android.work.demoset.sqlite.USER_TAB

object ContentResolverHelper{
    private const val TAG = "DemoSet_ContentResolverHelper"

    fun registerContentObserver(uri: String,observer:ContentObserver){
        App.getAppContext()?.let {
            it.contentResolver.registerContentObserver(Uri.parse(uri),true,observer)
        }
    }

    fun unregisterContentObserver(observer: ContentObserver){
        App.getAppContext()?.let{
            it.contentResolver.unregisterContentObserver(observer)
        }
    }

    fun insert(uri: String, values: ContentValues): Uri? {
        App.getAppContext()?.let {
            return it.contentResolver.insert(Uri.parse(uri), values)
        }
        return null
    }

    fun delete(uri: String, where: String? = null, selectionArgs: Array<String>? = null): Int {
        App.getAppContext()?.let {
            return it.contentResolver.delete(Uri.parse(uri), where, selectionArgs)
        }
        return -1
    }

    fun update(
        uri: String,
        values: ContentValues,
        where: String? = null,
        selectionArgs: Array<String>? = null
    ): Int {
        App.getAppContext()?.let {
            return it.contentResolver.update(Uri.parse(uri), values, where, selectionArgs)
        }
        return -1
    }

    fun query(
        uri: String,
        projection: Array<String>? = null,
        where: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ) :MutableList<UserInfo>?{
        App.getAppContext()?.let {
            Log.d(TAG,"${Uri.parse(uri)}")
            val cursor = it.contentResolver.query(Uri.parse(uri), projection, where, selectionArgs, sortOrder)
            return SqliteOperateHelper.query<UserInfo>(tabName = USER_TAB, cursor = cursor)
        }
        return null
    }
}