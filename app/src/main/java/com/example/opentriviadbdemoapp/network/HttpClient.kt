package com.example.opentriviadbdemoapp.network

import com.example.opentriviadbdemoapp.model.Catalog
import com.example.opentriviadbdemoapp.model.CategoryResponse
import com.example.opentriviadbdemoapp.model.QuestionResponse
import com.example.opentriviadbdemoapp.model.SessionTokenRequest
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface HttpClient {

    @GET("/api_category.php")
    fun getCategory(): Observable<CategoryResponse>

    @GET("/api_token.php?command=request")
    fun getSessionToken(): Observable<SessionTokenRequest>

    @GET("/api.php")
    fun getQuestionByCategory(
        @Query ("token") token: String,
        @Query ("category") category: Int,
        @Query ("amount") amount: Int
    ): Observable<QuestionResponse>

    @GET("/api_count.php")
    fun getQuestionCountByCategory(
        @Query("category") category: Int
    ): Observable<Catalog>
}