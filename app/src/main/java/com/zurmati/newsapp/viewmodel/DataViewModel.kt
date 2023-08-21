package com.zurmati.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurmati.newsapp.network.RetrofitClient
import com.zurmati.newsapp.models.Articles
import com.zurmati.newsapp.models.NewsResponse
import com.zurmati.newsapp.models.SourcesResponse
import com.zurmati.newsapp.sealed.NewsState
import com.zurmati.newsapp.sealed.SourcesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class DataViewModel : ViewModel() {
    var sources = MutableStateFlow<SourcesState>(SourcesState.Empty)
    var news = MutableStateFlow<NewsState>(NewsState.Empty)
    var selectedArticle = MutableLiveData<Articles>()


    fun setSelectedNews(news: Articles) {
        selectedArticle.postValue(news)
    }


    fun fetchSources() {
        viewModelScope.launch(Dispatchers.IO) {


            RetrofitClient.instance.getSources().enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    if (response.isSuccessful)
                        sources.value = SourcesState.Data(response.body()!!)
                    else
                        sources.value = SourcesState.Error(response.message())
                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    sources.value = SourcesState.Error(t.message!!)
                }
            })
        }
    }

    fun fetchTopHeadlinesFrom(source: String?) {
        source?.let {
            news.value = NewsState.Loading

            viewModelScope.launch(Dispatchers.IO) {
                RetrofitClient.instance.getTopHeadlinesFromSource(it)
                    .enqueue(object : Callback<NewsResponse> {
                        override fun onResponse(
                            call: Call<NewsResponse>,
                            response: Response<NewsResponse>
                        ) {
                            if (response.isSuccessful) {
                                news.value = NewsState.Data(response.body()!!)
                            } else
                                news.value = NewsState.Error(response.message())
                        }

                        override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                            news.value = NewsState.Error(t.message!!)
                        }

                    })
            }
        }

    }

}