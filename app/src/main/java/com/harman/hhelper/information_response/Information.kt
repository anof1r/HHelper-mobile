package com.harman.hhelper.information_response

import androidx.core.net.toUri
import com.google.gson.Gson
import java.net.URL

class Information {

    lateinit var infoResponse : InfoResponse

    fun getMainContent(): InfoResponse {
        val response = URL("http://192.168.0.4:8080/information").readText()
        val gson = Gson()
        infoResponse = gson.fromJson(response, InfoResponse::class.java)
        println(infoResponse.links.toString())
        return infoResponse
    }

    fun getLinks(): String {
        //return infoResponse.links.joinToString{
            //it.href
            //it.description
        return infoResponse.links.joinToString(""){
            it.description + "\n" + it.href + "\n"
        }
        }
    fun getLinksL(): String {
        return infoResponse.literature.joinToString("\n")
    }
    fun getSchedule(): String {
        return infoResponse.schedule
    }
}
