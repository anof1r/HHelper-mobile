package com.harman.hhelper

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.harman.hhelper.jsonresponse.JsonResponse

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    var adapter : RcViewAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nav_view = findViewById<NavigationView>(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)

        var rcView : RecyclerView = findViewById(R.id.rcView)

        var list = ArrayList<ListItem>()

        list.addAll(fillArrays(resources.getStringArray(R.array.course),
            resources.getStringArray(R.array.course_content),
            getImageId(R.array.course_img)))

        rcView.hasFixedSize()
        rcView.layoutManager = LinearLayoutManager(this)
        adapter = RcViewAdapter(list,this)
        rcView.adapter = adapter


        var response : JsonResponse = JsonResponse()


        Thread {
            var users: ArrayList<User> = response.jsonResponse()
            runOnUiThread {
                var internInfo: TextView = findViewById(R.id.header_iInfo)
                internInfo.text = users[1].firstName
            }
        }.start()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.id_course -> {
                adapter?.updateAdapter(fillArrays(resources.getStringArray(R.array.course),
                    resources.getStringArray(R.array.course_content),
                    getImageId(R.array.course_img)))
            }
            R.id.id_information ->{
                adapter?.updateAdapter(fillArrays(resources.getStringArray(R.array.information),
                    resources.getStringArray(R.array.information_content),
                    getImageId(R.array.information_img)))

            }
            R.id.id_contacts ->{

            }
            R.id.id_settings ->{

            }
        }
        var drawerLayout  = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    fun fillArrays(titleArray: Array<String>, contentArray: Array<String>,imageArrayId: IntArray): List<ListItem>{
        var listItemArray = ArrayList<ListItem>()
        for (n in titleArray.indices){
            var listItem = ListItem(imageArrayId[n],titleArray[n],contentArray[n])
            listItemArray.add(listItem)
        }
        return listItemArray
    }

    fun getImageId(imageArrayId: Int): IntArray{
        var tArray: TypedArray = resources.obtainTypedArray(imageArrayId)
        val count = tArray.length()
        val ids = IntArray(count)
        for (it in ids.indices){
            ids[it] = tArray.getResourceId(it,0)
        }
        tArray.recycle()
        return ids
    }
}