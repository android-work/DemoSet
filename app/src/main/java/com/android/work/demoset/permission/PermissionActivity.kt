package com.android.work.demoset.permission

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.R

class PermissionActivity : AppCompatActivity() {

    // 单权限申请
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(this, "权限申请成功", 0).show()
            } else {
                Toast.makeText(this, "权限申请失败", 0).show()
            }
        }

    // 多权限申请
    private val permissionMutableRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { mutableMap ->
            if (mutableMap.values.any { !it }) {
                Toast.makeText(this, "权限申请失败", 0).show()
            }else{
                Toast.makeText(this, "权限申请成功", 0).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_permission_layout)
    }

    fun singlePermissionRequest(view: View) =
        permissionRequest.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun mutablePermissionRequest(view: View) = permissionMutableRequest.launch(
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE
        )
    )
}