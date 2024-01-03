package com.alz19.storyapp2.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.model.response.BasicResponse
import com.alz19.storyapp2.repository.auth.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _response = MutableLiveData<Result<BasicResponse>>()
    val response: LiveData<Result<BasicResponse>>
        get() = _response

    init {
        _response.value = Result.Loading(false)
    }

    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            _response.value = Result.Loading(true)
            _response.value = authRepository.registerUser(name, email, password)
        }
    }


}