package com.example.techjunction.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techjunction.network.model.QiitaArticlesResponse
import com.example.techjunction.repository.QiitaArticlesRepository
import com.example.techjunction.room.RssDatabase
import com.example.techjunction.room.RssRepositoryImpl
import kotlinx.coroutines.launch

class ArticlesViewModel(private val application: Application): ViewModel() {
    companion object {
        private const val TAG = "ArticlesViewModel"
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