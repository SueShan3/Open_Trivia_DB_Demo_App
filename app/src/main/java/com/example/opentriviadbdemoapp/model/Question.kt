package com.example.opentriviadbdemoapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    @SerializedName("question")
    val question: String,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswer: List<String>,

    var selectedAnswer: String? = null
) : Parcelable {
    fun getAllAvailableAnswers(): List<String> {
        return incorrectAnswer + correctAnswer
    }

    //compare the selected answer with correct answer
    fun isAnswerCorrect(answer: String?): Boolean {
        return answer == correctAnswer
    }
}