package com.example.techjunction.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rss_items")
data class RssItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("channel_id") val channelId: Int,
    var title: String,
    var description: String,
    var link: String,
    @ColumnInfo("img_src") val imgSrc: String,
    @ColumnInfo("pub_date") var pubDate: Long?
)