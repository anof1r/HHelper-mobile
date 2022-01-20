package com.harman.hhelper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.harman.hhelper.api.LectureJson
import com.harman.hhelper.information_response.InfoResponse
import com.harman.hhelper.information_response.Information
import com.harman.hhelper.main_content_jsonresponse.MainContent
import com.harman.hhelper.main_content_jsonresponse.MainContentResponse
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BASE_URL = "http://192.168.0.6:8080"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var adapter: RcViewAdapter? = null
    private var mcResponse: MainContentResponse = MainContentResponse()
    private var information: Information = Information()
    private lateinit var contentArray: LectureJson
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var informationResponse: InfoResponse
    private var isAdminCheck: Boolean = false
    private var TAG = "MainActivity"

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val rcView: RecyclerView = findViewById(R.id.rcView)

        val list = LectureJson()
        val loadingDialog = LoadingDialog(this)
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
        loadingDialog.startLoadingDialog()
        GlobalScope.launch {

                contentArray = getCurrentData()
                informationResponse = information.getMainContent()
            runOnUiThread() {
                adapter?.updateAdapter(contentArray)
                loadingDialog.dismissDialog()
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
                //val frag = Fragment(R.layout.fragment_info_f_fragment)
                //loadFragment(frag)
            }
            R.id.night_mode -> {
            }
            R.id.id_sign_out -> {
                auth.signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            R.id.id_content_manager -> {
                if (isAdminCheck) {
                    Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, ContentManager::class.java)
                    //intent.putExtra("url", "http://95.79.178.246:8080/web_view")
                    intent.putExtra("url", "http://192.168.0.6:8080/web_view")
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "You're not allowed to use ContentManager",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    private fun addInfo() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item, null)

        //val userName = v.findViewById<EditText>(R.id.userName)
        //val userNo = v.findViewById<EditText>(R.id.userNo)

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            //val names = userName.text.toString()
            //val number = userNo.text.toString()
            //userList.add(UserData("Name: $names","Mobile No. : $number"))
            //userAdapter.notifyDataSetChanged()
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