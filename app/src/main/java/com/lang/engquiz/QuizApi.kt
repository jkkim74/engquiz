package com.lang.engquiz

import com.lang.engquiz.model.QuizResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizApi {

    @GET("/api/chatgpt/generate")
    suspend fun getQuiz(): Response<QuizResponse>

    @POST("/api/chatgpt/check")
    suspend fun checkAnswer(@Query("quiz") quiz: String, @Query("answer") answer: String): Response<QuizResponse>
}
