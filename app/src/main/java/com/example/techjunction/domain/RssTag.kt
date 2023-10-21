package com.example.techjunction.domain

import com.example.techjunction.room.RssRepository

class RssTag(name: String): Tag(name) {

    override fun createChildTag(name: String): Tag {
        return when(name) {
            "channel" -> RssChannelTag(name)
            else -> super.createChildTag(name)
        }
    }
    override fun handleChildTagEnd(tag: Tag, repo: RssRepository) {
        TODO("Not yet implemented")
    }
}

private class RssChannelTag(name: String): Tag(name) {

    override fun createChildTag(name: String): Tag {
        return when(name) {
            "item" -> RssItemTag(name)
            else -> super.createChildTag(name)
        }
    }
    override fun handleChildTagEnd(tag: Tag, repo: RssRepository) {
        TODO("Not yet implemented")
    }
}

private class RssItemTag(name: String): Tag(name) {
    override fun handleChildTagEnd(tag: Tag, repo: RssRepository) {
        TODO("Not yet implemented")
    }
}