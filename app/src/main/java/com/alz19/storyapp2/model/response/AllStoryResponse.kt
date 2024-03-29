package com.alz19.storyapp2.model.response

import com.google.gson.annotations.SerializedName
import com.alz19.storyapp2.model.entity.ListStoryItem

data class AllStoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem?>? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)