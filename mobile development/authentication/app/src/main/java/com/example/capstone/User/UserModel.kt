package com.example.capstone.User

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
)