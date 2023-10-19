package com.example.techjunction.domain

abstract class Tag(name: String) {

    fun createChildTag(name: String): Tag {
        return OtherTag(name)
    }

    abstract fun handleChildTagEnd()
}

class OtherTag(name: String): Tag(name) {
    override fun handleChildTagEnd() {
        TODO("Not yet implemented")
    }
}