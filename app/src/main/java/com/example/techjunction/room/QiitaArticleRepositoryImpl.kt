package com.example.techjunction.room

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.techjunction.network.QiitaApiDataSourceImpl
import com.example.techjunction.network.model.asDatabaseModel

class QiitaArticleRepositoryImpl(private val db: QiitaArticleDatabase): QiitaArticleRepository {

    private val qiitaApiDataSource = QiitaApiDataSourceImpl()

    override suspend fun getAll(): List<QiitaArticle> {
        val dao = db.qiitaArticleDao()
        return dao.getAll()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun storeArticles(query: String) {
        val dao = db.qiitaArticleDao()
        val articles = qiitaApiDataSource.getArticlesByQuery(query)
        dao.deleteAll()
        articles.forEach { article ->
            dao.insert(article.asDatabaseModel())
        }
    }

    override suspend fun getAllByQuery(query: String): List<QiitaArticle> {
        val dao = db.qiitaArticleDao()
        println("dao.getAllByQuery(query)=${dao.getAllByQuery(query)}")
        return dao.getAllByQuery(query)
    }
}