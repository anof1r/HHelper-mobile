package com.harman.hhelper.main_content_jsonresponse

import com.google.gson.Gson

import java.net.URL

class MainContentResponse {

    var content : MainContent = MainContent(1,1,"","","","")
    var contentArray : ArrayList<MainContent> = ArrayList()
    //fun main(args: Array<String>){
    fun getMainContent() : ArrayList<MainContent>  {
        var response = URL("http://192.168.0.6:8080/main_content").readText()
        var gson = Gson()
        val contentData = gson.fromJson(response, Array<ResponseMC>::class.java)
        for (it in contentData.indices){
            content.title = contentData[it].title!!
            content.content = contentData[it].content!!
            content.date = contentData[it].date!!
            content.homeWork = contentData[it].homeWork!!
            contentArray.add(content)
        }
        return contentArray
    }

    fun getContent() : String{
        return content.content
    }
    fun getTitle(): String{
        return content.title
    }
    fun getId(): Int{
        return content.id
    }
    fun getImgId(): Int{
        return content.imageId
    }
    fun getDate(): String{
        return content.date
    }
    fun getHw(): String{
        return content.homeWork
    }

}