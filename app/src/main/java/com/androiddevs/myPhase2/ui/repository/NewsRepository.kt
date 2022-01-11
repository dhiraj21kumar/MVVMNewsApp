package com.androiddevs.myPhase2.ui.repository

import com.androiddevs.myPhase2.ui.api.RetrofitInstance
import com.androiddevs.myPhase2.ui.db.ArticleDatabase
import com.androiddevs.myPhase2.ui.models.Article

class NewsRepository(
    val db: ArticleDatabase
)  {

    suspend fun getTopNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getTopNews(countryCode, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}