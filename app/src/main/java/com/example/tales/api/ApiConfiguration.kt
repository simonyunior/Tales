package com.example.tales.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfiguration {
    private var token: String? = null

    fun setToken(newToken: String) {
        token = newToken
    }

    fun getApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .method(original.method, original.body)

                if (token != null) {
                    requestBuilder.header("Authorization", "Bearer $token")
                    Log.d("ApiConfiguration", "Added Authorization header with token: $token")
                } else {
                    Log.d("ApiConfiguration", "Token is null")
                }

                val request = requestBuilder.build()
                Log.d("ApiConfiguration", "Request Headers: ${request.headers}")
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
