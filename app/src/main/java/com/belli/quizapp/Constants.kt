package com.belli.quizapp

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.belli.quizapp.model.Question
import com.belli.quizapp.view.MainActivity
import com.belli.quizapp.view.ui.login.LoginFragment


object Constants {

    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_question"
    const val CORRECT_ANSWERS: String = "correct_answers"

    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        val que1 = Question(
            1,
            "What country does this flag belong to?",
            R.drawable.ic_flag_brazil,
            "Argentina",
            "Bolivia",
            "Brazil",
            "Bulgaria",
            3
        )
        questionsList.add(que1)

        val que2 = Question(
            2,
            "What country does this flag belong to?",
            R.drawable.ic_flag_india,
            "Malaysia",
            "India",
            "Argentina",
            "United States",
            2
        )
        questionsList.add(que2)

        val que3 = Question(
            3,
            "What country does this flag belong to?",
            R.drawable.ic_flag_malaysia,
            "Malaysia",
            "Chile",
            "Australia",
            "Germany",
            1
        )
        questionsList.add(que3)

        val que4 = Question(
            4,
            "What country does this flag belong to?",
            R.drawable.ic_flag_newzealand,
            "United Kingdom",
            "Australia",
            "New Zealand",
            "Cuba",
            3
        )
        questionsList.add(que4)

        return questionsList
    }
}