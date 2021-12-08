package com.harman.hhelper

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class InfoLayout : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_layout)

        val infoTitle: TextView = findViewById(R.id.infoTitle)
        val infoContent: TextView = findViewById(R.id.infoContent)
        val infoImg : ImageView = findViewById(R.id.infoImg)

        infoTitle.text = "Useful Links"
        infoImg.setImageResource(R.drawable.links)
        infoContent.text = intent.extras?.getString("links").toString()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.info_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val infoTitle: TextView = findViewById(R.id.infoTitle)
        val infoContent: TextView = findViewById(R.id.infoContent)
        val infoImg : ImageView = findViewById(R.id.infoImg)

        when(item.itemId){
            R.id.info_links -> {
                infoImg.setImageResource(R.drawable.links)
                infoTitle.text  = "Useful Links"
                infoContent.text = intent.extras?.getString("links").toString()
            }
            R.id.info_literature -> {
                infoImg.setImageResource(R.drawable.literature)
                infoTitle.text = "Additional Literature"
                infoContent.text = intent.extras?.getString("literature")
            }
            R.id.info_schedule -> {
                infoImg.setImageResource(R.drawable.schedule)
                infoTitle.text = "Schedule"
                infoContent.text= intent.extras?.getString("schedule")
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
