package com.example.techjunction.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.techjunction.room.RssRepository
import com.example.techjunction.util.DateConverter
import java.util.Date

class RssTag(name: String, private val rssUrl: String): Tag(name) {
    override fun createChildTag(name: String): Tag {
        return when(name) {
            "channel" -> RssChannelTag(name, rssUrl)
            else -> super.createChildTag(name)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        if (tag is RssChannelTag) {
            repo.insertOrUpdateChannel(
                rssUrl,
                tag.title,
                tag.desc,
                tag.link,
                DateConverter.asDate(tag.lastBuildDate)
            )
        }
    }
}

private class RssChannelTag(name: String, private val channelLink: String): Tag(name) {
    var title = ""
    var desc = ""
    var link = ""
    var lastBuildDate = ""

    override fun createChildTag(name: String): Tag {
        return when(name) {
            "item" -> RssItemTag(name)
            else -> super.createChildTag(name)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        if (tag is RssItemTag) {
            repo.insertOrUpdateItem(
                channelLink,
                tag.title,
                tag.desc,
                tag.link,
                tag.imgSrc,
                DateConverter.asDate(tag.pubDate)
            )
        } else {
            when(tag.name) {
                "title" -> title = tag.text
                "description" -> desc = tag.text
                "link" -> link = tag.text
                "lastBuildDate" -> lastBuildDate = tag.text
            }
        }
    }
}

private class RssItemTag(name: String): Tag(name) {
    var title = ""
    var desc = ""
    var link = ""
    var imgSrc = ""
    var pubDate = ""
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        when(tag.name) {
            "title" -> title = tag.text
            "description" -> desc = tag.text
            "link" -> link = tag.text
            "enclosure" -> imgSrc = tag.attribute
            "pubDate" -> pubDate = tag.text
        }
    }
}