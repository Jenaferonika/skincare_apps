package com.example.capstone.home

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.capstone.Repository
import com.example.capstone.api.ApiConfig
import com.example.capstone.result.SkincareResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _results = MutableLiveData<SkincareResponse>()
    val results: LiveData<SkincareResponse> get() = _results

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun analyzeImage(filePath: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apiService = ApiConfig.getProcessImageApiService("your_token_here")
                val file = File(filePath)

                if (file.exists()) {
                    val photoPart = file.asMultipartBody("image")
                    val response = apiService.processImage(photoPart)

                    if (response.isSuccessful) {
                        Log.d("HomeViewModel", "Response: ${response.body()}")
                        response.body()?.let { responseBody ->
                            _results.value = responseBody
                        } ?: run {
                            Log.e("HomeViewModel", "Response body is null")
                        }
                    } else {
                        Log.e("HomeViewModel", "Failed to analyze image: ${response.errorBody()?.string()}")
                    }
                } else {
                    Log.e("HomeViewModel", "File not found at path: $filePath")
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                Log.e("HomeViewModel", "Error analyzing image: ${exception.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun convertFileToBase64(file: File): String {
        val bytes = file.readBytes()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun File.asMultipartBody(name: String): MultipartBody.Part {
        val requestFile = this.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, this.name, requestFile)
    }

}

