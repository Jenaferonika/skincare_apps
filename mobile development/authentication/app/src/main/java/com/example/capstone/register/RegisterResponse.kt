package com.example.capstone.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any? = null
)
