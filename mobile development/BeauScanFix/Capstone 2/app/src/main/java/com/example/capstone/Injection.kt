package com.example.capstone

import android.content.Context
import com.example.capstone.User.UserPreference
import com.example.capstone.User.dataStore
import com.example.capstone.api.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }

        val authApiService = ApiConfig.getDefaultApiService(user.token)
        val imageApiService = ApiConfig.getProcessImageApiService(user.token)

        return Repository.getInstance(pref, authApiService, imageApiService)
    }
}
