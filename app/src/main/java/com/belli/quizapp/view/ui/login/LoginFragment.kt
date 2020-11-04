package com.belli.quizapp.view.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.belli.quizapp.Constants
import com.belli.quizapp.R
import com.belli.quizapp.control.firebase.FirebaseControl
import com.belli.quizapp.features.Validators
import com.belli.quizapp.view.QuizQuestionActivity
import com.belli.quizapp.view.ui.login.reset_password.ResetPasswordFragment
import com.belli.quizapp.view.ui.register.RegisterFragment
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import java.lang.Exception

private const val TAG = "Login Fragment"

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.login_fragment, container, false)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        view.button_login.setOnClickListener {
            loginUser(view.editText_login_email, view.editText_login_password)
        }

        view.textView_resetPassword_backToLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragmentLayout_main,
                RegisterFragment.newInstance(),
                "replaceFragment"
            ).commit()
        }

        view.textView_forgetPassword.setOnClickListener {
            goToChangePassword()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        textView_errorMessage.visibility
    }

    private fun loginUser(email: EditText, password: EditText) {
        requireActivity().layout_main_loading.visibility = View.VISIBLE

        if (Validators.validateEmail(email) &&
            Validators.validateEmptyField(password, "Please enter password")
        ) {
            FirebaseControl.firebaseLogin(
                email.text.toString(),
                password.text.toString()
            )?.addOnCompleteListener { task ->
                if (task.isComplete && requireActivity().layout_main_loading.visibility == View.VISIBLE) requireActivity().layout_main_loading.visibility =
                    View.GONE

                if (task.isSuccessful) {
//                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()

                    val intent = Intent(activity, QuizQuestionActivity::class.java)
                    intent.putExtra(
                        Constants.USER_NAME,
                        editText_login_email.text.toString()
                    )
                    startActivity(intent)
                    activity?.finish()
                }
            }?.addOnFailureListener { error ->

                Log.e(TAG, "\nError: $error \n\n")
                run {
                    // If sign in fails, display a message to the user
                    try {
                        when ((error as FirebaseAuthException).errorCode) {
                            "ERROR_INVALID_CUSTOM_TOKEN" ->
                                Toast.makeText(
                                    activity,
                                    "The custom token format is incorrect. Please check the documentation.",
                                    Toast.LENGTH_LONG
                                ).show()


                            "ERROR_CUSTOM_TOKEN_MISMATCH" ->
                                Toast.makeText(
                                    activity,
                                    "The custom token corresponds to a different audience.",
                                    Toast.LENGTH_LONG
                                ).show()


                            "ERROR_INVALID_CREDENTIAL" ->
                                Toast.makeText(
                                    activity,
                                    "The supplied auth credential is malformed or has expired.",
                                    Toast.LENGTH_LONG
                                ).show()


                            "ERROR_INVALID_EMAIL" -> {
                                Toast.makeText(
                                    activity,
                                    "The email address is badly formatted.",
                                    Toast.LENGTH_LONG
                                ).show()
                                email.error = "The email address is badly formatted."
                                email.requestFocus()
                            }


                            "ERROR_WRONG_PASSWORD" -> {
                                Toast.makeText(
                                    activity,
                                    "The password is invalid or the user does not have a password.",
                                    Toast.LENGTH_LONG
                                ).show()
                                password.error = "password is incorrect "
                                password.requestFocus()
                                password.setText("")
                            }


                            "ERROR_USER_MISMATCH" ->
                                Toast.makeText(
                                    activity,
                                    "The supplied credentials do not correspond to the previously signed in user.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_REQUIRES_RECENT_LOGIN" ->
                                Toast.makeText(
                                    activity,
                                    "This operation is sensitive and requires recent authentication. Log in again before retrying this request.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" ->
                                Toast.makeText(
                                    activity,
                                    "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                Toast.makeText(
                                    activity,
                                    "The email address is already in use by another account.   ",
                                    Toast.LENGTH_LONG
                                ).show()
                                email.error =
                                    "The email address is already in use by another account."
                                email.requestFocus()
                            }

                            "ERROR_CREDENTIAL_ALREADY_IN_USE"
                            ->
                                Toast.makeText(
                                    activity,
                                    "This credential is already associated with a different user account.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_USER_DISABLED" ->
                                Toast.makeText(
                                    activity,
                                    "The user account has been disabled by an administrator.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_USER_TOKEN_EXPIRED" ->
                                Toast.makeText(
                                    activity,
                                    "The user\\'s credential is no longer valid. The user must sign in again.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_USER_NOT_FOUND" ->
                                Toast.makeText(
                                    activity,
                                    "There is no user record corresponding to this identifier. The user may have been deleted.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_INVALID_USER_TOKEN" ->
                                Toast.makeText(
                                    activity,
                                    "The user\\'s credential is no longer valid. The user must sign in again.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_OPERATION_NOT_ALLOWED" ->
                                Toast.makeText(
                                    activity,
                                    "This operation is not allowed. You must enable this service in the console.",
                                    Toast.LENGTH_LONG
                                ).show()

                            "ERROR_WEAK_PASSWORD" -> {
                                Toast.makeText(
                                    activity,
                                    "The given password is invalid.",
                                    Toast.LENGTH_LONG
                                ).show()
                                password.error =
                                    "The password is invalid it must 6 characters at least"
                                password.requestFocus()
                            }
                        }
                    } catch (e: FirebaseTooManyRequestsException) {
                        val exception: FirebaseTooManyRequestsException = e
                        requireView().textView_errorMessage.text =
                            "Too many wrong entries, try again later or change password"
                        requireView().textView_errorMessage.visibility = View.VISIBLE
                        Log.e(TAG, "FirebaseTooManyRequestsException: ${exception.message}")
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception: ${e.message}")
                        if (e.toString().contains("FirebaseTooManyRequestsException")) {
                            textView_errorMessage.text =
                                "Too many wrong entries, try again later or change password"
                            textView_errorMessage.visibility = View.VISIBLE
                        }
                    }
                }
            }
        } else {
            if (requireActivity().layout_main_loading.visibility == View.VISIBLE) requireActivity().layout_main_loading.visibility =
                View.GONE
        }
    }

    private fun goToChangePassword() {
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.fragmentLayout_main,
            ResetPasswordFragment.newInstance(),
            "resetFragment"
        ).commit()
    }

}