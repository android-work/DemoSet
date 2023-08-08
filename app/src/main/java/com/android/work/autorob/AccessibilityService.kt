package com.android.work.autorob

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

/**
 * 用于接受用户设定的抢购步骤
 */
val contents = ArrayList<String>()

/**
 * 监听的应用包
 */
var packName: String = "com.bendibao"

class AccessibilityService : AccessibilityService() {
    private var isLoop = true
    private var thread: Thread? = null
    private var isRunning: Boolean = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        Toast.makeText(this, "无障碍服务已开启", 0).show()
        instance = this
    }

    private fun getClick(child: AccessibilityNodeInfo?) {
        Log.d("AccessibilityService","getClick text:${child?.text}  view:${child?.className}  isClick:${child?.isClickable}")
        if (child?.isClickable == true) {
            mHandle.post {
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else {
            getClick(child?.parent)
        }
    }

    private fun getChildCount(child: AccessibilityNodeInfo?) {
        val counts = child?.childCount
        contents.forEach {
            Log.d("AccessibilityService","text:${child?.text}  view:${child?.className}  isClick:${child?.isClickable}  targetText:${it}   count:${counts}")
            // 当存找到目标组件，执行点击事件
            if (child?.text?.contains(it) == true) {
                getClick(child)
            }
        }

        // 当遍历到无子组件时，停止本次递归查询
        if (counts == null || counts == 0) {
            Toast.makeText(this,"暂无查询次组件",0).show()
            return
        }

        for (i in 0 until counts) {
            getChildCount(child.getChild(i))
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootInfo = rootInActiveWindow
        Log.d("AccessibilityService","rootInfo:$rootInfo")
        // 当前监听到的包名，与目标应用不一致，舍弃监听
        if (!TextUtils.equals(packName, rootInfo?.packageName)) {
            Log.w("AccessibilityService", "packName not equals")
            return
        }

        contents.forEach {
            val nodeInfos = rootInfo?.findAccessibilityNodeInfosByText(it)
            // 如果没有找到目标信息，则通过遍历child寻找
            if (nodeInfos == null || nodeInfos.isEmpty()){
                getChildCount(rootInfo)
            }else{
                // 存在元素，执行点击事件
                nodeInfos.forEach { nodeInfo ->
                    Log.d("","text:${nodeInfo?.text}   view:${nodeInfo?.className}   isClick:${nodeInfo?.isClickable}  targetText:${it}")
                    if (nodeInfo.text?.contains(it) == true){
                        // 执行点击事件
                        getClick(nodeInfo)
                    }
                }
            }
        }
    }

    private val mHandle = Handler(Looper.getMainLooper())

    override fun onInterrupt() {
        Toast.makeText(this, "无障碍服务关闭", 0).show()
        isLoop = false
        isRunning = false
        contents.clear()
    }

    companion object {
        var instance: com.android.work.autorob.AccessibilityService? = null

    }

    fun stop() {
        this.stopSelf()
        isRunning = false
        isLoop = false
        thread = null
    }
}