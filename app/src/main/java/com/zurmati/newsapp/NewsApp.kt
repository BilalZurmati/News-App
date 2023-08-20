package com.zurmati.newsapp

import android.app.Application
import android.content.Context

class NewsApp : Application() {

    companion object {

        private lateinit var mInstance: NewsApp

        fun getAppContext(): Context {
            return mInstance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }
}