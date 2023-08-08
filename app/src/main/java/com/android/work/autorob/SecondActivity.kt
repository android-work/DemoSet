package com.android.work.autorob

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.android.work.apt_annotation.BindView

class SecondActivity:AppCompatActivity() {

    @BindView(R.id.ll)
    lateinit var ll:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        `SecondActivity$$Util`().findViewById(this)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK){
                if (it.data != null && it.data?.getStringExtra("packName")?.isNotEmpty() == true){
                    ll.visibility = View.VISIBLE
                    packName = it.data!!.getStringExtra("packName")!!

                }
            }
        }
    }


    fun checkServer(view: View) {
        if (!isAccessibilitySettingsOn(this,AccessibilityService::class.java.name)){
            jumpToSettingPage(this)
        }
    }

    //跳转到开启无障碍设置页面
    private fun jumpToSettingPage(context:Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    //判断自定义辅助功能服务是否开启
    private fun isAccessibilitySettingsOn(context:Context, className:String):Boolean {
        if (context == null) return false
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //判断当前无障碍服务是否在运行服务列表中
        val runningServices:List<ActivityManager.RunningServiceInfo> = activityManager.getRunningServices(100);
        for (i in runningServices.indices) {
            val service = runningServices[i].service
            if (service.className == className) {
                return true
            }
        }
        return false
    }

    fun closeServer(view: View) {
        AccessibilityService.instance?.stop()
        jumpToSettingPage(this)
    }

    private lateinit var launcher: ActivityResultLauncher<Intent>

    fun choseAPP(view: View) {
        launcher.launch(Intent(this,ThirdActivity::class.java))
    }
}