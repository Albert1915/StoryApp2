package com.alz19.storyapp2.repository.auth

import com.alz19.storyapp2.helper.utils.Result
import com.alz19.storyapp2.model.response.BasicResponse
import com.alz19.storyapp2.model.response.LoginResponse

interface AuthRepository {
    suspend fun loginUser(email:String, password :String) : Result<LoginResponse>
    suspend fun registerUser(name:String, email: String, password: String) : Result<BasicResponse>

}