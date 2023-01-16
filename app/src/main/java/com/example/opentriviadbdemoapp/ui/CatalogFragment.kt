package com.example.opentriviadbdemoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.databinding.FragmentCatalogBinding
import com.example.opentriviadbdemoapp.item.CatalogItem
import com.example.opentriviadbdemoapp.model.Category
import com.example.opentriviadbdemoapp.utils.Status
import com.example.opentriviadbdemoapp.viewmodel.OpenTriviaViewModel
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatalogFragment : Fragment() {

    private lateinit var dataBinding: FragmentCatalogBinding
    private val openTriviaViewModel: OpenTriviaViewModel by viewModel()
    private var mAdapter = GroupieAdapter() //Group Adapter
    private var mSection = Section()
    private var list: List<Category> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //to get category id and name
        loadCategoryApi()

        loadCatalogApi()

        dataBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.add(mSection)

    }

    private fun loadCategoryApi() {
        openTriviaViewModel.getCategoryList()
        openTriviaViewModel.getCategoryListObserver().observe(viewLifecycleOwner, Observer {
            when(it?.status){
                Status.SUCCESS->{
                    val cList = it.data?.triviaCategories
                    if (!cList.isNullOrEmpty()) {
                        list = cList
                        //pass category id
                        list.forEach{
                            openTriviaViewModel.getQuestionCountByCategory(it.id)
                        }
                    }
                }
                else -> {}
            }
        })
    }

    private fun loadCatalogApi() {
        openTriviaViewModel.getQuestionCountByCategoryObserver().observe(viewLifecycleOwner, Observer {
            when(it?.status){
                Status.SUCCESS ->{
                    val catalog = it.data

                    mSection.add(CatalogItem(catalog!!))

                }
                else -> {}
            }
        })
    }
}