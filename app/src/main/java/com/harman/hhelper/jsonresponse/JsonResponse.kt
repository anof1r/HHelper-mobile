package com.harman.hhelper.jsonresponse

import com.google.gson.Gson
import com.harman.hhelper.User
import java.net.URL

class JsonResponse {
    var user : User = User(0," "," ")
//fun main(args: Array<String>){
    fun jsonResponse() : ArrayList<User> {
    var response = URL("http://192.168.0.6:8080/getJson2").readText()
    var gson = Gson()
    val data = gson.fromJson(response, Array<Response>::class.java)
        var listOfUsers : ArrayList<User> = ArrayList()
        for (it in data.indices){
            var tempList = User(data[it].id!!,data[it].firstName!!,data[it].secondName!!)
            //user.id = data[it].id!!
            //user.firstName = data[it].firstName.toString()
            //user.secondName = data[it].secondName.toString()
            listOfUsers.add(tempList)
        }
    return listOfUsers
    }
    fun getId(): Int{
        return user.id
    }
    fun getFirstName(): String{
        return user.firstName
    }
    fun getSecondName(): String{
        return user.secondName
    }
}