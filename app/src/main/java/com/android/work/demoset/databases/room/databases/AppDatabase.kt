package com.android.work.demoset.databases.room.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.work.demoset.databases.room.dao.UserDao
import com.android.work.demoset.databases.room.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        private var mAppDatabase: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            mAppDatabase?.let {
                return it
            }

            return Room.databaseBuilder(context, AppDatabase::class.java, "user_info").build().apply {
                mAppDatabase = this
            }
        }
    }
}
