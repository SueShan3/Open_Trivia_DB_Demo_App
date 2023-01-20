package com.example.opentriviadbdemoapp.bindingModel

import android.content.Context
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.model.Catalog

class CatalogBindingModel(val context: Context, private val catalog: Catalog) {

    fun getCategoryName(): String = catalog.categoryName.toString()

    fun getTotalQuestionCount(): String =
        catalog.categoryQuestionCount.totalQuestionCount.toString()

    fun getEasyQuestionCount(): String =
        context.getString(
            R.string.easy,
            catalog.categoryQuestionCount.totalEasyQuestionCount.toString()
        )

    fun getMediumQuestionCount(): String =
        context.getString(
            R.string.medium,
            catalog.categoryQuestionCount.totalMediumQuestionCount.toString()
        )

    fun getHardQuestionCount(): String =
        context.getString(
            R.string.hard,
            catalog.categoryQuestionCount.totalHardQuestionCount.toString()
        )
}