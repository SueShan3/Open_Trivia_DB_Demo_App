package com.example.opentriviadbdemoapp.model

import com.google.gson.annotations.SerializedName

data class CatalogQuestionCount(
    @SerializedName("total_question_count")
    val totalQuestionCount: Int,
    @SerializedName("total_easy_question_count")
    val totalEasyQuestionCount: Int,
    @SerializedName("total_medium_question_count")
    val totalMediumQuestionCount: Int,
    @SerializedName("total_hard_question_count")
    val totalHardQuestionCount: Int
)