package com.example.techjunction.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "follow_article_database")
data class FollowArticle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val link: String
)