package com.lang.engquiz.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST



// ChatGPT API 요청을 위한 데이터 모델
data class ChatRequest(
    val model: String = "gpt-4",  // 사용할 모델
    val messages: List<Message>,
    val max_tokens: Int = 50,
    val temperature: Float = 0.5f
)

data class Message(
    val role: String,  // "user" 또는 "assistant"
    val content: String
)

// ChatGPT API 응답을 위한 데이터 모델
data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

// Retrofit 인터페이스 정의
interface OpenAiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun getChatResponse(@Body request: ChatRequest): ChatResponse
}

// Retrofit 인스턴스 설정
object RetrofitInstance {
    private const val BASE_URL = "https://api.openai.com/"
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
//                .addHeader("Authorization", "Bearer ")  // OpenAI API 키 입력
                .build()
            chain.proceed(request)
        }.build()

    val api: OpenAiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAiService::class.java)
    }
}

