package com.example.supletanes.data.network

import com.example.supletanes.model.LoginRequest
import com.example.supletanes.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}