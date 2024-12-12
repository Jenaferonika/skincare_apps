package com.example.capstone.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("user")
    val user: User
)

data class User(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("public_id")
    val publicId: String
)


