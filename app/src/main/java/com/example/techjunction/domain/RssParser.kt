package com.example.techjunction.domain

import com.example.techjunction.room.RssRepository
import java.io.InputStream

class RssParser(private val repo: RssRepository) {

    suspend fun parse(input: InputStream, url: String) {
        // Todo: fileのXMLを解析
    }
}