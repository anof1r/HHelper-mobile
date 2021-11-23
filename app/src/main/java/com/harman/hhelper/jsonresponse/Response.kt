package com.harman.hhelper.jsonresponse

import com.google.gson.annotations.SerializedName

data class Response(


	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("secondName")
	val secondName: String? = null
)
