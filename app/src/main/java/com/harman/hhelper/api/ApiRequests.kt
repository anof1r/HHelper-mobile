package com.harman.hhelper.api

import retrofit2.Call
import retrofit2.http.*

interface ApiRequests {

    @GET("/main_content")
    fun getLectures(): Call<LectureJson>

    @FormUrlEncoded
    @POST("/addLecture")
    fun postLecture(
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("hw") homeWork: String,
        @Field("id") id : Int,
        @Field("imgId") ImageId: String,
        @Field("title") title: String
    ) : Call<LectureJson>

    @FormUrlEncoded
    @POST("/deleteLecture")
    fun deleteLecture(@Field("id") id: Int) : Call<Unit>

    @FormUrlEncoded
    @POST("/editLecture")
    fun editLecture(@Field("content") content: String,
                    @Field("date") date: String,
                    @Field("hw") homeWork: String,
                    @Field("id") id : Int,
                    @Field("imgId") ImageId: String,
                    @Field("title") title: String) : Call<Unit>
}