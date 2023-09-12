package com.example.techjunction.network.model

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