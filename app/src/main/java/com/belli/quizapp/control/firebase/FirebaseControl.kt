package com.belli.quizapp.control.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "FirebaseControl"

class FirebaseControl {

    companion object {

        private lateinit var auth: FirebaseAuth

        private lateinit var firebase: FirebaseApp

        fun initFirebase() {
            auth = FirebaseAuth.getInstance()
            firebase = FirebaseApp.getInstance()

            Log.w(TAG, firebase.name.toString() + firebase.applicationContext.toString())
        }

        fun checkUserIsLoggedIn(): FirebaseUser? {
            return auth.currentUser
        }

        fun firebaseLogin(email: String, password: String): Task<AuthResult>? {
            return auth.signInWithEmailAndPassword(email, password)
        }

        fun firebaseRegister(email: String, password: String): Task<AuthResult>? {
            return auth.createUserWithEmailAndPassword(email, password)
        }

        fun firebaseSignOut() {
            FirebaseAuth.getInstance().signOut()

            return auth.signOut()
        }

        fun firebaseSendChangePasswordEmail(email: String): Task<Void> {
            Log.w(TAG, "firebaseSendChangePasswordEmail: $email")

            return auth.sendPasswordResetEmail(email)
        }

        fun firebaseValidateResetPasswordCode(code: String): Task<String> {
            return auth.verifyPasswordResetCode(code)
        }
    }
}