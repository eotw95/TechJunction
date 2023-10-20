package com.example.techjunction.domain

class RdfTag(name: String): Tag(name) {

    override fun createChildTag(name: String): Tag {
        return when(name) {
            "channel" -> RdfChannelTag(name)
            "item" -> RdfItemTag(name)
            else -> super.createChildTag(name)
        }
    }
    override fun handleChildTagEnd() {
        TODO("Not yet implemented")
    }
}

private class RdfChannelTag(name: String): Tag(name) {
    override fun handleChildTagEnd() {
        TODO("Not yet implemented")
    }
}

private class RdfItemTag(name: String): Tag(name) {
    override fun handleChildTagEnd() {
        TODO("Not yet implemented")
    }
}