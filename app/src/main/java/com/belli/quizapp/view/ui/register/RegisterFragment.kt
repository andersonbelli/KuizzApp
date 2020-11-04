package com.belli.quizapp.view.ui.register

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.belli.quizapp.Constants
import com.belli.quizapp.R
import com.belli.quizapp.control.firebase.FirebaseControl
import com.belli.quizapp.view.MainActivity
import com.belli.quizapp.view.ui.login.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.register_fragment.view.*
import kotlinx.android.synthetic.main.reset_password_fragment.view.*

private const val TAG = "ResetPasswordFragment"
private const val DELAY_TIME: Long = 15000

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.register_fragment, container, false)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        view.button_register.setOnClickListener {
            onClick(view.button_resetPassword.text.toString())
        }

        view.textView_register_backToLogin.setOnClickListener {
            backToLogin()
        }

        return view
    }

    private fun onClick(buttonAction: String) {
        requireActivity().layout_main_loading.visibility = View.VISIBLE

        when (buttonAction) {
            "send code" -> delayToSendCode()
//                sendCodeToEmail(requireView().editText_resetPassword_email.text.toString())
            "send again" -> {
                if (requireView().button_resetPassword.isEnabled) sendCodeToEmail(requireView().editText_resetPassword_email.text.toString())
            }
        }
    }

    private fun sendCodeToEmail(email: String) {
        FirebaseControl.firebaseSendChangePasswordEmail(email).addOnCompleteListener { task ->
            Log.w(TAG, "resetPassword000: $task")
            Log.w(TAG, "resetPassword111: ${task.result}")

            if (task.isComplete && requireActivity().layout_main_loading.visibility == View.VISIBLE) requireActivity().layout_main_loading.visibility =
                View.GONE

            if (task.isSuccessful) {
                requireView().textInputLayout_resetPassword_email.visibility = View.GONE
                requireView().button_resetPassword.text = "send again"

                delayToSendCode()
            }
        }
    }

    private fun delayToSendCode() {
        requireView().textView_resetPassword_message.text =
            "An email was sent, check your mail box or spam box"
        requireView().textView_resetPassword_message.textSize = 18f
        requireView().textView_resetPassword_message.setTextColor(Color.parseColor("#ff6961"))

        requireView().button_resetPassword.isEnabled = false

        requireView().chronometer_resetPassword_timer.visibility = View.VISIBLE
        requireView().chronometer_resetPassword_timer.isCountDown = true
        requireView().chronometer_resetPassword_timer.base =
            SystemClock.elapsedRealtime() + DELAY_TIME
        requireView().chronometer_resetPassword_timer.start()

        // Set a delay in button to send new message
        Handler().postDelayed(Runnable {
            requireView().chronometer_resetPassword_timer.visibility = View.GONE
            requireView().button_resetPassword.isEnabled = true
            requireView().chronometer_resetPassword_timer.stop()
        }, DELAY_TIME)
    }

    private fun validateCode(code: String) {
        FirebaseControl.firebaseValidateResetPasswordCode(code).addOnCompleteListener { task ->
            Log.w(TAG, "validateCode: $task")
            Log.w(TAG, "validateCode: ${task.isSuccessful}")

            if (task.isComplete && requireActivity().layout_main_loading.visibility == View.VISIBLE) requireActivity().layout_main_loading.visibility =
                View.GONE
        }
    }

    private fun backToLogin() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Back to Login?")
        builder.setMessage("Cancel password reset and back to login?")
        builder.setPositiveButton("YES") { dialog, which ->
            dialog.dismiss()
            Log.w(ContentValues.TAG, which.toString())
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragmentLayout_main,
                LoginFragment.newInstance(),
                "loginFragment"
            ).commit()
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}