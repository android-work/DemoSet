package com.android.work.demoset.sqlite

import android.content.ContentValues
import android.database.Cursor
import com.android.work.demoset.App

object SqliteOperateHelper {

    val mSqliteHelper = App.getAppContext()?.let { SqliteHelper.getInstance(it) }

    fun insert(key: String, value: Any, tabName: String) =
        mSqliteHelper?.insert(key, value, tabName)

    fun insert(contentValues:ContentValues?, tabName: String) =
        mSqliteHelper?.insert(contentValues, tabName)

    fun delete(whereArgs: Array<String>? = null, whereClause: String? = null, tabName: String) =
        mSqliteHelper?.delete(whereArgs = whereArgs, whereClause = whereClause, tabName = tabName)

    fun update(
        value: Any,
        key: String,
        tabName: String,
        whereClause: String? = null,
        whereArgs: Array<String>? = null
    ) = mSqliteHelper?.update(
        value,
        key,
        tabName = tabName,
        whereClause = whereClause,
        whereArgs = whereArgs
    )

    fun update(
        values: ContentValues?,
        tabName: String,
        whereClause: String? = null,
        whereArgs: Array<String>? = null
    ) = mSqliteHelper?.update(
        values,
        tabName = tabName,
        whereClause = whereClause,
        whereArgs = whereArgs
    )

    inline fun <reified E> query(
        tabName: String? = null,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        limit: String? = null,
        sortOrder: String? = null, cursor: Cursor? = null
    ): MutableList<E>? =
        mSqliteHelper?.query<E>(tabName, projection, selection, selectionArgs, limit, sortOrder)

    fun query(
        tabName: String,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        limit: String? = null,
        sortOrder: String? = null
    ): Cursor? = mSqliteHelper?.query(
        tabName,
        projection,
        selection,
        selectionArgs,
        limit,
        sortOrder
    )
}