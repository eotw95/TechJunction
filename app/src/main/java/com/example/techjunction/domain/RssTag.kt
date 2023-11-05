package com.example.techjunction.domain

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.techjunction.room.RssRepository
import com.example.techjunction.util.DateConverter
import java.util.Date

class RssTag(name: String, private val rssUrl: String): Tag(name) {
    companion object {
        const val TAG = "RssTag"
    }

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
    companion object {
        const val TAG = "RssChannelTag"
    }

    var title = ""
    var desc = ""
    var link = ""
    var lastBuildDate = ""

    override fun createChildTag(name: String): Tag {
        Log.d(RssTag.TAG, "createChildTag() name=$name")
        return when(name) {
            "item" -> RssItemTag(name)
            else -> super.createChildTag(name)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        if (tag is RssItemTag) {
            // Todo: Zennのchannelのデータがitemとして登録されているバグが起きているので修正
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
    companion object {
        const val TAG = "RssItemTag"
    }
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
            //Todo: get imgSrc text
            "enclosure url" -> {
                println("RssItemTag handleChildTagEnd() enclosure url=${tag.text}")
                imgSrc = tag.text
            }
            "pubDate" -> pubDate = tag.text
        }
    }
}