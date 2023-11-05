package com.example.techjunction.room

import java.util.Date

interface RssRepository {
    suspend fun getChannels(): List<RssChannel>

    suspend fun findChannelByUrl(rssUrl: String): RssChannel?
    suspend fun getItemsByChannelId(channelId: Int): List<RssItem>

    suspend fun findItemByUrl(channelId: Int, link: String): RssItem?

    suspend fun insertOrUpdateChannel(
        rssUrl: String,
        title: String?,
        desc: String?,
        link: String?,
        latestDate: Date
    )

    suspend fun insertOrUpdateItem(
        rssUrl: String,
        title: String,
        desc: String,
        link: String,
        imgSrc: String,
        latestDate: Date
    )
}