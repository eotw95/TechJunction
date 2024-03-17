package com.example.techjunction.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.techjunction.room.RssRepository
import com.example.techjunction.util.DateConverter
import java.util.Date

class RdfTag(name: String, private val rssUrl: String): Tag(name) {

    override fun createChildTag(name: String): Tag {
        return when(name) {
            "channel" -> RdfChannelTag(name)
            "item" -> RdfItemTag(name)
            else -> super.createChildTag(name)
        }
    }
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        when(tag) {
            is RdfChannelTag -> repo.insertOrUpdateChannel(
                rssUrl,
                tag.title,
                tag.desc,
                tag.link,
                date
            )
            is RdfItemTag -> repo.insertOrUpdateItem(
                rssUrl,
                tag.title,
                tag.desc,
                tag.link,
                tag.imgSrc,
                tag.pubDate
            )
        }
    }
}

private class RdfChannelTag(name: String): Tag(name) {
    var title: String = ""
    var desc: String = ""
    var link: String = ""
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        when(tag.name) {
            "title" -> title = tag.text
            "description" -> desc = tag.text
            "link" -> link = tag.text
        }
    }
}

private class RdfItemTag(name: String): Tag(name) {
    var title: String = ""
    var desc: String = ""
    var link: String = ""
    var imgSrc: String = ""
    var pubDate: Date = Date()
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        when(tag.name) {
            "title" -> title = tag.text
            "description" -> desc = tag.text
            "link" -> link = tag.text
            "hatena:imageurl" -> imgSrc = tag.text
            "dc:date" -> pubDate = DateConverter.asDate(tag.text)
        }
    }
}