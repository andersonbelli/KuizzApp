package com.belli.quizapp.features

import android.util.Patterns
import android.widget.EditText

class Validators {
    companion object {
        fun validateEmail(field: EditText): Boolean {
            if (field.text.toString().isEmpty()) {
                field.error = "Please enter email"
                field.requestFocus()
                return false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(field.text.toString()).matches()) {
                field.error = "Please enter valid email"
                field.requestFocus()
                return false
            }
            return true
        }

        fun validateEmptyField(field: EditText, message: String): Boolean {
            if (field.text.toString().isEmpty()) {
                field.error = message
                field.requestFocus()
                return false
            }
            return true
        }
    }
}