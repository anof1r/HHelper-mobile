package com.harman.hhelper

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ContentActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_layout)
        var tvTitleCL : TextView = findViewById(R.id.tvTitleCL)
        var tvContentCL : TextView = findViewById(R.id.tvContentCL)
        var imgCL : ImageView = findViewById(R.id.imgCL)
        tvTitleCL.text = intent.getStringExtra("title")
        tvContentCL.text = intent.getStringExtra("content")
        //imgCL.setImageResource(intent.getIntExtra("image",R.drawable.harman2))
    }

}