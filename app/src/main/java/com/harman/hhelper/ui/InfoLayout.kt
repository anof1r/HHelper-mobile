package com.harman.hhelper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.harman.hhelper.R


class InfoLayout : AppCompatActivity() {

    private lateinit var tabLayout : TabLayout
    private  lateinit var pager2 : ViewPager2
    private  lateinit var adapter : FragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_tab)

        /*

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


         */

        tabLayout = findViewById(R.id.tab_layout)
        pager2 = findViewById(R.id.view_pager2)

        val fm = supportFragmentManager
        adapter = FragmentAdapter(fm,lifecycle)
        pager2.adapter = adapter


        //bundle.putString("links",intent.extras?.getString("links").toString())

        tabLayout.addTab(tabLayout.newTab().setText("Links"))
        tabLayout.addTab(tabLayout.newTab().setText("Literature"))
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        pager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

    }
}
