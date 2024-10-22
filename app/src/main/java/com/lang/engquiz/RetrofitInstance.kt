package com.lang.engquiz

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.100:8080")  // Spring Boot 서버 IP 주소
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: QuizApi by lazy {
        retrofit.create(QuizApi::class.java)
    }
}
