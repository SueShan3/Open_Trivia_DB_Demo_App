package com.example.opentriviadbdemoapp.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.databinding.FragmentQuizBinding
import com.example.opentriviadbdemoapp.model.Category
import com.example.opentriviadbdemoapp.model.Question
import com.example.opentriviadbdemoapp.utils.SharedPrefUtils
import com.example.opentriviadbdemoapp.utils.SharedPrefUtils.Companion.token
import com.example.opentriviadbdemoapp.utils.Status
import com.example.opentriviadbdemoapp.viewmodel.OpenTriviaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val SCORE = "score"
private const val TIME_LEFT = "timeLeft"
private const val REQUEST_CODE = 1234

class QuizFragment : Fragment() {

    private lateinit var dataBinding: FragmentQuizBinding
    private val openTriviaViewModel: OpenTriviaViewModel by viewModel()
    private lateinit var sessionToken: String
    private var list: List<Category> = listOf()
    private var quizList: List<Question> = ArrayList()
    private var categoryId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = SharedPrefUtils.customSharedPrefs(requireContext()).token

        if (token != null) {
            sessionToken = token
        } else {
            return
        }

        loadCategoryListApi()

        with(dataBinding.spinnerCategory) {
            adapter = ArrayAdapter(
                context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list
            )

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 > 0) {
                        categoryId = list[p2 - 1].id
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

        openTriviaViewModel.getQuestionListByCategoryObserver()
            .observe(viewLifecycleOwner, Observer {
                when (it?.status) {
                    Status.SUCCESS -> {
                        val questionList = it.data?.results

                        if (!questionList.isNullOrEmpty()) {
                            quizList = questionList
                        }

                        dataBinding.loading.visibility = View.GONE

                        startActivityForResult(
                            StartQuizActivity.start(
                                requireContext(),
                                ArrayList(quizList)
                            ), REQUEST_CODE
                        )
                    }
                    else -> {}
                }
            })

        dataBinding.nextBtn.setOnClickListener {
            openTriviaViewModel.getQuestionByCategory(sessionToken, categoryId, 10)
            dataBinding.loading.visibility = View.VISIBLE
        }
    }

    private fun loadCategoryListApi() {
        openTriviaViewModel.getCategoryList()
        openTriviaViewModel.getCategoryListObserver().observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    val cList = it.data?.triviaCategories
                    if (!cList.isNullOrEmpty()) {
                        list = cList

                        val data = cList.map { it.name }.toMutableList()
                            .apply { add(0, "Default") }

                        with(dataBinding.spinnerCategory) {
                            adapter = ArrayAdapter(
                                context,
                                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                data.toTypedArray()
                            )
                        }
                    }
                }
                Status.ERROR -> {}
                else -> {}
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            val totalScore = data?.getIntExtra(SCORE, 0)
            val timeLeft = data?.getStringExtra(TIME_LEFT)

            with(dataBinding) {
                cvQuizResult.visibility = View.VISIBLE
                totalCorrect.text = getString(R.string.total_correct_score, totalScore.toString())
                timeRemaining.text = getString(R.string.time_remaining, timeLeft)
            }
        }
    }
}