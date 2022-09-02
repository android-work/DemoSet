package com.android.work.demoset.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.android.work.demoset.sqlite.SqliteOperateHelper
import com.android.work.demoset.sqlite.USER_TAB

class CustomContentProvide : ContentProvider() {
    private val TAG = "DemoSet_CustomContentProvide"

    companion object {
        private var mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private const val path = USER_TAB
        private const val authority = "com.android.work.demoset.provider.CustomContentProvide"
        const val mURI = "content://$authority/$path"

        init {
            mUriMatcher.addURI(authority, path, 1)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val id = mUriMatcher.match(uri)
        Log.d(TAG,"query id:$id   uri:$uri")
        return when (id) {
            1 -> SqliteOperateHelper.query(
                USER_TAB,
                projection = projection,
                selection = selection,
                selectionArgs = selectionArgs,
                sortOrder = sortOrder
            )
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val id = mUriMatcher.match(uri)
        Log.d(TAG, "insert id:$id  oldUri:$uri")
        when (id) {
            1 -> {
                val rowId = SqliteOperateHelper.insert(values, USER_TAB)
                Log.d(TAG, "insert rowId:$rowId")
                if (rowId == null || rowId < 0) {
                    return uri
                }
                val newUri = ContentUris.withAppendedId(uri, rowId)
//                App.getAppContext()?.contentResolver?.notifyChange(newUri,ContentObserver())
                Log.d(TAG, "insert newUri:$newUri")
            }
        }
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val id = mUriMatcher.match(uri)
        Log.d(TAG,"delete id:$id")
        return when (id) {
            1 -> SqliteOperateHelper.delete(
                whereArgs = selectionArgs, whereClause = selection,
                USER_TAB
            ) ?: -1
            else -> -1
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val id = mUriMatcher.match(uri)
        Log.d(TAG,"delete id:$id")
        return when (id) {
            1 -> SqliteOperateHelper.update(
                values,
                USER_TAB,
                whereClause = selection,
                whereArgs = selectionArgs
            ) ?: -1
            else -> -1
        }
    }
}