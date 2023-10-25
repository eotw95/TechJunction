package com.example.techjunction.domain

import com.example.techjunction.room.RssRepository
import java.util.Date

abstract class Tag(val name: String) {

    var text: String = ""

    open fun createChildTag(name: String): Tag {
        return OtherTag(name)
    }

    abstract suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date)
}

// コンストラクタにメンバ変数でrssUrlを持つ必要あるか？
// rssUrlでどのRootTagかを判別できるようにするため？
class RootTag(private val rssUrl: String): Tag("Root") {

    override fun createChildTag(name: String): Tag {
        return when (name) {
            "rss" -> RssTag(name, rssUrl)
            "rdf:RDF" -> RdfTag(name, rssUrl)
            else -> throw java.lang.RuntimeException("not supported tag [$name]")
        }
    }
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        // do nothing
    }

}

class OtherTag(name: String): Tag(name) {
    override suspend fun handleChildTagEnd(tag: Tag, repo: RssRepository, date: Date) {
        TODO("Not yet implemented")
    }
}