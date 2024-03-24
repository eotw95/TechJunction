package com.example.techjunction.screens.search

import android.app.Application
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
import com.example.techjunction.room.QiitaArticle
import com.example.techjunction.room.QiitaArticleDatabase
import com.example.techjunction.room.QiitaArticleRepositoryImpl
import com.example.techjunction.room.RssDatabase
import com.example.techjunction.room.RssItem
import com.example.techjunction.room.RssRepositoryImpl
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class SearchArticlesViewModel(application: Application): ViewModel() {
    companion object {
        private val mutex = Mutex()

        fun provideFactory(
            application: Application
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchArticlesViewModel(application) as T
            }
        }
    }

    private val db = QiitaArticleDatabase.getInstance(application)
    private val qiitaArtRepo = QiitaArticleRepositoryImpl(db)
    private val rssRepo = RssRepositoryImpl(RssDatabase.getInstance(application))
    private var _searchArticles = MutableLiveData<List<SearchArticle>>()
    val searchArticles: LiveData<List<SearchArticle>> = _searchArticles

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