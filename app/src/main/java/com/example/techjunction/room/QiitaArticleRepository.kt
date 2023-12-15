package com.example.techjunction.room

interface QiitaArticleRepository {
    suspend fun getAll(limit: Int): List<QiitaArticle>

    suspend fun storeArticles(query: String)
}