package com.example.techjunction.repository

import android.util.Log
import com.example.techjunction.network.QiitaApiDataSourceImpl
import com.example.techjunction.network.model.QiitaArticlesResponse
import retrofit2.http.Query

class QiitaArticlesRepository {
    companion object {
        private const val TAG = "QiitaArticlesRepository"
    }

    private val qiitaApiDataSource = QiitaApiDataSourceImpl()

    suspend fun fetchQiitaArticles(query: String): List<QiitaArticlesResponse> {
        Log.d(TAG, "fetchQiitaArticles ret=${qiitaApiDataSource.fetchQiitaArticles(query)}")
        return qiitaApiDataSource.fetchQiitaArticles(query)
    }
}