package com.android.work.demoset.databases

import android.util.Log
import com.android.work.demoset.databases.room.databases.AppDatabase
import com.android.work.demoset.databases.room.entity.User
import com.android.work.network.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseViewModel: BaseViewModel() {
    var age:Int = 0

    fun add() {
        launch(block = {
            withContext(Dispatchers.IO){
                age += 1
                context?.apply {
                    val user = User(userName = "user:$age", age = age)
                    val result = AppDatabase.getInstance(this).getUserDao().insertUser(user)
                    Log.d("DatabaseViewModel","add result:$result")
                }
                query()
            }
        })
    }

    fun delete(){
        launch({
            withContext(Dispatchers.IO){
                context?.apply {
                    val user = User(userName = "user:$age", age = age)
                    AppDatabase.getInstance(this).getUserDao().deleteUser(age)
                    age -= 1
                    query()
                    Log.d("DatabaseViewModel","delete over:$age")
                }
            }
        })
    }

    fun update(){
        launch({
            withContext(Dispatchers.IO){
                context?.apply {
                    AppDatabase.getInstance(this).getUserDao().updateUser(age,"user:$age.$age")
                    query()
                    Log.d("DatabaseViewModel","update")
                }
            }
        })
    }

    fun query(){
        launch({
            withContext(Dispatchers.IO){
                context?.apply {
                    val result = AppDatabase.getInstance(this).getUserDao().queryUser()
                    result?.forEach{
                        Log.d("DatabaseViewModel","query id:${it.id}   name:${it.userName}   age:${it.age}")
                    }
                }
            }
        })
    }
}