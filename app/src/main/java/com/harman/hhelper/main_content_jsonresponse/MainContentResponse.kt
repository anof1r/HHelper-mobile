package com.harman.hhelper.main_content_jsonresponse

import com.google.gson.Gson

import java.net.URL

class MainContentResponse {

    var contentArray : ArrayList<MainContent> = ArrayList()
    fun getMainContent() : ArrayList<MainContent>  {
        var response = URL("http://192.168.0.6:8080/main_content").readText()
        var gson = Gson()
        val contentData = gson.fromJson(response, Array<MainContent>::class.java)
        for (it in contentData.indices){
            var content = MainContent("",1,"",1,"","")
            content.title = contentData[it].title
            content.content = contentData[it].content
            content.date = contentData[it].date
            content.homeWork = contentData[it].homeWork
            contentArray.add(content)
        }
        return contentArray
    }

    fun getContent() : String {
        return contentArray[contentArray.lastIndex].content
    }
    fun getTitle(): String {
        return contentArray[contentArray.lastIndex].title
    }
    fun getId(): Int {
        return contentArray[contentArray.lastIndex].id
    }
    fun getImgId(): Int {
        return contentArray[contentArray.lastIndex].imageId
    }
    fun getDate(): String {
        return contentArray[contentArray.lastIndex].date
    }
    fun getHw(): String {
        return contentArray[contentArray.lastIndex].homeWork
    }

}