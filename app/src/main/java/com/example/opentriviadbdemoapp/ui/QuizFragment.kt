package com.example.opentriviadbdemoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private lateinit var dataBinding: FragmentQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false)
        return dataBinding.root
    }
}