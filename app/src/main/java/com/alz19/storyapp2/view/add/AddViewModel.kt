package com.alz19.storyapp2.view.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.model.response.BasicResponse
import com.alz19.storyapp2.repository.story.StoryRepository
import com.alz19.storyapp2.repository.token.TokenRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddViewModel(
    private val storyRepository: StoryRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _response = MutableLiveData<Result<BasicResponse>>()
    val response: LiveData<Result<BasicResponse>>
        get() = _response

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    init {
        _response.value = Result.Loading(false)
    }

    fun getSession() {
        _token.value = tokenRepository.getSession()
    }

    fun postMultiPart(
        file: MultipartBody.Part, description: RequestBody, lat: Double? = null, lon: Double? = null
    ) {
        viewModelScope.launch {
            _response.value = Result.Loading(true)
            val temp = storyRepository.postMultiPart(tokenRepository.getSession(), file, description, lat, lon)
            _response.value = Result.Loading(false)
            _response.value = temp
        }
    }

}