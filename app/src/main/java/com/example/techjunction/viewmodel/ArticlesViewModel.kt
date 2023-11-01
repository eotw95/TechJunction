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
import com.example.techjunction.room.RssChannel
import com.example.techjunction.room.RssDatabase
import com.example.techjunction.room.RssItem
import com.example.techjunction.room.RssRepositoryImpl
import kotlinx.coroutines.launch

class ArticlesViewModel(private val application: Application): ViewModel() {
    companion object {
        private const val TAG = "ArticlesViewModel"
    }

    private val qiitaArtRepo = QiitaArticlesRepository()
    private val rssRepo = RssRepositoryImpl(RssDatabase.getInstance(application))
    private var _articles = MutableLiveData<List<QiitaArticlesResponse>>()
    val articles: LiveData<List<QiitaArticlesResponse>> = _articles
    private var _rssChannels = MutableLiveData<List<RssChannel>>()
    val rssChannels: LiveData<List<RssChannel>> = _rssChannels
    private val _rssItems = MutableLiveData<List<RssItem>>()
    val rssItems: LiveData<List<RssItem>> = _rssItems

    init {
        fetchQiitaArticles("kotlin")
        fetchRssChannels()
        fetchRssitems()
    }

    fun fetchQiitaArticles(query: String) {
        viewModelScope.launch {
            val articles = qiitaArtRepo.fetchQiitaArticles(query)
            Log.d(TAG, "fetchQiitaArticles ret=$articles")
            _articles.postValue(articles)
        }
    }

    fun fetchRssChannels() {
        viewModelScope.launch {
            val channels = rssRepo.getChannels()
            _rssChannels.postValue(channels)
        }
    }

    fun fetchRssitems() {
        viewModelScope.launch {
            val allItems = mutableListOf<RssItem>()
            rssRepo.getChannels().forEach {channel ->
                val items = rssRepo.getItemsByChannelId(channel.id)
                allItems.addAll(items)
            }
            _rssItems.postValue(allItems)
        }
    }
}