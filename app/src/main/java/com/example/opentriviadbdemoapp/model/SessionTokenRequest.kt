package com.example.opentriviadbdemoapp.model

import com.google.gson.annotations.SerializedName

data class SessionTokenRequest(
    @SerializedName("response_code")
    val responseCode: Int? = null,
    @SerializedName("response_message")
    val responseMessage: String? = null,
    @SerializedName("token")
    val token: String? = null
)