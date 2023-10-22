package com.example.techjunction.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "rss_channels")
data class RssChannel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("rss_url") val rssUrl: String,
    var title: String?,
    var description: String?,
    var link: String?,
    @ColumnInfo("latest_date") var latestDate: Long?
)