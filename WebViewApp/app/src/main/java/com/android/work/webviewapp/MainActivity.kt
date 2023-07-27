package com.android.work.webviewapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWeb()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWeb(){
        val webView = findViewById<com.tencent.smtt.sdk.WebView>(R.id.web_view)
        webView.settings.apply {
            javaScriptEnabled = true
            setSupportZoom(true)
            setSupportMultipleWindows(true)
            allowFileAccess = true
        }

        webView.loadUrl("https://dashboard.fav-car.com/login")
    }

}