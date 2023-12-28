package com.example.techjunction.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techjunction.room.FollowArticle
import com.example.techjunction.room.FollowArticleDatabase
import com.example.techjunction.room.FollowArticleRepositoryImpl
import com.example.techjunction.room.QiitaArticle
import com.example.techjunction.room.QiitaArticleDatabase
import com.example.techjunction.room.QiitaArticleRepositoryImpl
import com.example.techjunction.room.RssChannel
import com.example.techjunction.room.RssDatabase
import com.example.techjunction.room.RssItem
import com.example.techjunction.room.RssRepositoryImpl
import kotlinx.coroutines.launch

class ArticlesViewModel(private val application: Application): ViewModel() {
    companion object {
        private const val TAG = "ArticlesViewModel"
    }

    private val db = QiitaArticleDatabase.getInstance(application)
    private val qiitaArtRepo = QiitaArticleRepositoryImpl(db)
    private val rssRepo = RssRepositoryImpl(RssDatabase.getInstance(application))
    private val followArticleRepo =
        FollowArticleRepositoryImpl(FollowArticleDatabase.getInstance(application))
    private var _articles = MutableLiveData<List<QiitaArticle>>()
    val articles: LiveData<List<QiitaArticle>> = _articles
    private var _rssChannels = MutableLiveData<List<RssChannel>>()
    val rssChannels: LiveData<List<RssChannel>> = _rssChannels
    private val _rssItems = MutableLiveData<List<RssItem>>()
    val rssItems: LiveData<List<RssItem>> = _rssItems
    private val _followArticle = MutableLiveData<List<FollowArticle>>()
    val followArticle: LiveData<List<FollowArticle>> = _followArticle

    init {
        fetchQiitaArticles("kotlin")
        fetchRssChannels()
        fetchRssitems()
    }

    private fun fetchQiitaArticles(query: String) {
        viewModelScope.launch {
            qiitaArtRepo.storeArticles(query)
            val articles = qiitaArtRepo.getAll()
            _articles.postValue(articles)
        }
    }

    private fun fetchRssChannels() {
        viewModelScope.launch {
            val channels = rssRepo.getChannels()
            _rssChannels.postValue(channels)
        }
    }

    private fun fetchRssitems() {
        viewModelScope.launch {
            val allItems = mutableListOf<RssItem>()
            rssRepo.getChannels().forEach {channel ->
                val items = rssRepo.getItemsByChannelId(channel.id)
                allItems.addAll(items)
            }
            _rssItems.postValue(allItems)
        }
    }

    private fun fetchFollowArticles() {
        viewModelScope.launch {
            val articles = followArticleRepo.getAll()
            _followArticle.postValue(articles)
        }
    }

    private fun storeArticle(article: FollowArticle) {
        viewModelScope.launch {
            followArticleRepo.storeArticle(article)
        }
    }

    private fun deleteArticle(article: FollowArticle) {
        viewModelScope.launch {
            followArticleRepo.deleteArticle(article)
        }
    }
}