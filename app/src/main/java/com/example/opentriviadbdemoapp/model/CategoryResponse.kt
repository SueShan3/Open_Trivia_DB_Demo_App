package com.example.opentriviadbdemoapp.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("trivia_categories")
    val triviaCategories: List<Category>? = null
)