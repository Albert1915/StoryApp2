package com.alz19.storyapp2.repository.story

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.alz19.storyapp2.data.story.StoryDatabase
import com.alz19.storyapp2.data.story.StoryRemoteMediator
import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.model.entity.ListStoryItem
import com.alz19.storyapp2.model.response.AllStoryResponse
import com.alz19.storyapp2.model.response.BasicResponse
import com.alz19.storyapp2.model.response.DetailStoryResponse
import com.alz19.storyapp2.provider.config.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StoryRepositoryImpl (private val storyDatabase: StoryDatabase) : StoryRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    override suspend fun getDetailStory(token: String, id: String): Result<DetailStoryResponse> {
        return suspendCoroutine { continuation ->
            var detailStoryResult: Result<DetailStoryResponse>
            val client = ApiConfig.getApiService().getDetailStory("Bearer $token", id)
            client.enqueue(
                object : Callback<DetailStoryResponse> {
                    override fun onResponse(call: Call<DetailStoryResponse>, response: Response<DetailStoryResponse>) {
                        if (response.isSuccessful) {
                            detailStoryResult = Result.Success(response.body() as DetailStoryResponse)
                            continuation.resume(detailStoryResult)
                            return
                        }
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        detailStoryResult = Result.Failure(jsonObj.getString("message") ?: response.message())
                        continuation.resume(detailStoryResult)
                    }

                    override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                        detailStoryResult = Result.Failure(t.toString())
                        continuation.resume(detailStoryResult)

                    }

                }
            )
        }
    }

    override suspend fun postMultiPart(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): Result<BasicResponse> {
        return suspendCoroutine { continuation ->
            var postStoryResult: Result<BasicResponse>
            val client = ApiConfig.getApiService().postStoriesData("Bearer $token", file, description, lat, lon)
            client.enqueue(
                object : Callback<BasicResponse> {
                    override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                        if (response.isSuccessful) {
                            postStoryResult = Result.Success(response.body() as BasicResponse)
                            continuation.resume(postStoryResult)
                            return
                        }
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        postStoryResult = Result.Failure(jsonObj.getString("message") ?: response.message())
                        continuation.resume(postStoryResult)
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        postStoryResult = Result.Failure(t.toString())
                        continuation.resume(postStoryResult)
                    }

                }
            )
        }
    }

    override suspend fun getAllStoryLocation(token: String): Result<AllStoryResponse> {
        return suspendCoroutine { continuation ->
            var allStoryResult: Result<AllStoryResponse>
            val client = ApiConfig.getApiService().getAllStoryLocation("Bearer $token")
            client.enqueue(
                object : Callback<AllStoryResponse> {
                    override fun onResponse(call: Call<AllStoryResponse>, response: Response<AllStoryResponse>) {
                        if (response.isSuccessful) {
                            allStoryResult = Result.Success(response.body() as AllStoryResponse)
                            continuation.resume(allStoryResult)
                            return
                        }
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        allStoryResult = Result.Failure(jsonObj.getString("message") ?: response.message())
                        continuation.resume(allStoryResult)
                    }

                    override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                        allStoryResult = Result.Failure(t.toString())
                        continuation.resume(allStoryResult)

                    }

                }
            )
        }
    }


}