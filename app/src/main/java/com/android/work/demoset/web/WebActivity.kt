package com.android.work.demoset.web

import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.work.demoset.R

@Route(path = "/app/web")
class WebActivity:AppCompatActivity() {

    lateinit var web:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_layout)

        web = findViewById<WebView>(R.id.web)
        web.settings.apply {
            setJavaScriptEnabled(true)
            setAppCacheEnabled(true)
        }
        web.webChromeClient = webViewChromeClient
        web.webViewClient = webViewClient
        web.loadUrl("https://t.ingeek.com/blnDJ"/*"file:///android_asset/h5.html"*/)
    }

    private val webViewClient = object:WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            web.loadUrl(url?:"");
            return false
        }
    }

    private val webViewChromeClient = object:WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }

        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return true
        }

        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return true
        }
    }
}