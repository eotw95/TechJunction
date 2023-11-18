package com.example.techjunction.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QiitaArticleDao {
    @Query("SELECT * FROM qiita_articles LIMIT 5")
    suspend fun getAll(): List<QiitaArticle>

    @Query("DELETE FROM qiita_articles")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(article: QiitaArticle)

    @Update
    suspend fun update(article: QiitaArticle)
}