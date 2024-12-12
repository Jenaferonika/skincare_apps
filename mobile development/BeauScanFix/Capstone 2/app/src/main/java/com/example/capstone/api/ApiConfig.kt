package com.example.capstone.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.capstone.BuildConfig
import com.google.gson.GsonBuilder
import java.io.IOException

class ApiConfig {
    companion object {
        private var retrofitDefault: Retrofit? = null
        private var retrofitProcessImage: Retrofit? = null

        fun getDefaultApiService(token: String): ApiService {
            if (retrofitDefault == null) {
                retrofitDefault = createRetrofit("https://capstone-project-442222.et.r.appspot.com", token)
            }
            return retrofitDefault!!.create(ApiService::class.java)
        }

        fun getProcessImageApiService(token: String): ApiService {
            if (retrofitProcessImage == null) {
                retrofitProcessImage = createRetrofit("http://35.202.103.204:8080", token)
            }
            return retrofitProcessImage!!.create(ApiService::class.java)
        }

        private fun createRetrofit(baseUrl: String, token: String): Retrofit {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val authInterceptor = Interceptor { chain ->
                val requestHeaders = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        if (token.isNotEmpty()) "Bearer $token" else ""
                    )
                    .build()

                val response = chain.proceed(requestHeaders)
                if (response.code == 401 || response.code == 403) {
                    throw IOException("Unauthorized or Forbidden")
                }
                response
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }
    }
}





