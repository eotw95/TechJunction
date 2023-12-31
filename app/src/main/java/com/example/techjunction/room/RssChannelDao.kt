package com.example.techjunction.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RssChannelDao {
    @Query("SELECT * FROM rss_channels ORDER BY latest_date DESC")
    suspend fun getAll(): List<RssChannel>

    @Query("SELECT * FROM rss_channels WHERE rss_url = :rssUrl")
    suspend fun findChannelByUrl(rssUrl: String): RssChannel?

    @Insert
    suspend fun insert(channel: RssChannel)

    @Update
    suspend fun update(channel: RssChannel)
}