package com.example.techjunction.network

import com.example.techjunction.network.model.QiitaArticlesResponse
import com.example.techjunction.room.Result

interface QiitaApiDataSource {
    suspend fun getArticlesByQuery(query: String): Result<List<QiitaArticlesResponse>>
}