package com.example.capstone.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val message: String
)

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
