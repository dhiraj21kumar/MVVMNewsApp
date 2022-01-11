package com.androiddevs.myPhase2.ui

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.myPhase2.ui.models.Article
import com.androiddevs.myPhase2.ui.models.NewsResponse
import com.androiddevs.myPhase2.ui.repository.NewsRepository
import com.androiddevs.myPhase2.ui.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
): ViewModel() {

    val topNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var topNewsPage = 1
    var topNewsResponse: NewsResponse? = null

    init {
        getTopNews("in")
    }

    fun getTopNews(countryCode: String) = viewModelScope.launch {
        topNews.postValue(Resource.Loading())

        val response = newsRepository.getTopNews(countryCode, topNewsPage)
        topNews.postValue( handleTopNewsResponse(response))
    }

    private fun handleTopNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                topNewsPage++
                if(topNewsResponse == null){
                    topNewsResponse = resultResponse
                }
                else{
                    val oldArticles = topNewsResponse?.articles
                    val newArticles = resultResponse.articles

                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: @NonNull com.androiddevs.mvvmnewsapp.ui.models.Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun savedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}
