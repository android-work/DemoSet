package com.android.work.demoset.provider

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.R
import com.android.work.demoset.data.UserInfo
import com.android.work.demoset.sqlite.*

class ContentProviderTestActivity : AppCompatActivity() {
    private val TAG = "DemoSet_ContentProviderTestActivity"
    private val mCustomContentObserver = CustomContentObserver(object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.d(TAG,"内容变化，通知")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider_layout)
        ContentResolverHelper.registerContentObserver(CustomContentProvide.mURI,mCustomContentObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        ContentResolverHelper.unregisterContentObserver(mCustomContentObserver)
    }

    fun insert(view: View) {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, "小明")
        contentValues.put(COLUMN_SEX, 0)
        contentValues.put(COLUMN_AGE, 28)
        contentValues.put(COLUMN_WEIGHT, 137.8)
        contentValues.put(COLUMN_HEIGHT, 175)
        val newUri =
            ContentResolverHelper.insert(uri = CustomContentProvide.mURI, values = contentValues)
        Log.d(TAG, "insert newUri:$newUri")
    }

    fun delete(view: View) {
        val rowId = ContentResolverHelper.delete(
            uri = CustomContentProvide.mURI,
            where = "$COLUMN_ID = ?",
            selectionArgs = arrayOf("1")
        )
        Log.d(TAG, "delete rowId:$rowId")
    }

    fun update(view: View) {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, "小红")
        contentValues.put(COLUMN_SEX, 1)
        contentValues.put(COLUMN_AGE, 18)
        contentValues.put(COLUMN_WEIGHT, 90.8)
        contentValues.put(COLUMN_HEIGHT, 160)
        val rowId = ContentResolverHelper.update(
            uri = CustomContentProvide.mURI,
            values = contentValues,
            where = "$COLUMN_ID = ?",
            selectionArgs = arrayOf("1")
        )
        Log.d(TAG, "update rowId:$rowId")
    }

    fun query(view: View) {
        val userInfoList = ContentResolverHelper.query(
            uri = CustomContentProvide.mURI, projection = arrayOf(
                COLUMN_ID,
                COLUMN_NAME, COLUMN_SEX, COLUMN_HEIGHT
            )
        )
//        val userInfoList = SqliteOperateHelper.query<UserInfo>(USER_TAB,null,null,null,null,null)
        userInfoList?.forEach {
            Log.d(TAG,"{\n$COLUMN_ID:${it._id}\n" +
                    "$COLUMN_NAME:${it._name}\n" +
                    "$COLUMN_HEIGHT:${it._height}\n" +
                    "$COLUMN_SEX:${it._sex}\n}")
        }
    }
}