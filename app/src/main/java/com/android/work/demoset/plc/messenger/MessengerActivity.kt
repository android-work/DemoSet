package com.android.work.demoset.plc.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.work.demoset.R
import com.android.work.demoset.plc.messenger.listener.MessengerCallback
import com.android.work.demoset.plc.messenger.service.*

class MessengerActivity : AppCompatActivity() {

    private var content_ll: LinearLayout? = null
    private var messenger: Messenger? = null
    private var receiverMessenger = Messenger(MessengerHandle(object: MessengerCallback{
        override fun callback(data: String?) {
            buildChatItem(data?:"",1)
        }

    }))
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    private fun sendMessenger(what: Int,data:String) {
        val message = Message.obtain(null, what)
        val bundle = Bundle()
        bundle.putString(FROM_CLIENT_DATA, data)
        buildChatItem(data,0)
        message.replyTo = receiverMessenger
        message.data = bundle
        messenger?.send(message)
    }

    private fun buildChatItem(content: String, type: Int = 0) {
        val contentText = TextView(this)
        contentText.text = content
        contentText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        val ruleText = TextView(this)
        contentText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        if (type == 0) {
            params.gravity = Gravity.START
            ruleText.text = "客户端:"
        }
        else {
            params.gravity = Gravity.END
            ruleText.text = "服务端:"
        }
        content_ll?.addView(ruleText,params)
        content_ll?.addView(contentText, params)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger_layout)
        content_ll = findViewById<LinearLayout>(R.id.content_ll)
    }

    fun startProcess(view: View) {
        val result = bindService(
            Intent(this, MessengerService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        Toast.makeText(this,if (result) "子进程开启成功" else "子进程开启失败",0).show()
    }

    fun sendData(view: View) {
        sendMessenger(CLIENT_SEND_MESSAGE_CODE,"二货，你可以滚了")
    }

    fun getData(view: View) {
        sendMessenger(SERVICE_SEND_MESSAGE_CODE,"喊你呢，蠢货")
    }

    companion object {

        class MessengerHandle(private val callback:MessengerCallback) : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    CALLBACK_MESSAGE_CODE -> {
                        val data = msg.data.getString(CALLBACK_CLIENT_DATA)
                        callback.callback(data)
                    }
                    SERVICE_SEND_MESSAGE_CODE -> {
                        val data = msg.data.getString(FROM_SERVICE_DATA)
                        callback.callback(data)
                    }
                }
            }
        }
    }

}