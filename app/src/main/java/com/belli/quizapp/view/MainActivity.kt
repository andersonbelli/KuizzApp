package com.belli.quizapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.belli.quizapp.R
import com.belli.quizapp.control.firebase.FirebaseControl
import com.belli.quizapp.view.ui.login.LoginFragment
import com.google.firebase.auth.FirebaseUser

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // remove android toolbar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        FirebaseControl.initFirebase()
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in and update UI accordingly
        updateUI(FirebaseControl.checkUserIsLoggedIn())
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Log.w("MainActivity", "User status -> " + currentUser.toString())
        Log.w("MainActivity", "User status - Uid -> " + currentUser?.uid.toString())
        if (currentUser == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentLayout_main, LoginFragment.newInstance(), "loginFragment")
                .commit()
        } else {
            startActivity(Intent(this, QuizQuestionActivity::class.java))
            finish()
        }
    }

}