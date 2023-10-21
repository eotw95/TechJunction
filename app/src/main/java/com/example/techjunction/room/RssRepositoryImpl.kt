package com.example.techjunction.room

import android.content.Context

class RssRepositoryImpl(private val db: RssDatabase): RssRepository {
    override suspend fun fetchChannels(): List<RssChannel> {
        val dao = db.rssChannelDao()
        return dao.getAll()
    }

    override suspend fun fetchItemsByChannelId(channelId: Int): List<RssItem> {
        val dao = db.rssItemDao()
        return fetchItemsByChannelId(channelId)
    }

    override suspend fun insertOrUpdateChannel(channel: RssChannel) {
        val dao = db.rssChannelDao()
        if (fetchChannels().isEmpty()) {
            dao.insert(channel)
        } else {
            dao.update(channel)
        }
    }

    override suspend fun insertOrUpdateItem(item: RssItem) {
        val dao = db.rssItemDao()
        if (fetchItemsByChannelId(item.channelId).isEmpty()) {
            dao.insert(item)
        } else {
            dao.update(item)
        }
    }
}