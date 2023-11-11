package com.example.techjunction.room

interface QiitaArticleRepository {
    suspend fun getAll(): List<QiitaArticle>

    suspend fun storeArticles(query: String)
}