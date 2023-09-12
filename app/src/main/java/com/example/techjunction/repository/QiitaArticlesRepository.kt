package com.example.techjunction.repository

import com.example.techjunction.network.QiitaApiDataSourceImpl
import com.example.techjunction.network.model.QiitaArticlesResponse
import retrofit2.http.Query

class QiitaArticlesRepository {
    private val qiitaApiDataSource = QiitaApiDataSourceImpl()

    suspend fun fetchQiitaArticles(query: String): QiitaArticlesResponse {
        return qiitaApiDataSource.fetchQiitaArticles(query)
    }
}