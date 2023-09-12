package com.example.techjunction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techjunction.network.model.QiitaArticlesResponse
import com.example.techjunction.repository.QiitaArticlesRepository
import kotlinx.coroutines.launch
import retrofit2.http.Query

class QiitaArticlesViewModel: ViewModel() {
    private val repository = QiitaArticlesRepository()
    private var _articles = MutableLiveData<QiitaArticlesResponse>()
    val articles: LiveData<QiitaArticlesResponse> = _articles

    fun fetchQiitaArticles(query: String) {
        viewModelScope.launch {
            val articles = repository.fetchQiitaArticles(query)
            _articles.postValue(articles)
        }
    }
}