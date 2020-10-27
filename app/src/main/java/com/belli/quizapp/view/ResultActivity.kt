package com.belli.quizapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.belli.quizapp.Constants
import com.belli.quizapp.R
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // remove android toolbar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val username = intent.getStringExtra(Constants.USER_NAME)
        textView_username.text = username

        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)
        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        textView_score.text =
            "You score $correctAnswers of $totalQuestions questions"

        when {
            correctAnswers == totalQuestions -> {
                imageView_trophy.setImageResource(R.drawable.trophyfail)
                textView_message.text = "Hey, are you a specialist or something?\n Congratulations!"
            }
            correctAnswers >= (totalQuestions / 2) -> {
                imageView_trophy.setImageResource(R.drawable.trophyfail)
                textView_message.text = "Congratulations!"
            }
            else -> {
                imageView_trophy.setImageResource(R.drawable.crying)
                textView_message.text = "That's not went well, try again\n(You should be sad)"
            }
        }

        button_finish.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}