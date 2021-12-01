package com.harman.hhelper

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfoLayout : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_layout)


        val linksTextView = findViewById<TextView>(R.id.tvLinksTitle)
        val literatureTv = findViewById<TextView>(R.id.tvLiterature)
        val scheduleInfo = findViewById<TextView>(R.id.tvSchedule)
        linksTextView.text = intent.extras?.getString("links").toString()
        literatureTv.text = intent.extras?.getString("literature")
        scheduleInfo.text = intent.extras?.getString("schedule")
    }

}
