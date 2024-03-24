package com.example.techjunction.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.QIITA
import com.example.techjunction.constants.ZENN
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
class ArticlesViewModel(application: Application): ViewModel() {
    companion object {
        private val mutex = Mutex()

        fun provideFactory(
            application: Application
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ArticlesViewModel(application) as T
            }
        }
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
    private val _followArticles = MutableLiveData<List<FollowArticle>>()
    val followArticles: LiveData<List<FollowArticle>> = _followArticles
    private var _searchArticles = MutableLiveData<List<SearchArticle>>()
    val searchArticles: LiveData<List<SearchArticle>> = _searchArticles

    init {
        fetchQiitaArticles("kotlin")
        fetchRssChannels()
        fetchRssitems()
        fetchFollowArticles()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchQiitaArticles(query: String) {
        viewModelScope.launch {
            mutex.withLock {
                withContext(Dispatchers.IO) {
                    qiitaArtRepo.storeArticles(query)
                    val articles = qiitaArtRepo.getAll()
                    _articles.postValue(articles)
                }
            }
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

    fun fetchFollowArticles() {
        viewModelScope.launch {
            val articles = followArticleRepo.getAll()
            _followArticles.postValue(articles)
        }
    }

    fun storeArticle(article: FollowArticle) {
        viewModelScope.launch {
            followArticleRepo.storeArticle(article)
        }
    }

    fun deleteArticle(article: FollowArticle) {
        viewModelScope.launch {
            followArticleRepo.deleteArticle(article)
            fetchFollowArticles()
        }
    }

    fun getAllByQuery(query: String) {
        viewModelScope.launch {
            val qiitaArticles = qiitaArtRepo.getAllByQuery(query)
            val rssItem = rssRepo.getAllByQuery(query)
            val rssChannel = rssRepo.getChannels()
            val searchList = mutableListOf<SearchArticle>()
            val tmpList = mutableListOf<Any>()
            tmpList.addAll(
                qiitaArticles +
                        rssItem
            )
            tmpList.forEach { item ->
                when (item) {
                    is QiitaArticle -> {
                        searchList.add(
                            SearchArticle(
                                item.title,
                                item.body,
                                item.url,
                                QIITA
                            )
                        )
                    }
                    is RssItem -> {
                        rssChannel.forEach { channel ->
                            if (channel.id == item.channelId) {
                                val channelName = when (channel.rssUrl) {
                                    CHANNEL_URL_ZENN -> ZENN
                                    CHANNEL_URL_HATENA -> HATENA
                                    else -> throw IllegalArgumentException("Invalid rssUrl")
                                }
                                searchList.add(
                                    SearchArticle(
                                        item.title,
                                        item.description,
                                        item.link,
                                        channelName
                                    )
                                )
                            }
                        }
                    }
                }
            }
            _searchArticles.postValue(searchList)
        }
    }
}

data class SearchArticle(
    val title: String,
    val description: String?,
    val url: String,
    val channel: String
)