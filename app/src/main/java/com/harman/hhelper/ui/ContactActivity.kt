package com.harman.hhelper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harman.hhelper.ContactAdapter
import com.harman.hhelper.NetworkConnection
import com.harman.hhelper.R
import com.harman.hhelper.api.ApiRequests
import com.harman.hhelper.contatcts.ContactsJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class ContactActivity : AppCompatActivity() {

    private var adapter: ContactAdapter? = null
    private lateinit var contactsArray: ContactsJson

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contacts_list)

        val list = ContactsJson()

        val rcView: RecyclerView = findViewById(R.id.contactRcView)

        rcView.hasFixedSize()
        rcView.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(list, this)
        rcView.adapter = adapter


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, { isConnected ->
            if (isConnected) {
                CoroutineScope(Dispatchers.IO).launch {
                    contactsArray = getContacts()
                    runOnUiThread {
                        adapter?.updateAdapter(contactsArray)
                    }
                }
            }
        })
    }

    @DelicateCoroutinesApi
    suspend fun getContacts(): ContactsJson {
        lateinit var data: ContactsJson
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
        val response = api.getContacts().awaitResponse()
        if (response.isSuccessful) {
            data = response.body()!!
        }
        return data
    }
}