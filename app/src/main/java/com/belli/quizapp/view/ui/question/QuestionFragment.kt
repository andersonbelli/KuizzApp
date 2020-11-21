package com.belli.quizapp.view.ui.question

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.belli.quizapp.Constants
import com.belli.quizapp.R
import com.belli.quizapp.control.firebase.FirebaseControl
import com.belli.quizapp.model.Question
import com.belli.quizapp.view.MainActivity
import com.belli.quizapp.view.ResultActivity
import kotlinx.android.synthetic.main.question_fragment.view.*

class QuestionFragment : Fragment(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    companion object {
        fun newInstance() = QuestionFragment()
    }

    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.question_fragment, container, false)
        viewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)

        mUserName = arguments?.getString(Constants.USER_NAME)
//        mUserName = intent.getStringExtra()

        mQuestionsList = Constants.getQuestions()

        view.textView_optionOne.setOnClickListener(this)
        view.textView_optionTwo.setOnClickListener(this)
        view.textView_optionThree.setOnClickListener(this)
        view.textView_optionFour.setOnClickListener(this)
        view.button_submit.setOnClickListener(this)
        view.button_signOut.setOnClickListener(this)

        return view
    }

    override fun onStart() {
        super.onStart()
        setQuestion()
    }

    private fun setQuestion() {
        val question = mQuestionsList!![mCurrentPosition - 1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList!!.size) {
            requireView().button_submit.text = "FINISH"
        } else {
            requireView().button_submit.text = "SUBMIT"
        }

        requireView().progressBar.progress = mCurrentPosition

        requireView().textView_progress.text = "$mCurrentPosition / ${requireView().progressBar.max}"

        requireView().textView_question.text = question.question
        requireView().imageView_image.setImageResource(question.image)
        requireView().textView_optionOne.text = question.optionOne
        requireView().textView_optionTwo.text = question.optionTwo
        requireView().textView_optionThree.text = question.optionThree
        requireView().textView_optionFour.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, requireView().textView_optionOne)
        options.add(1, requireView().textView_optionTwo)
        options.add(2, requireView().textView_optionThree)
        options.add(3, requireView().textView_optionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textView_optionOne -> {
                selectedOptionView(requireView().textView_optionOne, 1)
            }
            R.id.textView_optionTwo -> {
                selectedOptionView(requireView().textView_optionTwo, 2)
            }
            R.id.textView_optionThree -> {
                selectedOptionView(requireView().textView_optionThree, 3)
            }
            R.id.textView_optionFour -> {
                selectedOptionView(requireView().textView_optionFour, 4)
            }
            R.id.button_submit -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(activity, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            activity?.finish()
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
                        requireView().button_submit.text = "FINISH"
                    } else {
                        requireView().button_submit.text = "GO TO THE NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
            R.id.button_signOut -> {
                Log.w("666", "onClick: SIGNOUT000")
                FirebaseControl.firebaseSignOut()
                Log.w("666", "onClick: SIGNOUT001")

                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun verifyAnswer(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                requireView().textView_optionOne.background =
                    ContextCompat.getDrawable(requireActivity(), drawableView)
            }
            2 -> {
                requireView().textView_optionTwo.background =
                    ContextCompat.getDrawable(requireActivity(), drawableView)
            }
            3 -> {
                requireView().textView_optionThree.background =
                    ContextCompat.getDrawable(requireActivity(), drawableView)
            }
            4 -> {
                requireView().textView_optionFour.background =
                    ContextCompat.getDrawable(requireActivity(), drawableView)
            }
        }
    }

    private fun selectedOptionView(textView: TextView, selectedOptionNum: Int) {
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.selected_option_border_bg)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        FirebaseControl.checkUserIsLoggedIn()
    }
}