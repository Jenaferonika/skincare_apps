package com.example.capstone

import android.util.Log
import com.example.capstone.User.UserModel
import com.example.capstone.User.UserPreference
import com.example.capstone.api.ApiConfig
import com.example.capstone.api.ApiService
import com.example.capstone.register.RegisterRequest
import com.example.capstone.login.LoginRequest
import com.example.capstone.login.LoginResponse
import com.example.capstone.profile.User
import com.example.capstone.register.RegisterResponse
import com.example.capstone.result.ProcessImageRequest
import com.example.capstone.result.SkincareResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class Repository private constructor(
    private val userPreference: UserPreference,
    private var authApi: ApiService,
    private var imageApi: ApiService
) {

    suspend fun authenticateUser(email: String, password: String): Flow<LoginResponse> = flow {
        try {
            val loginRequest = LoginRequest(email, password)
            val loginResponse = authApi.login(loginRequest)
            val userModel = UserModel(email, loginResponse.token, isLogin = true)
            storeUserSession(userModel)

            emit(loginResponse)
        } catch (exception: Exception) {
            throw exception
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userRegister(name: String, email: String, password: String): Flow<RegisterResponse> = flow {
        val registerRequest = RegisterRequest(name, email, password)
        val responseBody = authApi.register(registerRequest)
        val responseString = responseBody.string()

        val response = RegisterResponse(message = responseString)
        Log.d("RegisterRepository", "Response: $response")
        emit(response)
    }.catch { exception ->
        val errorMessage = when (exception) {
            is HttpException -> {
                val errorBody = exception.response()?.errorBody()?.string()
                "Pendaftaran gagal: ${errorBody ?: exception.message}"
            }
            is IOException -> "Error jaringan: ${exception.message}"
            else -> "Pendaftaran gagal: ${exception.message}"
        }
        Log.e("RegisterRepository", errorMessage)
        throw Exception(errorMessage)
    }.flowOn(Dispatchers.IO)

    suspend fun fetchUserProfile(token: String): User? {
        return withContext(Dispatchers.IO) {
            val response = authApi.getUsers(token)
            Log.d("Repository", "ProfileResponse: $response")
            response.user
        }
    }

    internal suspend fun storeUserSession(userData: UserModel) {
        userPreference.setUserSession(userData)
    }

    fun updateAuthApiService(apiService: ApiService) {
        this.authApi = apiService
    }

    fun updateImageApiService(apiService: ApiService) {
        this.imageApi = apiService
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            userPreference: UserPreference,
            authApi: ApiService,
            imageApi: ApiService
        ): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository(userPreference, authApi, imageApi).also { instance = it }
            }
        }
    }

    suspend fun loadUserProfile(token: String): User? {
        return withContext(Dispatchers.IO) {
            val response = authApi.getUsers(token)
            Log.d("Repository", "ProfileResponse: $response")
            response.user
        }
    }

    suspend fun getUserToken(): String? {
        return userPreference.getToken()
    }

}




