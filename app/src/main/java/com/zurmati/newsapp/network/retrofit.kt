package com.zurmati.newsapp.network

import com.zurmati.newsapp.models.NewsResponse
import com.zurmati.newsapp.models.SourcesResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines/sources?apiKey=30a00fb4dced4a22beb453b5f00c5e3b")
    fun getSources(): Call<SourcesResponse>

    @GET("top-headlines?apiKey=30a00fb4dced4a22beb453b5f00c5e3b")
    fun getTopHeadlinesFromSource(@Query("sources") source: String): Call<NewsResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://newsapi.org/v2/"

    val instance: NewsApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(NewsApiService::class.java)
    }
}