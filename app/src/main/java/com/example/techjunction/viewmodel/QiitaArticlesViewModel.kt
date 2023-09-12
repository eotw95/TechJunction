package com.example.techjunction.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techjunction.network.model.QiitaArticlesResponse
import com.example.techjunction.repository.QiitaArticlesRepository
import kotlinx.coroutines.launch
import retrofit2.http.Query

class QiitaArticlesViewModel: ViewModel() {
    companion object {
        private const val TAG = "QiitaArticlesViewModel"
    }

    private val repository = QiitaArticlesRepository()
    private var _articles = MutableLiveData<List<QiitaArticlesResponse>>()
    val articles: LiveData<List<QiitaArticlesResponse>> = _articles

    init {
        fetchQiitaArticles("kotlin")
    }

    fun fetchQiitaArticles(query: String) {
        viewModelScope.launch {
            val articles = repository.fetchQiitaArticles(query)
            Log.d(TAG, "fetchQiitaArticles ret=$articles")
            _articles.postValue(articles)
        }
    }
}