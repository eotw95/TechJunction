package com.example.techjunction.room

class FollowArticleRepositoryImpl(private val db: FollowArticleDatabase): FollowArticleRepository {

    private val dao = db.FollowArticleDao()

    override suspend fun getAll(): List<FollowArticle> {
        return dao.getAll()
    }

    override suspend fun storeArticle(article: FollowArticle) {
        val currentArticles = getAll()
        currentArticles.forEach {
            if (it.link == article.link) {
                return
            }
        }
        dao.insert(article)
    }

    override suspend fun deleteArticle(article: FollowArticle) {
        dao.delete(article)
    }
}