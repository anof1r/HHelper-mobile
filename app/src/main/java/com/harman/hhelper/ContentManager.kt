package com.harman.hhelper

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class ContentManager : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_manager)

        val web = findViewById<WebView>(R.id.webView)
        var url = intent.getStringExtra("url")

        if (url != null) {
            web.webViewClient
            web.settings.javaScriptEnabled
            web.loadUrl(url)
        }
    }
}