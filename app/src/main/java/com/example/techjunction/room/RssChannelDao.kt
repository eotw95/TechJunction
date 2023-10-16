package com.example.techjunction.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update

@Dao
interface RssChannelDao {
    @Insert
    suspend fun insert(channel: RssChannel)

    @Update
    suspend fun update(channel: RssChannel)
}