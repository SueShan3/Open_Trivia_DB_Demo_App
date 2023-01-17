package com.example.opentriviadbdemoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.databinding.ActivityStartQuizBinding

class StartQuizActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityStartQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_start_quiz)
    }
}