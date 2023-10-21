package com.example.techjunction.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rss_items")
data class RssItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
//    @ColumnInfo("channel_id") val channelId: Int,
    val title: String,
    val description: String,
    val link: String,
    @ColumnInfo("pub_date") val pubDate: Long?
)