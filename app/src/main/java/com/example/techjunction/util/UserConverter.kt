package com.example.techjunction.util

import androidx.room.TypeConverter
import com.example.techjunction.room.QiitaArticle
import com.google.gson.Gson

class UserConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromUser(user: QiitaArticle.User): String {
        return gson.toJson(user)
    }

    @TypeConverter
    fun toUser(json: String): QiitaArticle.User {
        return gson.fromJson(json, QiitaArticle.User::class.java)
    }
}