package com.alz19.storyapp2.repository.story

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.model.entity.ListStoryItem
import com.alz19.storyapp2.model.response.AllStoryResponse
import com.alz19.storyapp2.model.response.BasicResponse
import com.alz19.storyapp2.model.response.DetailStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepository {

    suspend fun getDetailStory(token: String, id: String): Result<DetailStoryResponse>

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>>
    suspend fun postMultiPart(
        token: String, file: MultipartBody.Part, description: RequestBody, lat: Double? = null, lon: Double? = null
    ): Result<BasicResponse>

    suspend fun getAllStoryLocation(token: String): Result<AllStoryResponse>
}