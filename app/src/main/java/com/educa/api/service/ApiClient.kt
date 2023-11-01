package com.educa.api.service

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var mainApiService: MainApiService

    // var BASE_URL = "http://localhost:80/"
    var BASE_URL = "https://educaback.hopto.org/"

    fun getAuthApiService(context: Context): AuthApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okhttpClient(context))
            .build()
        return retrofit.create(AuthApiService::class.java)
    }

    fun getMainApiService(context: Context): MainApiService {
        if (!::mainApiService.isInitialized) {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okhttpClient(context))
                .build()
            mainApiService = retrofit.create(MainApiService::class.java)
        }
        return mainApiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }
}