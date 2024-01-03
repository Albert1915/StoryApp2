package com.alz19.storyapp2.helper.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alz19.storyapp2.di.Injection
import com.alz19.storyapp2.repository.auth.AuthRepository
import com.alz19.storyapp2.repository.auth.AuthRepositoryImpl
import com.alz19.storyapp2.repository.story.StoryRepository
import com.alz19.storyapp2.repository.token.TokenRepository
import com.alz19.storyapp2.view.add.AddViewModel
import com.alz19.storyapp2.view.detail.DetailViewModel
import com.alz19.storyapp2.view.login.LoginViewModel
import com.alz19.storyapp2.view.main.MainViewModel
import com.alz19.storyapp2.view.maps.MapsViewModel
import com.alz19.storyapp2.view.register.RegisterViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository, tokenRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository, tokenRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(storyRepository, tokenRepository) as T
        } else if (modelClass.isAssignableFrom(AddViewModel::class.java)){
            return AddViewModel(storyRepository, tokenRepository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)){
            return MapsViewModel(storyRepository, tokenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideStoryRepository(context),
                    Injection.provideTokenRepository(context),
                    AuthRepositoryImpl(),
                )
            }.also { instance = it }
    }
}