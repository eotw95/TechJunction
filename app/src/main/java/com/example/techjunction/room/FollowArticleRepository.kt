package com.example.techjunction.room

interface FollowArticleRepository {
    suspend fun getAll(): List<FollowArticle>

    suspend fun storeArticle(article: FollowArticle)

    suspend fun deleteArticle(article: FollowArticle)
}