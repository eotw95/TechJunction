package com.example.techjunction.room

import com.example.techjunction.network.QiitaApiDataSourceImpl
import com.example.techjunction.network.model.asDatabaseModel

class QiitaArticleRepositoryImpl(private val db: QiitaArticleDatabase): QiitaArticleRepository {

    private val qiitaApiDataSource = QiitaApiDataSourceImpl()

    override suspend fun getAll(limit: Int): List<QiitaArticle> {
        val dao = db.qiitaArticleDao()
        return dao.getAll(limit)
    }

    override suspend fun storeArticles(query: String) {
        val dao = db.qiitaArticleDao()
        val articles = qiitaApiDataSource.getArticlesByQuery(query)
        dao.deleteAll()
        articles.forEach { article ->
            dao.insert(article.asDatabaseModel())
        }
    }
}