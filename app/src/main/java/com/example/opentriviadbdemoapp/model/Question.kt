package com.example.opentriviadbdemoapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Question (
    @PrimaryKey(autoGenerate = true) val uid:Int? = null,

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
):Parcelable{
    fun getAllAvailableAnswers(): List<String>{
        return incorrectAnswer + correctAnswer
    }

    //TODO: compare the selected answer with correct answer
    fun isAnswerCorrect(answer: String?): Boolean{
        return answer == correctAnswer
    }
}