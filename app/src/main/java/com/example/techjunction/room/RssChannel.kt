package com.example.techjunction.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class RssChannel(
    @PrimaryKey(autoGenerate = true) val id: String,
    val title: String,
    val description: String,
    val link: String,
    @ColumnInfo("latest_date") val latestDate: Long?
)