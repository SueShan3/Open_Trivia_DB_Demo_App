package com.example.opentriviadbdemoapp.model

import com.google.gson.annotations.SerializedName

data class Question (
    @SerializedName("question")
    val question: String ?= null,
    @SerializedName("difficulty")
    val difficulty: String ?= null,
    @SerializedName("type")
    val type: String ?= null,
    @SerializedName("category")
    val category: String ?= null,
    @SerializedName("correct_answer")
    val correctAnswer: String ?= null,
    @SerializedName("incorrect_answers")
    val incorrectAnswer: List<String> ?=null
)