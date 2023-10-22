package com.example.techjunction.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RssItemDao {
    @Query("SELECT * FROM rss_items WHERE channel_id = :channelId ORDER BY pub_date DESC")
    suspend fun getAllByChannelId(channelId: Int): List<RssItem>

    @Query("SELECT * FROM rss_items WHERE link = :link")
    suspend fun findItemByUrl(link: String): RssItem?

    @Insert
    suspend fun insert(rssItem: RssItem)

    @Update
    suspend fun update(rssItem: RssItem)
}