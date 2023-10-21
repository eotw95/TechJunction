package com.example.techjunction.domain

import com.example.techjunction.room.RssRepository

class RdfTag(name: String): Tag(name) {

    override fun createChildTag(name: String): Tag {
        return when(name) {
            "channel" -> RdfChannelTag(name)
            "item" -> RdfItemTag(name)
            else -> super.createChildTag(name)
        }
    }
    override fun handleChildTagEnd(tag: Tag, repo: RssRepository) {
        TODO("Not yet implemented")
    }
}

private class RdfChannelTag(name: String): Tag(name) {
    var title: String? = null
    var desc: String? = null
    var link: String? = null
    override fun handleChildTagEnd(tag: Tag, repo: RssRepository) {
        when(tag.name) {
            "title" -> title = tag.text
            "desc" -> desc = tag.text
            "link" -> link = tag.text
        }
    }
}

private class RdfItemTag(name: String): Tag(name) {
    override fun handleChildTagEnd(tag: Tag, repo: RssRepository) {
        TODO("Not yet implemented")
    }
}