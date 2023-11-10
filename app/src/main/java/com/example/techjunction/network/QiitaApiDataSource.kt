package com.example.techjunction.network

import com.example.techjunction.network.model.QiitaArticlesResponse
import retrofit2.http.Query

interface QiitaApiDataSource {
    suspend fun getArticlesByQuery(query: String): List<QiitaArticlesResponse>
}