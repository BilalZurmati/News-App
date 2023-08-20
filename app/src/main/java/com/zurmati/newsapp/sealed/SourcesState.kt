package com.zurmati.newsapp.sealed

import com.zurmati.newsapp.models.SourcesResponse

sealed class SourcesState {
    object Empty : SourcesState()
    class Error(val message: String) : SourcesState()
    class Data(val response: SourcesResponse) : SourcesState()
}
