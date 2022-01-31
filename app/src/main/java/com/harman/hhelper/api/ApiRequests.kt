package com.harman.hhelper.api

import com.harman.hhelper.contatcts.ContactsJson
import com.harman.hhelper.information_response.InfoResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiRequests {

    @GET("/main_content")
    fun getLectures(): Call<LectureJson>

    @GET("/information")
    fun getInfo(): Call<InfoResponse>

    @GET("/get_contacts")
    fun getContacts(): Call<ContactsJson>

    @FormUrlEncoded
    @POST("/addLecture")
    fun postLecture(
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("hw") homeWork: String,
        @Field("id") id: Int,
        @Field("imgId") ImageId: String,
        @Field("title") title: String
    ): Call<LectureJson>

    @FormUrlEncoded
    @POST("/deleteLecture")
    fun deleteLecture(@Field("id") id: Int): Call<Unit>

    @FormUrlEncoded
    @POST("/editLecture")
    fun editLecture(
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("hw") homeWork: String,
        @Field("id") id: Int,
        @Field("imgId") ImageId: String,
        @Field("title") title: String
    ): Call<Unit>

    @FormUrlEncoded
    @POST("/updateInfo")
    fun updateInfo(
        @Field("literature") literature: String,
        @Field("schedule") schedule: String,
        @Field("links") links: String
    ): Call<Unit>
}