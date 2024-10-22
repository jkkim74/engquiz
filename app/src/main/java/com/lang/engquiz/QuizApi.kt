package com.lang.engquiz

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizApi {

    @GET("/api/chatgpt/quiz")
    suspend fun getQuiz(): Response<String>

    @POST("/api/quiz/check")
    suspend fun checkAnswer(@Query("quiz") quiz: String, @Query("answer") answer: String): Response<String>
}
