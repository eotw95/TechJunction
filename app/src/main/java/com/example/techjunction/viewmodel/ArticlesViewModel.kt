package com.example.techjunction.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techjunction.network.QiitaApiDataSourceImpl
import com.example.techjunction.room.QiitaArticle
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

    private val qiitaApiDataSource = QiitaApiDataSourceImpl()
    private val qiitaArtRepo = QiitaArticleRepositoryImpl()
    private val rssRepo = RssRepositoryImpl(RssDatabase.getInstance(application))
    private var _articles = MutableLiveData<List<QiitaArticle>>()
    val articles: LiveData<List<QiitaArticle>> = _articles
    private var _rssChannels = MutableLiveData<List<RssChannel>>()
    val rssChannels: LiveData<List<RssChannel>> = _rssChannels
    private val _rssItems = MutableLiveData<List<RssItem>>()
    val rssItems: LiveData<List<RssItem>> = _rssItems

    init {
        fetchQiitaArticles("kotlin")
        fetchRssChannels()
        fetchRssitems()
    }

    private fun fetchQiitaArticles(query: String) {
        viewModelScope.launch {
            getAndStoreQiitaArticles(query)
            val articles = qiitaArtRepo.getAllByQuery(query)
            _articles.postValue(articles)
        }
    }
    private fun getAndStoreQiitaArticles(query: String) {
        viewModelScope.launch {
            val articles = qiitaApiDataSource.getArticlesByQuery(query)
            // Todo: 取得したQiita APIのレスポンスデータをDBに保存する。 qiitaArtRepo.insertOrUpdate()
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
}