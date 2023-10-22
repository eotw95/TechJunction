package com.example.techjunction.room

import java.util.Date

class RssRepositoryImpl(private val db: RssDatabase): RssRepository {
    override suspend fun getChannels(): List<RssChannel> {
        val dao = db.rssChannelDao()
        return dao.getAll()
    }

    override suspend fun findChannelByUrl(rssUrl: String): RssChannel? {
        val dao = db.rssChannelDao()
        return dao.findChannelByUrl(rssUrl)
    }

    override suspend fun getItemsByChannelId(channelId: Int): List<RssItem> {
        TODO("Not yet implemented")
    }

    override suspend fun findItemByUrl(channelId: Int, link: String): RssItem? {
        val dao = db.rssItemDao()
        return dao.findItemByUrl(channelId, link)
    }

    override suspend fun insertOrUpdateChannel(
        rssUrl: String,
        title: String?,
        desc: String?,
        link: String?,
        latestDate: Date
    ) {
        val dao = db.rssChannelDao()

        val channel: RssChannel? = findChannelByUrl(rssUrl)
        if (channel == null) {
            dao.insert(
                RssChannel(
                0,
                rssUrl,
                title,
                desc,
                link,
                latestDate.time
                )
            )
        } else {
            channel.also {
                it.title = title
                it.description = desc
                it.link = link
                it.latestDate = latestDate.time
            }
            dao.update(channel)
        }
    }

    override suspend fun insertOrUpdateItem(
        rssUrl: String,
        channelId: Int,
        title: String,
        desc: String,
        link: String,
        latestDate: Date
    ) {
        val channelDao = db.rssChannelDao()
        val itemDao = db.rssItemDao()

        // Todo: itemをinsertするタイミングでchannelが無い状態ってありえるのか
        val channel: RssChannel? = channelDao.findChannelByUrl(rssUrl)
        if (channel == null) {
            channelDao.insert(
                RssChannel(
                    0,
                    rssUrl,
                    null,
                    null,
                    null,
                    latestDate.time
                )
            )
        } else {
            val channelId = channel.id
            val item: RssItem? = itemDao.findItemByUrl(channelId, link)
            if (item == null) {
                itemDao.insert(
                    RssItem(
                        0,
                        channelId,
                        title,
                        desc,
                        link,
                        latestDate.time
                    )
                )
            } else {
                item.also {
                    it.title = title
                    it.description = desc
                    it.link = link
                    it.pubDate = latestDate.time
                }
                itemDao.update(item)
            }
        }
    }
}