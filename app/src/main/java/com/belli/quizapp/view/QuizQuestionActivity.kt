package com.belli.quizapp.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.belli.quizapp.Constants
import com.belli.quizapp.R
import com.belli.quizapp.model.Question
import kotlinx.android.synthetic.main.activity_quiz_question.*

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        textView_optionOne.setOnClickListener(this)
        textView_optionTwo.setOnClickListener(this)
        textView_optionThree.setOnClickListener(this)
        textView_optionFour.setOnClickListener(this)
        button_submit.setOnClickListener(this)
    }

    private fun setQuestion() {
        val question = mQuestionsList!![mCurrentPosition - 1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList!!.size) {
            button_submit.text = "FINISH"
        } else {
            button_submit.text = "SUBMIT"
        }

        progressBar.progress = mCurrentPosition

        textView_progress.text = "$mCurrentPosition / ${progressBar.max}"

        textView_question.text = question.question
        imageView_image.setImageResource(question.image)
        textView_optionOne.text = question.optionOne
        textView_optionTwo.text = question.optionTwo
        textView_optionThree.text = question.optionThree
        textView_optionFour.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, textView_optionOne)
        options.add(1, textView_optionTwo)
        options.add(2, textView_optionThree)
        options.add(3, textView_optionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textView_optionOne -> {
                selectedOptionView(textView_optionOne, 1)
            }
            R.id.textView_optionTwo -> {
                selectedOptionView(textView_optionTwo, 2)
            }
            R.id.textView_optionThree -> {
                selectedOptionView(textView_optionThree, 3)
            }
            R.id.textView_optionFour -> {
                selectedOptionView(textView_optionFour, 4)
            }
            R.id.button_submit -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        verifyAnswer(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswers++
                    }
                    verifyAnswer(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionsList!!.size) {
                        button_submit.text = "FINISH"
                    } else {
                        button_submit.text = "GO TO THE NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun verifyAnswer(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                textView_optionOne.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                textView_optionTwo.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                textView_optionThree.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                textView_optionFour.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun selectedOptionView(textView: TextView, selectedOptionNum: Int) {
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }
}