package com.example.techjunction.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qiita_articles")
data class QiitaArticle(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val url: String,
    val user: User,
    @ColumnInfo("created_at") val createdDate: Long
) {
    data class User(
        val userId: String,
        val description: String?,
        @ColumnInfo("profile_image_url") val profileImageUrl: String
    )
}