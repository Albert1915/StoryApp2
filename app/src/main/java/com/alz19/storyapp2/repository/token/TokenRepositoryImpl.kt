package com.alz19.storyapp2.repository.token

import com.alz19.storyapp2.helper.preference.TokenPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TokenRepositoryImpl private constructor(
    private val pref: TokenPreference,
) : TokenRepository {
    override suspend fun saveSession(token: String) {
        pref.setToken(token)
    }

    override fun getSession(): String {
        return runBlocking { pref.getToken().first() }
    }

    override suspend fun logout() {
        pref.logout()
    }

    companion object {
        private var instance: TokenRepositoryImpl? = null

        fun getInstance(
            pref: TokenPreference,
        ): TokenRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: TokenRepositoryImpl(pref)
            }.also { instance = it }
        }
    }

}