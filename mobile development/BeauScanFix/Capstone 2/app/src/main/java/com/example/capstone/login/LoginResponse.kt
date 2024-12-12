package com.example.capstone.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String
)

data class LoginRequest(
    val email: String,
    val password: String
)
