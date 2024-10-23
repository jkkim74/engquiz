package com.lang.engquiz

import android.os.Bundle
import android.util.Log
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
    private val TAG = "QuizActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quizTextView = findViewById(R.id.quizTextView)
        answerEditText = findViewById(R.id.answerEditText)
        submitButton = findViewById(R.id.submitButton)
        resultTextView = findViewById(R.id.resultTextView)
        Log.d(TAG,"############## fetchQuiz started before#######################")
        // 퀴즈를 가져옴
        fetchQuiz()

        // 답변 제출 버튼 클릭 시
        submitButton.setOnClickListener {
            val userAnswer = answerEditText.text.toString()
            checkAnswer(userAnswer)
        }
    }

    private fun fetchQuiz() {
        Log.d(TAG,"fetchQuiz started")
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG,"fetchQuiz started_2")
            val response = RetrofitInstance.api.getQuiz()
            Log.d(TAG,"fetchQuiz getQuiz : ${response.isSuccessful}")
            Log.d(TAG,"fetchQuiz getQuiz body : ${response.body()}")
            if (response.isSuccessful) {
                currentQuiz = response.body()?.question ?: "Empty response"
                withContext(Dispatchers.Main) {
                    quizTextView.text = currentQuiz
                }
            }
        }
    }

    private fun checkAnswer(answer: String) {
        Log.d(TAG, "this quiz's answer is $answer")
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.checkAnswer(currentQuiz, answer)
            if (response.isSuccessful) {
                val result = response.body()?.correctAnswer ?: ""
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "this quiz's current answer is $result")
                    resultTextView.text = result
                    fetchQuiz()  // 새로운 퀴즈 가져오기
                }
            }
        }
    }
}
