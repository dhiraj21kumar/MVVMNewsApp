package com.androiddevs.myPhase2.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.myPhase2.ui.models.Article


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM news")
    fun getAllArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}