package com.example.techjunction.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qiita_articles")
data class QiitaArticle(
    @PrimaryKey val id: Int,
    val title: String,
    val url: String,
    val user: User
) {
    data class User(
        val id: String,
        @ColumnInfo("profile_image_url") val profileImageUrl: String
    )
}