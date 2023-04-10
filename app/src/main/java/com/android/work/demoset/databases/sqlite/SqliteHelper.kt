package com.example.bluetoothdemo.databases.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.android.work.demoset.sqlite.CREATE_USER_TAB
import com.android.work.demoset.sqlite.USER_TAB
import com.google.gson.Gson
import org.json.JSONObject

class SqliteHelper(
    context: Context,
    name: String,
    version: Int,
    factory: SQLiteDatabase.CursorFactory? = null
) : SQLiteOpenHelper(context, name, factory, version) {

    val TAG = "DemoSet_SqliteHelper"

    companion object {
        private var mSqliteHelper: SqliteHelper? = null
        fun getInstance(
            context: Context,
            name: String = "sqlite_demo",
            version: Int = 1,
            factory: SQLiteDatabase.CursorFactory? = null
        ): SqliteHelper {
            if (mSqliteHelper == null) {
                synchronized(SqliteHelper::class.java) {
                    if (mSqliteHelper == null) {
                        mSqliteHelper = SqliteHelper(context, name, version, factory)
                    }
                }
            }
            return mSqliteHelper!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_USER_TAB)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            db?.execSQL("alter table $USER_TAB drop _sex")
        }
    }

    fun query(
        tabName: String,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        limit: String? = null,
        sortOrder: String? = null
    ): Cursor? {
        return readableDatabase.query(
            tabName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder,
            limit
        )
    }

    inline fun <reified E> query(
        tabName: String? = null,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        limit: String? = null,
        sortOrder: String? = null, cursor: Cursor? = null
    ) :MutableList<E> {
        val mTList = mutableListOf<E>()
        var mCursor = cursor
        try {
            if (mCursor == null) {
                mCursor = readableDatabase.query(
                    tabName,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder,
                    limit
                )
            }
            queryT(mTList,mCursor!!)
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            mCursor?.close()
        }
        return mTList
    }

    inline fun <reified E> queryT(mTList: MutableList<E>, cursor: Cursor) {
        while (cursor.moveToNext()){
            // 构建一行数据的jsonObject
            val jsonObj = JSONObject()
            cursor.columnNames.forEachIndexed { index, value ->
                val type = cursor.getType(index)
                Log.d(TAG,"queryT type:$type")
                val columnValue = when(type){
                    Cursor.FIELD_TYPE_INTEGER -> cursor.getIntOrNull(index)
                    Cursor.FIELD_TYPE_FLOAT -> cursor.getFloatOrNull(index)
                    else -> cursor.getStringOrNull(index)
                }
                Log.d(TAG,"index:$index,value:$value,columnValue:$columnValue")
                jsonObj.putOpt(value,columnValue)
            }
            val jsonObjStr = jsonObj.toString()
            Log.d(TAG,"jsonObjStr:$jsonObjStr")
            val e = Gson().fromJson(jsonObjStr,E::class.java)
            mTList.add(e)
        }
    }

    fun insert(key:String,value:Any,tabName: String):Long{
        val contentValue = putContentValues(value, key)
        return readableDatabase.insert(tabName, null, contentValue)
    }

    fun insert(contentValues: ContentValues?,tabName: String):Long{
        if (contentValues == null){
            return -1
        }
        return readableDatabase.insert(tabName, null, contentValues)
    }

    private fun putContentValues(value: Any, key: String): ContentValues {
        val contentValue = ContentValues()
        when (value::class.java) {
            String::class.java -> {
                contentValue.put(key, value as String)
            }
            Int::class.java -> {
                contentValue.put(key, value as Int)
            }
            Double::class.java -> {
                contentValue.put(key, value as Double)
            }
            Byte::class.java -> {
                contentValue.put(key, value as Byte)
            }
            ByteArray::class.java -> {
                contentValue.put(key, value as ByteArray)
            }
            Float::class.java -> {
                contentValue.put(key, value as Float)
            }
            Short::class.java -> {
                contentValue.put(key, value as Short)
            }
            Long::class.java -> {
                contentValue.put(key, value as Long)
            }
        }
        return contentValue
    }

    fun delete(whereClause:String?,whereArgs:Array<String>?,tabName: String):Int{
        return readableDatabase.delete(tabName,whereClause,whereArgs)
    }

    fun update(value: Any,key: String,tabName: String,whereClause:String? = null,whereArgs:Array<String>? = null):Int{
        val contentValue = putContentValues(value,key)
        return readableDatabase.update(tabName,contentValue,whereClause,whereArgs)
    }

    fun update(value:ContentValues?,tabName: String,whereClause:String? = null,whereArgs:Array<String>? = null):Int{
        if (value == null){
            return -1
        }
        return readableDatabase.update(tabName,value,whereClause,whereArgs)
    }
}