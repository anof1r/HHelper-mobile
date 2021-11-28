package com.harman.hhelper.main_content_jsonresponse

import com.google.gson.annotations.SerializedName

data class ResponseMC(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("imageId")
	val imageId: Int? = null,

	@field:SerializedName("homeWork")
	val homeWork: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
