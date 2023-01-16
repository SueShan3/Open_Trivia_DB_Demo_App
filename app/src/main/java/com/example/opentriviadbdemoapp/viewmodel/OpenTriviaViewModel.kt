package com.example.opentriviadbdemoapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.opentriviadbdemoapp.model.*
import com.example.opentriviadbdemoapp.module.Resource
import com.example.opentriviadbdemoapp.module.getRetroInstance
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class OpenTriviaViewModel : ViewModel() {
    val categoryList: MutableLiveData<Resource<CategoryResponse>?> = MutableLiveData()
    val sessionToken: MutableLiveData<Resource<SessionTokenRequest>?> = MutableLiveData()
    val questionWithCategory: MutableLiveData<Resource<QuestionResponse>?> = MutableLiveData()
    val questionCountWithCategory: MutableLiveData<Resource<Catalog>?> = MutableLiveData()

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
        return questionCountWithCategory
    }

    fun getCategoryList() {
        getRetroInstance().getCategory()
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
        getRetroInstance().getSessionToken()
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

    fun getQuestionByCategory(token: String, category: Int, amount: Int) {
        getRetroInstance().getQuestionByCategory(token, category, amount)
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

    fun getQuestionCountByCategory(category: Int){
        getRetroInstance().getQuestionCountByCategory(category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Catalog>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Catalog) {
                    questionCountWithCategory.postValue(Resource.success(t))
                }

                override fun onError(e: Throwable) {
                    questionCountWithCategory.postValue(null)
                    Log.d("onError", e.toString())
                }

                override fun onComplete() {

                }

            })
    }
}