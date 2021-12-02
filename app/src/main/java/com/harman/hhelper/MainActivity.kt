package com.harman.hhelper

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.harman.hhelper.information_response.InfoResponse
import com.harman.hhelper.information_response.Information
import com.harman.hhelper.main_content_jsonresponse.MainContent
import com.harman.hhelper.main_content_jsonresponse.MainContentResponse
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private var adapter : RcViewAdapter?= null
    private var mcResponse : MainContentResponse = MainContentResponse()
    private var information : Information = Information()
    private var contentArray : ArrayList<MainContent> = ArrayList()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var informationResponse : InfoResponse

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nav_view = findViewById<NavigationView>(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)

        //webView = WebView(this)
        val rcView : RecyclerView = findViewById(R.id.rcView)

        val list = ArrayList<MainContent>()

        rcView.hasFixedSize()
        rcView.layoutManager = LinearLayoutManager(this)
        adapter = RcViewAdapter(list,this)
        rcView.adapter = adapter



        GlobalScope.launch {
                contentArray = mcResponse.getMainContent()
                adapter?.updateAdapter(contentArray)
                informationResponse = information.getMainContent()
            //val user = findViewById<TextView>(R.id.header_info)
            //user.text = auth.currentUser?.email
        }
    }
    override fun onStart(){
        super.onStart()
        if(auth.currentUser == null){
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
        }
    }

    @SuppressLint("ResourceType")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_course -> {
                adapter?.updateAdapter(contentArray)
                // Clear array + checkArrayChanges
            }
            R.id.id_information -> {
                val intent = Intent(this@MainActivity,InfoLayout::class.java)
                intent.putExtra("links",information.getLinks())
                intent.putExtra("literature",information.getLinksL())
                intent.putExtra("schedule",information.getSchedule())
                //переделать на NestedScrollView
                startActivity(intent)
            }
            R.id.id_contacts ->{

            }
            R.id.night_mode ->{

            }
            R.id.id_sign_out ->{
                auth.signOut()
                startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            }
            R.id.id_content_manager -> {
                val intent = Intent(this@MainActivity,ContentManager::class.java)
                intent.putExtra("url","http://192.168.0.4:8080/web_view")
                startActivity(intent)
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
    /*
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

     */
}