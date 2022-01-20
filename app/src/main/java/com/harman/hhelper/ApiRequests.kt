package com.harman.hhelper

import com.harman.hhelper.api.LectureJson
import com.harman.hhelper.api.LectureJsonItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiRequests {

    @GET("/main_content")
    fun getLectures(): Call<LectureJson>

    @FormUrlEncoded
    @POST("/addLecture")
    fun postLecture(@Field("content") content: String,
                    @Field("date") date: String,
                    @Field("hw") homeWork: String,
                    @Field("id") id: Int,
                    @Field("imgId") ImageId: String,
                    @Field("title") title: String) : Call<LectureJson>
}