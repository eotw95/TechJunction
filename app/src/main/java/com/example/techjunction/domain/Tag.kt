package com.example.techjunction.domain

abstract class Tag(name: String) {

    open fun createChildTag(name: String): Tag {
        return OtherTag(name)
    }

    abstract fun handleChildTagEnd()
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
    override fun handleChildTagEnd() {
        // do nothing
    }

}

class OtherTag(name: String): Tag(name) {
    override fun handleChildTagEnd() {
        TODO("Not yet implemented")
    }
}