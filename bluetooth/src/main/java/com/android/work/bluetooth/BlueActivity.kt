package com.android.work.bluetooth

import adapter.BlueAdapter
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.work.bluetooth.databinding.ActivityBlueLayoutBinding
import com.android.work.network.BaseViewModelFactory

class BlueActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory(this.application)
        ).get(BlueViewModel::class.java)
    }

    private lateinit var mBinding: ActivityBlueLayoutBinding

    private val permissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Log.d("TAG","it:$it")
            if (it.values.any { result ->
                    result == false
                }) {
                Toast.makeText(this, "请开权限", 0).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_blue_layout)
        findViewById<RecyclerView>(R.id.rv).apply {
            layoutManager = LinearLayoutManager(this@BlueActivity, RecyclerView.VERTICAL, false)
            adapter = BlueAdapter(mutableListOf())
        }
        mBinding.viewModel = viewModel

        permissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_PRIVILEGED
            )
        )
    }

    fun startScan(view: View) = viewModel.scanDevice()
    fun stopScan(view: View) = viewModel.stopScan()
}