package com.harman.hhelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class ContentManager : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_manager)

        val web = findViewById<WebView>(R.id.webView)
        var url = intent.getStringExtra("url")

        if (url != null) {
            web.settings.javaScriptEnabled = true
            web.webViewClient = WebViewClient()
            web.loadUrl(url)
        }
    }
}