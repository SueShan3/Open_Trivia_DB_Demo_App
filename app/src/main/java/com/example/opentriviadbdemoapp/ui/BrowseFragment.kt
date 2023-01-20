package com.example.opentriviadbdemoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.adapter.CategoryAdapter
import com.example.opentriviadbdemoapp.databinding.FragmentBrowseBinding
import com.example.opentriviadbdemoapp.model.Category
import com.example.opentriviadbdemoapp.model.Question
import com.example.opentriviadbdemoapp.utils.EndlessRecyclerOnScrollListener
import com.example.opentriviadbdemoapp.utils.SharedPrefUtils
import com.example.opentriviadbdemoapp.utils.SharedPrefUtils.Companion.token
import com.example.opentriviadbdemoapp.utils.Status
import com.example.opentriviadbdemoapp.viewmodel.OpenTriviaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAKE = 15

class BrowseFragment : Fragment() {

    private lateinit var dataBinding: FragmentBrowseBinding
    private val openTriviaViewModel: OpenTriviaViewModel by viewModel()
    private lateinit var sessionToken: String
    private lateinit var adapter: CategoryAdapter
    private var list: List<Category> = listOf()
    private var categoryList: List<Question> = listOf()
    private var categoryId = 0
    private var totalQuestion = 0

    private var endlessListener: EndlessListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse, container, false)
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

        showLoading(true)

        loadCategoryApi()

        openTriviaViewModel.getResetSessionTokenObserver()
            .observe(viewLifecycleOwner, Observer {
                when (it?.status) {
                    Status.SUCCESS -> {
                        openTriviaViewModel.getQuestionByCategory(sessionToken, categoryId, TAKE)
                    }
                    else -> {}
                }
            })

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = CategoryAdapter()
        dataBinding.recyclerView.layoutManager = layoutManager
        dataBinding.recyclerView.adapter = adapter

        with(dataBinding.recyclerView) {
            endlessListener = EndlessListener(layoutManager).also {
                addOnScrollListener(it)
            }
        }

        dataBinding.swipeToRefresh.setOnRefreshListener {
            getQuestionByCategory(sessionToken, categoryId, true)
        }

        dataBinding.loading.setOnClickListener {

        }

        with(dataBinding.spinnerCategory) {
            adapter = ArrayAdapter(
                context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list
            )

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 > 0) {
                        categoryId = list[p2 - 1].id
                        getQuestionByCategory(sessionToken, categoryId, true)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

        openTriviaViewModel.getQuestionListByCategoryObserver()
            .observe(viewLifecycleOwner, Observer {
                showLoading(false)
                when (it?.status) {
                    Status.SUCCESS -> {

                        val caList = it.data?.results

                        if (!caList.isNullOrEmpty()) {
                            categoryList = caList
                        }

                        if (mIsRefresh) {
                            adapter.updateChangedCategoryList(categoryList)
                        } else {
                            adapter.setCategoryList(categoryList)
                        }

                        endlessListener?.hasMorePages(caList?.count()!! >= TAKE)
                        endlessListener?.onLoadMoreComplete()
                    }
                    else -> {}
                }
            })
    }

    internal inner class EndlessListener(layoutManager: LinearLayoutManager) :
        EndlessRecyclerOnScrollListener(layoutManager) {
        override fun onLoadMore() {
            getQuestionByCategory(sessionToken, categoryId, false)
            dataBinding.bottomLoading.visibility = View.VISIBLE
        }

        override fun onLoadMoreComplete() {
            super.onLoadMoreComplete()
            dataBinding.bottomLoading.visibility = View.GONE
            setEnabled(true)
        }

    }

    private fun getTotalQuestionCountByCategory(id: Int) {
        openTriviaViewModel.getQuestionCountByCategory(id)
        openTriviaViewModel.getQuestionCountByCategoryObserver()
            .observe(viewLifecycleOwner, Observer {
                when (it?.status) {
                    Status.SUCCESS -> {
                        totalQuestion = it.data?.categoryQuestionCount?.totalQuestionCount ?: 0
                    }
                    else -> {}
                }
            })
    }

    private fun loadCategoryApi() {
        openTriviaViewModel.getCategoryList()
        openTriviaViewModel.getCategoryListObserver().observe(viewLifecycleOwner, Observer {
            showLoading(false)
            when (it?.status) {
                Status.SUCCESS -> {
                    val cList = it.data?.triviaCategories
                    if (!cList.isNullOrEmpty()) {
                        list = cList

                        val data = cList.map { it.name }.toMutableList()
                            .apply { add(0, "Select a category") }

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

    private var mIsRefresh = true

    private fun getQuestionByCategory(token: String, id: Int, isRefresh: Boolean) {
        mIsRefresh = isRefresh
        if (!isRefresh) {
            val leftTotalQuestionCount = totalQuestion - adapter.itemCount
            var actualTake = TAKE

            if (TAKE > leftTotalQuestionCount) {
                actualTake = leftTotalQuestionCount
            }

            openTriviaViewModel.getQuestionByCategory(token, id, actualTake)
        } else {
            showLoading(true)
            openTriviaViewModel.getResetSessionToken(token)
            getTotalQuestionCountByCategory(categoryId)
        }
    }

    private fun showLoading(isVisible: Boolean) {
        with(dataBinding) {
            swipeToRefresh.isRefreshing = isVisible
            swipeToRefresh.isEnabled = !isVisible
            loading.isVisible = isVisible
        }
    }
}