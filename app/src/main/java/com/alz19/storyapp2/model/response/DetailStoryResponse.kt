package com.alz19.storyapp2.model.response

import com.google.gson.annotations.SerializedName
import com.alz19.storyapp2.model.entity.ListStoryItem

data class DetailStoryResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("story")
    val story: ListStoryItem? = null
)