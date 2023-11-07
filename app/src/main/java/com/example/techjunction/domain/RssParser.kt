package com.example.techjunction.domain

import com.example.techjunction.room.RssRepository
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.util.Date

class RssParser(private val repo: RssRepository) {
    companion object {
        const val START_TAG = XmlPullParser.START_TAG
        const val TEXT = XmlPullParser.TEXT
        const val END_TAG = XmlPullParser.END_TAG
        const val END_DOCUMENT = XmlPullParser.END_DOCUMENT
        const val ENCLOSURE = "enclosure"
    }

    suspend fun parse(input: InputStream, rssUrl: String) {
        val stack = ArrayDeque<Tag>(listOf(RootTag(rssUrl)))
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(input, null)
        var event = parser.eventType
        while (event != END_DOCUMENT) {
            when (event) {
                START_TAG -> {
                    val childTag = stack.first().createChildTag(parser.name)
                    when (parser.name) {
                        ENCLOSURE -> {
                            childTag.attribute = parser.getAttributeValue(null, "url")
                        }
                    }
                    stack.addFirst(childTag)
                }
                TEXT -> {
                    stack.first().text = parser.text.trim()
                }
                END_TAG -> {
                    val tag = stack.removeFirst()
                    stack.first().handleChildTagEnd(tag, repo, Date())
                }
            }
            event = parser.next()
        }
    }
}