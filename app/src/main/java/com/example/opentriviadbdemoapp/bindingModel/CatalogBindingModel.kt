package com.example.opentriviadbdemoapp.bindingModel

import com.example.opentriviadbdemoapp.model.Catalog

class CatalogBindingModel(private val catalog: Catalog) {

    fun getCategoryName() : String = catalog.categoryName.toString()

    fun getTotalQuestionCount(): String =
        catalog.categoryQuestionCount.totalQuestionCount.toString()

    fun getEasyQuestionCount(): String =
        "Easy: " + catalog.categoryQuestionCount.totalEasyQuestionCount.toString()

    fun getMediumQuestionCount(): String =
        "Medium: " + catalog.categoryQuestionCount.totalMediumQuestionCount.toString()

    fun getHardQuestionCount(): String =
        "Hard: " + catalog.categoryQuestionCount.totalHardQuestionCount.toString()
}