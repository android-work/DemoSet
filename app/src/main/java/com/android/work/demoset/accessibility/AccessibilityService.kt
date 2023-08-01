package com.android.work.demoset.accessibility

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi

class AccessibilityService : AccessibilityService() {

    override fun onCreate() {
        super.onCreate()
        Log.e("AccessibilityService", "onCreate")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        Log.e("AccessibilityService", "AccessibilityEvent:${event?.toString()}")

        val rootInfo = rootInActiveWindow
        val findAccessibilityNodeInfosByViewId =
            rootInfo?.findAccessibilityNodeInfosByText("无障碍")
        val dialog =
            rootInfo?.findAccessibilityNodeInfosByText("弹窗")
        val button =
            rootInfo?.findAccessibilityNodeInfosByText("测试")
        Log.v("AccessibilityService","button:$button")
        Log.i("AccessibilityService","dialog:$dialog")
        Log.w(
            "AccessibilityService",
            "findAccessibilityNodeInfosByViewId:$findAccessibilityNodeInfosByViewId"
        )

        dialog?.forEach {
            if (TextUtils.equals(it.text, "弹窗")) {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }

        button?.forEach {
            if (TextUtils.equals(it.text, "测试")) {
                /*while (true) {
                    it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    SystemClock.sleep(1000)
                }*/
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }

        findAccessibilityNodeInfosByViewId?.forEach {
            if (TextUtils.equals(it.text, "开启无障碍服务")) {
                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.e("AccessibilityService", "onServiceConnected")
    }

    override fun onInterrupt() {

    }

    companion object {
        private var instance: AccessibilityService? = null
        fun getInstance(): AccessibilityService? {
            return instance
        }
    }
}