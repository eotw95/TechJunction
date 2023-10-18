package com.example.techjunction.room

class RssRepositoryImpl: RssRepository {
    override suspend fun fetchChannels(): List<RssChannel> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchItemsByChannelId(channelId: Int): List<RssItem> {
        TODO("Not yet implemented")
    }

    override suspend fun insertChannel(channel: RssChannel) {
        TODO("Not yet implemented")
    }

    override suspend fun insertItem(item: RssItem) {
        TODO("Not yet implemented")
    }

    override suspend fun updateChannel(channel: RssChannel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateItem(item: RssItem) {
        TODO("Not yet implemented")
    }
}