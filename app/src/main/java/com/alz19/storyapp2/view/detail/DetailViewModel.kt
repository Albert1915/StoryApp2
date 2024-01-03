package com.alz19.storyapp2.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.model.response.DetailStoryResponse
import com.alz19.storyapp2.repository.story.StoryRepository
import com.alz19.storyapp2.repository.token.TokenRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val storyRepository: StoryRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    private val _detailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val detailStory: LiveData<Result<DetailStoryResponse>>
        get() = _detailStory

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getSession() {
        _token.value = tokenRepository.getSession()
    }

    fun getDetailStory (id:String){
        viewModelScope.launch {
            _detailStory.value = Result.Loading(true)
            val temp = storyRepository.getDetailStory(tokenRepository.getSession(), id)
            _detailStory.value = Result.Loading(false)
            _detailStory.value = temp
        }
    }

}