package com.example.techjunction.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FollowArticleDao {
    @Query("SELECT * FROM follow_article_database")
    suspend fun getAll(): List<FollowArticle>

    @Insert
    suspend fun insert(article: FollowArticle)

    @Delete
    suspend fun delete(article: FollowArticle)
}