package com.lang.engquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.lang.engquiz.network.ChatRequest
import com.lang.engquiz.network.Message
import com.lang.engquiz.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var quizTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var resultTextView: TextView

    private var currentQuestion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI 요소 연결
        quizTextView = findViewById(R.id.quizTextView)
        answerEditText = findViewById(R.id.answerEditText)
        submitButton = findViewById(R.id.submitButton)
        resultTextView = findViewById(R.id.resultTextView)

        // 처음에 퀴즈 생성 요청
        generateQuiz()

        // 정답 제출 버튼 클릭 이벤트
        submitButton.setOnClickListener {
            val userAnswer = answerEditText.text.toString()
            checkAnswerWithChatGPT(userAnswer)
        }
    }

    // ChatGPT API를 사용하여 새로운 퀴즈를 생성하는 함수
    private fun generateQuiz() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // ChatGPT에게 퀴즈 생성 요청 (유아용 간단한 퀴즈)
                val message = Message(
                    "user",
                    "Please create a simple quiz question suitable for a preschool child."
                )
                val request = ChatRequest(messages = listOf(message))

                // API 호출
                val response = RetrofitInstance.api.getChatResponse(request)
                currentQuestion = response.choices[0].message.content

                // UI 업데이트
                runOnUiThread {
                    quizTextView.text = currentQuestion
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ChatGPT API를 사용하여 정답을 확인하는 함수
    private fun checkAnswerWithChatGPT(userAnswer: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // ChatGPT에게 질문과 답변을 확인하도록 요청
                val message = Message(
                    "user",
                    "The quiz question is: '$currentQuestion'. The child's answer is: '$userAnswer'. Is the answer correct for a preschool child?"
                )
                val request = ChatRequest(messages = listOf(message))

                // API 호출 및 응답 처리
                val response = RetrofitInstance.api.getChatResponse(request)
                val result = response.choices[0].message.content

                // UI 업데이트
                runOnUiThread {
                    resultTextView.text = "ChatGPT says: $result"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
