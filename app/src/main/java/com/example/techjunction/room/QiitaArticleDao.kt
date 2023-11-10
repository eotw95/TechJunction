package com.example.techjunction.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QiitaArticleDao {
    @Query("SELECT * FROM qiita_articles")
    suspend fun getAll(): List<QiitaArticle>

    @Insert
    suspend fun insert(article: QiitaArticle)

    @Update
    suspend fun update(article: QiitaArticle)
}