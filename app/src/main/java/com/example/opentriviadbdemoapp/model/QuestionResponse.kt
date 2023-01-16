package com.example.opentriviadbdemoapp.model

import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("response_code")
    val responseCode: Int? = null,
    @SerializedName("results")
    val results: List<Question>? = null
)