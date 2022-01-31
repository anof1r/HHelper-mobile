package com.harman.hhelper.information_response

import com.harman.hhelper.api.ApiRequests
import com.harman.hhelper.ui.BASE_URL
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class Information {

    lateinit var data: InfoResponse

    @DelicateCoroutinesApi
    suspend fun getInformation(): InfoResponse {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
        val response = api.getInfo().awaitResponse()
        if (response.isSuccessful) {
            data = response.body()!!
        }
        return data
    }

    fun getLinks(): String {
        return data.links.joinToString("") {
            it.description + "\n" + it.href + "\n"
        }
    }

    fun getLinksL(): String {
        return data.literature.joinToString("\n")
    }

    fun getSchedule(): String {
        return data.schedule
    }
}
