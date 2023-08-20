package com.zurmati.newsapp.sealed

import com.zurmati.newsapp.models.NewsResponse

sealed class NewsState {
    object Empty : NewsState()
    object Loading : NewsState()
    class Error(val message: String) : NewsState()
    class Data(val response: NewsResponse) : NewsState()
}