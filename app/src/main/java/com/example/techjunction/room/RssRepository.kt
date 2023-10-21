package com.example.techjunction.room

interface RssRepository {
    suspend fun fetchChannels(): List<RssChannel>

    suspend fun  fetchItemsByChannelId(channelId: Int): List<RssItem>

    suspend fun insertOrUpdateChannel(channel: RssChannel)

    suspend fun insertOrUpdateItem(item: RssItem)
}