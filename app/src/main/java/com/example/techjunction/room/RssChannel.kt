package com.example.techjunction.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "rss_channels")
data class RssChannel(
    @PrimaryKey(autoGenerate = true) val id: String,
    @ColumnInfo("rss_url") val rssUrl: String,
    val title: String,
    val description: String,
    val link: String,
    @ColumnInfo("latest_date") val latestDate: Long?
)