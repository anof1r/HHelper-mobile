package com.harman.hhelper.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.harman.hhelper.LoadingDialog
import com.harman.hhelper.api.ApiRequests
import com.harman.hhelper.R
import com.harman.hhelper.RcViewAdapter
import com.harman.hhelper.api.LectureJson
import com.harman.hhelper.api.LectureJsonItem
import com.harman.hhelper.information_response.InfoResponse
import com.harman.hhelper.information_response.Information
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import com.harman.hhelper.NetworkConnection


const val BASE_URL = "http://192.168.0.6:8080"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var adapter: RcViewAdapter? = null
    private var information: Information = Information()
    private lateinit var contentArray: LectureJson
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var informationResponse: InfoResponse
    private var isAdminCheck: Boolean = false
    private lateinit var snackbarView : ConstraintLayout
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
        snackbarView = findViewById(R.id.main_content_layout)
        val fab: View = findViewById(R.id.fab_main)
        fab.setOnClickListener {
            addLecture()
        }
        loadingDialog.startLoading()
        rcView.hasFixedSize()
        rcView.layoutManager = LinearLayoutManager(this)
        adapter = RcViewAdapter(list, this)
        rcView.adapter = adapter

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, { isConnected ->
            if (isConnected){
                CoroutineScope(Dispatchers.IO).launch{
                    contentArray = getCurrentData()
                    informationResponse = information.getMainContent()
                    runOnUiThread {
                        adapter?.updateAdapter(contentArray)
                        loadingDialog.isDismiss()
                    }
                }

                refresh.setOnRefreshListener {
                    CoroutineScope(Dispatchers.IO).launch{
                        contentArray.clear()
                        contentArray = getCurrentData()
                        runOnUiThread {
                            adapter?.updateAdapter(contentArray)
                            refresh.isRefreshing = false
                        }
                    }
                }
            } else {
                Snackbar.make(snackbarView, "You are not connected to the internet", Snackbar.LENGTH_LONG).show()
                refresh.setOnRefreshListener {
                    Snackbar.make(snackbarView, "You are not connected to the internet", Snackbar.LENGTH_LONG).show()
                    refresh.isRefreshing = false
                }
            }
         })
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
    suspend fun postCurrentData(item: LectureJsonItem) {
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

    @SuppressLint("SimpleDateFormat")
    @DelicateCoroutinesApi
    private fun addLecture() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item, null)

        val format = "dd-MM-yyyy"
        val addTitle = v.findViewById<EditText>(R.id.addTitle)
        val addLectureInfo = v.findViewById<EditText>(R.id.addContent)
        val addImg = v.findViewById<EditText>(R.id.addImage)
        val addDate = v.findViewById<TextView>(R.id.addDate)

        addDate.text = SimpleDateFormat(format).format(System.currentTimeMillis())
            val calendar = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat(format, Locale.FRANCE)
                addDate.text = sdf.format(calendar.time)
            }
        addDate.setOnClickListener{
            DatePickerDialog(this@MainActivity, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val addHw = v.findViewById<EditText>(R.id.addHomeWork)
        //val addId = v.findViewById<EditText>(R.id.addId)

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Add") { dialog, _ ->
            val content = addLectureInfo.text.toString()
            val date = addDate.text.toString()
            val homeWork = addHw.text.toString()
            val imageId = addImg.text.toString()
            val title = addTitle.text.toString()
            val item = LectureJsonItem(content,date,homeWork,(date + content).hashCode(),imageId,title)
            CoroutineScope(Dispatchers.IO).launch {
                postCurrentData(item)
                contentArray = getCurrentData()
                runOnUiThread{
                    adapter?.updateAdapter(contentArray)
                }
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
    override fun onResume() {
        super.onResume()
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, { isConnected ->
            if (isConnected){
                CoroutineScope(Dispatchers.IO).launch{
                    contentArray = getCurrentData()
                    //informationResponse = information.getMainContent()
                    runOnUiThread {
                        adapter?.updateAdapter(contentArray)
                    }
                }

                CoroutineScope(Dispatchers.IO).launch{
                    contentArray = getCurrentData()
                    informationResponse = information.getMainContent()
                    runOnUiThread {
                        adapter?.updateAdapter(contentArray)
                        //loadingDialog.dismissDialog()
                    }
                }
            }
        })
    }
}