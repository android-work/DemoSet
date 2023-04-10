package com.android.work.demoset.plc.messenger.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast

const val FROM_CLIENT_DATA = "from_client_data"
const val CALLBACK_CLIENT_DATA = "callback_client_data"
const val FROM_SERVICE_DATA = "from_service_data"
const val CALLBACK_MESSAGE_CODE = 1000
const val CLIENT_SEND_MESSAGE_CODE = 1001
const val SERVICE_SEND_MESSAGE_CODE = 1002

class MessengerService: Service() {
    private val handler = MessengerHandler()
    private val messenger = Messenger(handler)
    companion object{
        private var mContext: Context? = null
        class MessengerHandler : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == CLIENT_SEND_MESSAGE_CODE) {
                    // 接收到客户端数据
                    val data = msg.data.getString(FROM_CLIENT_DATA)
                    Toast.makeText(mContext, data, 0).show()
                    // 给客户端回消息
                    serverSendData(msg,CALLBACK_MESSAGE_CODE,CALLBACK_CLIENT_DATA,"草，知道了，滚")
                }

                if(msg.what == SERVICE_SEND_MESSAGE_CODE){
                    // 给客户端发送消息
                    serverSendData(msg,SERVICE_SEND_MESSAGE_CODE,FROM_SERVICE_DATA,"草，你真鸡儿烦，滚")
                }
            }

            private fun serverSendData(msg: Message,what:Int,msgKey:String,msgContent:String) {
                val replyTo = msg.replyTo
                val message = Message.obtain(null, what)
                val bundle = Bundle()
                bundle.putString(msgKey, msgContent)
                message.data = bundle
                replyTo.send(message)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this.applicationContext
    }
    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }
}