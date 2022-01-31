package com.harman.hhelper.information_response

import com.google.gson.annotations.SerializedName

data class InfoResponse(

	@field:SerializedName("literature")
	var literature: List<String>,

	@field:SerializedName("schedule")
	var schedule: String,

	@field:SerializedName("links")
	var links: List<LinksItem>
)
data class LinksItem(

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("href")
	val href: String
)


