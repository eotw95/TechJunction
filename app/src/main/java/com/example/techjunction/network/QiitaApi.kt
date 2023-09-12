package com.example.techjunction.network

import com.example.techjunction.network.model.QiitaArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QiitaApi {
    companion object {
        const val BASE_URL = "https://qiita.com/"
    }
    @GET("/api/v2/items?page=1&per_page=5")
    suspend fun fetchQiitaArticles(@Query("query") query: String): Response<List<QiitaArticlesResponse>>
}