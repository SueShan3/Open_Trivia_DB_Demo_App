package com.example.opentriviadbdemoapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.databinding.FragmentQuizBinding
import com.example.opentriviadbdemoapp.model.Category
import com.example.opentriviadbdemoapp.utils.Status
import com.example.opentriviadbdemoapp.viewmodel.OpenTriviaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuizFragment : Fragment() {

    private lateinit var dataBinding: FragmentQuizBinding
    private val openTriviaViewModel: OpenTriviaViewModel by viewModel()
    private var list: List<Category> = listOf()


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

        //TODO: After selected category, click next btn and pass category id to startQuizActivity
        dataBinding.nextBtn.setOnClickListener {
            startActivity(Intent(activity, StartQuizActivity::class.java))
        }
    }
}