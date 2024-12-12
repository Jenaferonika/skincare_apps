package com.example.capstone

import com.example.capstone.User.UserModel
import com.example.capstone.User.UserPreference
import com.example.capstone.api.ApiService
import com.example.capstone.login.LoginResponse
import com.example.capstone.register.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository private constructor(
    private val userPreference: UserPreference,
    private var api: ApiService
) {
    fun authenticateUser(email: String, password: String): Flow<LoginResponse> = flow {
        try {
            val loginResponse = api.login(email, password)
            storeUserSession(UserModel(email, loginResponse.loginResult.token, isLogin = true))
            emit(loginResponse)
        } catch (exception: Exception) {
            throw exception
        }
    }.flowOn(Dispatchers.IO)

    fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<RegisterResponse> = flow {
        try {
            val registrationResult = api.register(name, email, password)
            emit(registrationResult)
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(RegisterResponse(error = true, message = exception.message ?: "Registration failed"))
        }
    }.flowOn(Dispatchers.IO)

    internal suspend fun storeUserSession(userData: UserModel) {
        userPreference.setUserSession(userData)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun updateApiService(apiService: ApiService) {
        this.api = apiService
    }

    companion object {
        @Volatile
        private var sharedInstance: Repository? = null

        fun getInstance(preferences: UserPreference, api: ApiService): Repository =
            sharedInstance ?: synchronized(this) {
                sharedInstance ?: Repository(preferences, api).also { sharedInstance = it }
            }
    }
}
