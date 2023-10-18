package com.example.techjunction.room

interface RssRepository {
    suspend fun fetchChannels(): List<RssChannel>

    suspend fun  fetchItemsByChannelId(channelId: Int): List<RssItem>

    suspend fun insertChannel(channel: RssChannel)

    suspend fun insertItem(item: RssItem)

    suspend fun updateChannel(channel: RssChannel)

    suspend fun updateItem(item: RssItem)
}