package com.example.techjunction.room

interface QiitaArticleRepository {
    suspend fun getAll(): List<QiitaArticle>

    suspend fun insert(article: QiitaArticle)

    suspend fun update(article: QiitaArticle)
}