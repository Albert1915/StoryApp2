package com.alz19.storyapp2.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.helper.utils.wrapEspressoIdlingResource
import com.alz19.storyapp2.model.response.LoginResponse
import com.alz19.storyapp2.repository.auth.AuthRepository
import com.alz19.storyapp2.repository.token.TokenRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
) :
    ViewModel() {

    private val _user = MutableLiveData<Result<LoginResponse>>()
    val user: LiveData<Result<LoginResponse>>
        get() = _user

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    init {
        _user.value = Result.Loading(false)
    }

    fun loginUser(email: String, password: String) {
        wrapEspressoIdlingResource {
            viewModelScope.launch {
                _user.value = Result.Loading(true)
                _user.value = authRepository.loginUser(email, password)
            }
        }
    }

    fun saveSession(token: String) {
        _token.value = token
        viewModelScope.launch {
            tokenRepository.saveSession(token)
        }
    }
}