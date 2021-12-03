package com.harman.hhelper.main_content_jsonresponse

import com.google.gson.annotations.SerializedName

data class MainContent(

    @field:SerializedName("date")
    var date: String,

    @field:SerializedName("imageId")
    var imageId: String,

    @field:SerializedName("homeWork")
    var homeWork: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    var title: String,

    @field:SerializedName("content")
    var content: String
)