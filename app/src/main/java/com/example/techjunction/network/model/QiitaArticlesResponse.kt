package com.example.techjunction.network.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.techjunction.room.QiitaArticle
import com.example.techjunction.util.DateConverter
import com.squareup.moshi.Json

data class QiitaArticlesResponse(
    val title: String,
    val url: String,
    val user: User,
    @Json(name = "created_at") val createdDate: String
) {
    data class User(
        val id: String,
        @Json(name = "profile_image_url") val profileImageUrl: String
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun QiitaArticlesResponse.asDatabaseModel(): QiitaArticle {
    return QiitaArticle(
        0,
        this.title,
        this.url,
        QiitaArticle.User(
            this.user.id,
            this.user.profileImageUrl
        ),
        DateConverter.asDate(this.createdDate).time
    )
}