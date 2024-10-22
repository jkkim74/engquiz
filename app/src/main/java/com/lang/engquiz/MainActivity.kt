package com.lang.engquiz

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var quizTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var resultTextView: TextView

    private var currentQuiz: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quizTextView = findViewById(R.id.quizTextView)
        answerEditText = findViewById(R.id.answerEditText)
        submitButton = findViewById(R.id.submitButton)
        resultTextView = findViewById(R.id.resultTextView)

        // 퀴즈를 가져옴
        fetchQuiz()

        // 답변 제출 버튼 클릭 시
        submitButton.setOnClickListener {
            val userAnswer = answerEditText.text.toString()
            checkAnswer(userAnswer)
        }
    }

    private fun fetchQuiz() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.getQuiz()
            if (response.isSuccessful) {
                currentQuiz = response.body() ?: ""
                withContext(Dispatchers.Main) {
                    quizTextView.text = currentQuiz
                }
            }
        }
    }

    private fun checkAnswer(answer: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.checkAnswer(currentQuiz, answer)
            if (response.isSuccessful) {
                val result = response.body() ?: ""
                withContext(Dispatchers.Main) {
                    resultTextView.text = result
                    fetchQuiz()  // 새로운 퀴즈 가져오기
                }
            }
        }
    }
}
