package com.harman.hhelper

import com.harman.hhelper.api.LectureJson
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {
    @GET("/main_content")
    fun getLectures(): Call<LectureJson>
}