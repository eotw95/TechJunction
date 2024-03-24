package com.example.techjunction.screens.follow

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.techjunction.room.FollowArticle
import com.example.techjunction.room.FollowArticleDatabase
import com.example.techjunction.room.FollowArticleRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
class FollowArticlesViewModel(application: Application): ViewModel() {
    companion object {
        private val mutex = Mutex()

        fun provideFactory(
            application: Application
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FollowArticlesViewModel(application) as T
            }
        }
    }

    private val followArticleRepo =
        FollowArticleRepositoryImpl(FollowArticleDatabase.getInstance(application))
    private val _followArticles = MutableLiveData<List<FollowArticle>>()
    val followArticles: LiveData<List<FollowArticle>> = _followArticles

    init {
        fetchFollowArticles()
    }

    fun fetchFollowArticles() {
        viewModelScope.launch {
            val articles = followArticleRepo.getAll()
            _followArticles.postValue(articles)
        }
    }

    fun deleteArticle(article: FollowArticle) {
        viewModelScope.launch {
            mutex.withLock(this) {
                withContext(Dispatchers.IO) {
                    followArticleRepo.deleteArticle(article)
                    fetchFollowArticles()
                }
            }
        }
    }
}