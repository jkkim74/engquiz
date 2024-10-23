package com.lang.engquiz

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
val gson = GsonBuilder().setLenient().create()

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            //.baseUrl("http://210.97.98.164:8080")  // Spring Boot 서버 IP 주소
            .baseUrl("http://192.168.1.124:8080")  // Spring Boot 서버 IP 주소
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: QuizApi by lazy {
        retrofit.create(QuizApi::class.java)
    }
}
