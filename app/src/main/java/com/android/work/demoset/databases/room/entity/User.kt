package com.android.work.demoset.databases.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(val userName:String,val age:Int){
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}

