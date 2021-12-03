package com.harman.hhelper

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.harman.hhelper.information_response.InfoResponse
import com.harman.hhelper.information_response.Information
import com.harman.hhelper.main_content_jsonresponse.MainContent
import com.harman.hhelper.main_content_jsonresponse.MainContentResponse
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private var adapter: RcViewAdapter? = null
    private var mcResponse: MainContentResponse = MainContentResponse()
    private var information: Information = Information()
    private var contentArray: ArrayList<MainContent> = ArrayList()
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var informationResponse: InfoResponse
    private var isAdminCheck: Boolean = false

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nav_view = findViewById<NavigationView>(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)

        val rcView: RecyclerView = findViewById(R.id.rcView)

        val list = ArrayList<MainContent>()

        rcView.hasFixedSize()
        rcView.layoutManager = LinearLayoutManager(this)
        adapter = RcViewAdapter(list, this)
        rcView.adapter = adapter

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        GlobalScope.launch {
            contentArray = mcResponse.getMainContent()
            informationResponse = information.getMainContent()

            runOnUiThread() {
                adapter?.updateAdapter(contentArray)
            }
        }
    }

    @SuppressLint("ResourceType")
    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
        checkAccessLevel(auth.uid.toString())
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
                if (isAdminCheck) {
                    Toast.makeText(this,"Welcome Admin!",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, ContentManager::class.java)
                    //intent.putExtra("url", "http://95.79.178.246:8080/web_view")
                    intent.putExtra("url", "http://192.168.0.4:8080/web_view")
                    startActivity(intent)
                } else {
                    Toast.makeText(this,"You're not allowed to use ContentManager",Toast.LENGTH_SHORT).show()
                }
            }
        }
        val drawerLayout  = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun checkAccessLevel(uid : String) : Boolean{
        val doc : DocumentReference = fStore.collection("Users").document(uid)
        doc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.getBoolean("isAdmin") != false) {
                isAdminCheck = true
            }
        }
        return isAdminCheck
    }
}