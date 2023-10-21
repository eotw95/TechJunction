package com.example.techjunction.domain

import com.example.techjunction.room.RssChannel
import com.example.techjunction.room.RssItem
import com.example.techjunction.room.RssRepository
import java.util.Date

open class RdfTag(name: String, val rssUrl: String): Tag(name) {

    override fun createChildTag(name: String): Tag {
        return when(name) {
            "channel" -> RdfChannelTag(name, rssUrl)
            "item" -> RdfItemTag(name, rssUrl)
            else -> super.createChildTag(name)
        }
    }
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        when(tag) {
            is RdfChannelTag -> repo.insertOrUpdateChannel(tag.asDatabaseModel(tag))
            is RdfItemTag -> repo.insertOrUpdateItem(tag.asDatabaseModel(tag))
        }
    }
}

private class RdfChannelTag(name: String, rssUrl: String): RdfTag(name, rssUrl) {
    var title: String = ""
    var desc: String = ""
    var link: String = ""
    var latestDate: Date = Date()
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        when(tag.name) {
            "title" -> title = tag.text
            "desc" -> desc = tag.text
            "link" -> link = tag.text
        }
        latestDate = date
    }
}

private class RdfItemTag(name: String, rssUrl: String): RdfTag(name, rssUrl) {
    var title: String = ""
    var desc: String = ""
    var link: String = ""
    var latestDate: Date = Date()
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        when(tag.name) {
            "title" -> title = tag.text
            "desc" -> desc = tag.text
            "link" -> link = tag.text
        }
        latestDate = date
    }
}

private fun RdfChannelTag.asDatabaseModel(tag: RdfChannelTag):  RssChannel{
    return RssChannel(
        id = 0,
        rssUrl = tag.rssUrl,
        title = tag.title,
        description = tag.desc,
        link = tag.link,
        latestDate = tag.latestDate.time
    )
}

private fun RdfItemTag.asDatabaseModel(tag: RdfItemTag):  RssItem{
    return RssItem(
        id = 0,
        title = tag.title,
        description = tag.desc,
        link = tag.link,
        pubDate = tag.latestDate.time
    )
}