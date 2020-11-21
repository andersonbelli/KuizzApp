package com.belli.quizapp.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.belli.quizapp.R
import com.belli.quizapp.view.ui.question.QuestionFragment
import kotlinx.android.synthetic.main.activity_quiz_question.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class QuizQuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        setSupportActionBar(toolbar)

//        drawer_layout.open()
        drawer_layout.fitsSystemWindows = true

        val drawerToggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.common_open_on_phone,
            R.string.common_signin_button_text_long
        )

        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val questionFragment = QuestionFragment.newInstance()
        questionFragment.arguments = intent.extras
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentLayout_question, questionFragment, "questionFragment")
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (!drawer_layout.isOpen) {
                    drawer_layout.openDrawer(GravityCompat.START)
                    true
                } else {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    false
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}