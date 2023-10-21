package com.example.techjunction.domain

import com.example.techjunction.room.RssRepository

abstract class Tag(val name: String) {

    var text: String? = null

    open fun createChildTag(name: String): Tag {
        return OtherTag(name)
    }

    abstract fun handleChildTagEnd(tag: Tag, repo: RssRepository)
}

// コンストラクタにメンバ変数でrssUrlを持つ必要あるか？
// rssUrlでどのRootTagかを判別できるようにするため？
class RootTag(private val rssUrl: String): Tag("Root") {

    override fun createChildTag(name: String): Tag {
        return when (name) {
            "rss" -> RssTag(name)
            "rdf:RDF" -> RdfTag(name)
            else -> throw java.lang.RuntimeException("not supported tag [$name]")
        }
    }
    override fun handleChildTagEnd(tag: Tag, repo: RssRepository) {
        // do nothing
    }

}

class OtherTag(name: String): Tag(name) {
    override fun handleChildTagEnd(tag: Tag, repo: RssRepository) {
        TODO("Not yet implemented")
    }
}