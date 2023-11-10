package com.example.techjunction.room

interface QiitaArticleRepository {
    suspend fun getAll(): List<QiitaArticle>

    suspend fun getAllByQuery(query: String): List<QiitaArticle>

    suspend fun insertOrUpdate(article: QiitaArticle)
}