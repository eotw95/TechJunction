package com.example.techjunction.network

import android.util.Log
import com.example.techjunction.network.model.QiitaArticlesResponse
import com.example.techjunction.room.Result
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class QiitaApiDataSourceImpl: QiitaApiDataSource {
    companion object {
        val TAG = "QiitaApiDataSourceImpl"
        val retrofit = Retrofit.Builder()
            .baseUrl(QiitaApi.BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(QiitaApi::class.java)
    }
    override suspend fun getArticlesByQuery(query: String): Result<List<QiitaArticlesResponse>> {
        val response = retrofit.fetchQiitaArticles(query)
        if (response.isSuccessful) {
            Log.d(TAG, "success http request. response=${response.body()}")
            return response.body()?.let {
                Result.Success(it)
            } ?: Result.Error(NullPointerException("value is null"))
        } else {
            Log.e(TAG, "fail http request. errorCode=${response.code()}")
            throw HttpException()
        }
    }
}

class HttpException: Throwable()