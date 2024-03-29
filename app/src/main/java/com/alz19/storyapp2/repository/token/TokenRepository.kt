package com.alz19.storyapp2.repository.token

interface TokenRepository {
    suspend fun saveSession(token: String)
    fun getSession() :String
    suspend fun logout()
}