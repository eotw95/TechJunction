package com.example.techjunction.network.model

import com.example.techjunction.room.QiitaArticle
import com.squareup.moshi.Json

data class QiitaArticlesResponse(
    val title: String,
    val url: String,
    val user: User
) {
    data class User(
        val id: String,
        @Json(name = "profile_image_url") val profileImageUrl: String
    )
}

fun QiitaArticlesResponse.asDatabaseModel(): QiitaArticle {
    return QiitaArticle(
        0,
        this.title,
        this.url,
        QiitaArticle.User(
            0,
            this.user.profileImageUrl
        )
    )
}