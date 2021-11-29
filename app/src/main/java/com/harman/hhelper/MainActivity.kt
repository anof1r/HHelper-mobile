package com.harman.hhelper

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.harman.hhelper.main_content_jsonresponse.MainContent
import com.harman.hhelper.main_content_jsonresponse.MainContentResponse
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    var adapter : RcViewAdapter?= null
    var mcResponse : MainContentResponse = MainContentResponse()
    var contentArray : ArrayList<MainContent> = ArrayList()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nav_view = findViewById<NavigationView>(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)


        var rcView : RecyclerView = findViewById(R.id.rcView)

        var list = ArrayList<MainContent>()


        rcView.hasFixedSize()
        rcView.layoutManager = LinearLayoutManager(this)
        adapter = RcViewAdapter(list,this)
        rcView.adapter = adapter


        GlobalScope.launch {
                contentArray = mcResponse.getMainContent()
                adapter?.updateAdapter(contentArray)
        }
       // Thread {
         //    contentArray = mcResponse.getMainContent()
        //}.start()
    }
    override fun onStart(){
        super.onStart()
        if(auth.currentUser == null){
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.id_course -> {
        // Clear array + checkArrayChanges
            }
            R.id.id_information ->{
               // adapter?.updateAdapter(fillArrays(resources.getStringArray(R.array.information),
                    //resources.getStringArray(R.array.information_content),
                    //getImageId(R.array.information_img)))
            }
            R.id.id_contacts ->{

            }
            R.id.id_settings ->{
                auth.signOut()
                startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            }
        }
        val drawerLayout  = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    /*
    private fun fillArrays(title:String, content: String, id:Int, imageId:Int, date:String, hw:String): List<MainContent>{
        val listItemArray = ArrayList<MainContent>()
        val listItem = MainContent(date,imageId,hw,id,title,content)
        listItemArray.add(listItem)
        return listItemArray
    }

     */

    fun getImageId(imageArrayId: Int): IntArray{
        val tArray: TypedArray = resources.obtainTypedArray(imageArrayId)
        val count = tArray.length()
        val ids = IntArray(count)
        for (it in ids.indices){
            ids[it] = tArray.getResourceId(it,0)
        }
        tArray.recycle()
        return ids
    }
}