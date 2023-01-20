package com.example.opentriviadbdemoapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.opentriviadbdemoapp.model.Catalog
import com.example.opentriviadbdemoapp.model.CategoryResponse
import com.example.opentriviadbdemoapp.model.QuestionResponse
import com.example.opentriviadbdemoapp.model.SessionTokenRequest
import com.example.opentriviadbdemoapp.module.Resource
import com.example.opentriviadbdemoapp.network.HttpClient
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class OpenTriviaViewModel(private val httpClient: HttpClient) : ViewModel() {
    val categoryList: MutableLiveData<Resource<CategoryResponse>?> = MutableLiveData()
    val sessionToken: MutableLiveData<Resource<SessionTokenRequest>?> = MutableLiveData()
    val resetSessionToken: MutableLiveData<Resource<SessionTokenRequest>?> = MutableLiveData()
    val questionWithCategory: MutableLiveData<Resource<QuestionResponse>?> = MutableLiveData()
    val questionCountWithCategoryID: MutableLiveData<Resource<Catalog>?> = MutableLiveData()
    val questionCountList: MutableLiveData<Resource<List<Catalog>>?> = MutableLiveData()

    fun getCategoryListObserver(): MutableLiveData<Resource<CategoryResponse>?> {
        return categoryList
    }

    fun getSessionTokenObserver(): MutableLiveData<Resource<SessionTokenRequest>?> {
        return sessionToken
    }

    fun getQuestionListByCategoryObserver(): MutableLiveData<Resource<QuestionResponse>?> {
        return questionWithCategory
    }

    fun getQuestionCountByCategoryObserver(): MutableLiveData<Resource<Catalog>?> {
        return questionCountWithCategoryID
    }

    fun getQuestionCountListByCategoryObserver(): MutableLiveData<Resource<List<Catalog>>?> {
        return questionCountList
    }

    fun getResetSessionTokenObserver(): MutableLiveData<Resource<SessionTokenRequest>?> {
        return resetSessionToken
    }

    fun getCategoryList() {
        httpClient.getCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CategoryResponse> {
                override fun onSubscribe(d: Disposable) {
                    //start show progress indicator
                }

                override fun onNext(t: CategoryResponse) {
                    categoryList.postValue(Resource.success(t))
                }

                override fun onError(e: Throwable) {
                    categoryList.postValue(null)
                    Log.d("onError", e.toString())
                }

                override fun onComplete() {
                    //hide progress indicator
                }
            })
    }

    fun getSessionToken() {
        httpClient.getSessionToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SessionTokenRequest> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: SessionTokenRequest) {
                    sessionToken.postValue(Resource.success(t))
                }

                override fun onError(e: Throwable) {
                    sessionToken.postValue(null)
                    Log.d("onError", e.toString())
                }

                override fun onComplete() {

                }
            })
    }

    fun getResetSessionToken(token: String) {
        httpClient.getResetSessionToken(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SessionTokenRequest> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: SessionTokenRequest) {
                    resetSessionToken.postValue(Resource.success(t))
                }

                override fun onError(e: Throwable) {
                    resetSessionToken.postValue(null)
                    Log.d("onError", e.toString())
                }

                override fun onComplete() {

                }
            })
    }

    fun getQuestionByCategory(token: String, category: Int, amount: Int) {
        httpClient.getQuestionByCategory(token, category, amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<QuestionResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: QuestionResponse) {
                    questionWithCategory.postValue(Resource.success(t))
                }

                override fun onError(e: Throwable) {
                    questionWithCategory.postValue(null)
                    Log.d("onError", e.toString())
                }

                override fun onComplete() {

                }
            })
    }

    fun getQuestionCountByCategory(category: Int) {
        httpClient.getQuestionCountByCategory(category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Catalog> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Catalog) {
                    questionCountWithCategoryID.postValue(Resource.success(t))
                }

                override fun onError(e: Throwable) {
                    questionCountWithCategoryID.postValue(null)
                    Log.d("onError", e.toString())
                }

                override fun onComplete() {

                }
            })
    }

    fun getCatalogByCategory() {
        // Call API to get categories list
        httpClient.getCategory()
            // Iterate and convert into Observable<Category>, from Observable<CategoryResponse>
            .flatMap {
                Observable.fromIterable(it.triviaCategories)
            }
            .flatMap(
                {
                    // Call API to get catalog based on the categoryID
                    httpClient.getQuestionCountByCategory(it.id)
                },
                // Assigning category name into catalog.categoryName
                { category, catalog ->
                    catalog.categoryName = category.name
                    return@flatMap catalog
                }
            )
            // Convert it from Observable<Catalog> to Observable<List<Catalog>>
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Catalog>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: List<Catalog>) {
                    questionCountList.postValue(Resource.success(t))
                }

                override fun onError(e: Throwable) {
                    questionCountList.postValue(null)
                    Log.d("onError", e.toString())
                }
            })
    }
}