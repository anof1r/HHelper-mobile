package com.harman.hhelper.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.harman.hhelper.R
import com.harman.hhelper.api.ApiRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class InfoLayout : AppCompatActivity() {

    private lateinit var tabLayout : TabLayout
    private  lateinit var pager2 : ViewPager2
    private  lateinit var adapter : FragmentAdapter

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_tab)


        tabLayout = findViewById(R.id.tab_layout)
        pager2 = findViewById(R.id.view_pager2)

        val fm = supportFragmentManager
        adapter = FragmentAdapter(fm,lifecycle)
        pager2.adapter = adapter


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

        val fab = findViewById<FloatingActionButton>(R.id.fab_info)
        fab.setOnClickListener{
            updateInfo()
        }
        pager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    @DelicateCoroutinesApi
    private fun updateInfo() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.update_info_dialog, null)

        val addDialog = AlertDialog.Builder(this)

        val uLinks = v.findViewById<EditText>(R.id.updateLinks)
        val uLiterature = v.findViewById<EditText>(R.id.updateLiterature)
        val uSchedule = v.findViewById<EditText>(R.id.updateSchedule)

        uLinks.setText(intent.getStringExtra("links"))
        uLiterature.setText(intent.getStringExtra("literature"))
        uSchedule.setText(intent.getStringExtra("schedule"))
        addDialog.setView(v)
        addDialog.setPositiveButton("Update") { dialog, _ ->

            val links = uLinks.text.toString()
            val literature = uLiterature.text.toString()
            val schedule = uSchedule.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                postCurrentData(literature,schedule,links)
            }

            Toast.makeText(this, "Adding User Information Success", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }
        addDialog.create()
        addDialog.show()
    }

    @DelicateCoroutinesApi
    suspend fun postCurrentData(literature : String, schedule : String, links : String) {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
        api.updateInfo(literature,schedule,links).awaitResponse()
    }
}
