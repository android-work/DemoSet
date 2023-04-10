package com.android.work.demoset.databases.room.dao

import androidx.room.*
import com.android.work.demoset.databases.room.entity.User

@Dao
interface UserDao {

    @Insert
     fun insertUser(user: User):Long

    @Query("delete from User where age = :age")
     fun deleteUser(age: Int)

    @Query("update User set userName = :userName where id = :id ")
     fun updateUser(id: Int,userName: String)

    @Query("select * from User")
     fun queryUser():List<User>?
}
