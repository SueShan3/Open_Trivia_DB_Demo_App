package com.example.opentriviadbdemoapp.model

import com.google.gson.annotations.SerializedName

data class Catalog (
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("category_question_count")
    val categoryQuestionCount: CatalogQuestionCount,

    var categoryName: String ?= null
)