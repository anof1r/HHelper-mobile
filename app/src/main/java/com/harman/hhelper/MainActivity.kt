package com.harman.hhelper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.harman.hhelper.api.LectureJson
import com.harman.hhelper.api.LectureJsonItem
import com.harman.hhelper.information_response.InfoResponse
import com.harman.hhelper.information_response.Information
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://192.168.0.6:8080"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var adapter: RcViewAdapter? = null
    //private var mcResponse: MainContentResponse = MainContentResponse()
    private var information: Information = Information()
    private lateinit var contentArray: LectureJson
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var informationResponse: InfoResponse
    private var isAdminCheck: Boolean = false


    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val rcView: RecyclerView = findViewById(R.id.rcView)

        val list = LectureJson()
        //val loadingDialog = LoadingDialog(this)
        val refresh = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

        val fab: View = findViewById(R.id.fab_main)
        fab.setOnClickListener {
            addInfo()
        }

        rcView.hasFixedSize()
        rcView.layoutManager = LinearLayoutManager(this)
        adapter = RcViewAdapter(list, this)
        rcView.adapter = adapter

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        //loadingDialog.startLoadingDialog()
        GlobalScope.launch {
                contentArray = getCurrentData()
                informationResponse = information.getMainContent()
            runOnUiThread {
                adapter?.updateAdapter(contentArray)
                //loadingDialog.dismissDialog()
            }
        }
        refresh.setOnRefreshListener {
            GlobalScope.launch {
                contentArray.clear()
                contentArray = getCurrentData()
                runOnUiThread {
                    adapter?.updateAdapter(contentArray)
                    refresh.isRefreshing = false
                }
            }
        }
    }

    @DelicateCoroutinesApi
    private suspend fun getCurrentData(): LectureJson {
        lateinit var data : LectureJson
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
            val response = api.getLectures().awaitResponse()
            if (response.isSuccessful){
                data = response.body()!!
        }
        return data
    }

    @DelicateCoroutinesApi
    private suspend fun postCurrentData(item: LectureJsonItem) {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
        api.postLecture(item.content,item.date,item.homeWork,item.id,item.imageId,item.title).awaitResponse()
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
            }
            R.id.id_information -> {
                val intent = Intent(this@MainActivity, InfoLayout::class.java)
                intent.putExtra("links", information.getLinks())
                intent.putExtra("literature", information.getLinksL())
                intent.putExtra("schedule", information.getSchedule())
                startActivity(intent)
            }
            R.id.id_contacts -> {
            }
            R.id.night_mode -> {
            }
            R.id.id_sign_out -> {
                auth.signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            R.id.id_content_manager -> {
            }
        }
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun checkAccessLevel(uid: String): Boolean {
        val doc: DocumentReference = fStore.collection("Users").document(uid)
        doc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.getBoolean("isAdmin") != false) {
                isAdminCheck = true
            } else {
                val fab: View = findViewById(R.id.fab_main)
                fab.visibility = View.GONE
            }
        }
        return isAdminCheck
    }

    @DelicateCoroutinesApi
    private fun addInfo() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item, null)

        val addTitle = v.findViewById<EditText>(R.id.addTitle)
        val addLectureInfo = v.findViewById<EditText>(R.id.addContent)
        val addImg = v.findViewById<EditText>(R.id.addImage)
        val addDate = v.findViewById<EditText>(R.id.addDate)
        val addHw = v.findViewById<EditText>(R.id.addHomeWork)
        //val addId = v.findViewById<EditText>(R.id.addId)

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            val content = addLectureInfo.text.toString()
            val date = addDate.text.toString()
            val homeWork = addHw.text.toString()
            val imageId = addImg.text.toString()
            val title = addTitle.text.toString()
            //val id = addId.text.toString().toInt()
            val item = LectureJsonItem(content,date,homeWork,1,imageId,title)
            GlobalScope.launch {
                postCurrentData(item)
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
}