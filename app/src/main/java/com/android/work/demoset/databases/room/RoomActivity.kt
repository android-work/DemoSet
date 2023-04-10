package com.android.work.demoset.databases.room

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.work.demoset.R
import com.android.work.demoset.databases.DatabaseViewModel
import com.android.work.network.BaseViewModelFactory

class RoomActivity : AppCompatActivity() {
    private val viewModel: DatabaseViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory(this.application)
        ).get(DatabaseViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_layout)
    }

    fun add(view: View) = viewModel.add()
    fun query(view: View) = viewModel.query()
    fun update(view: View) = viewModel.update()
    fun delete(view: View) = viewModel.delete()
}