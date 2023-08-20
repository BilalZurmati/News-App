package com.zurmati.newsapp.helpers

import android.content.Context
import android.content.SharedPreferences
import com.zurmati.newsapp.NewsApp.Companion.getAppContext

object SharedPref {
    private lateinit var sharedPref: SharedPreferences

    private fun init() {
        if (!::sharedPref.isInitialized)
            sharedPref = getAppContext().getSharedPreferences("NewsAppPref", Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String) {
        init()
        sharedPref.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        init()
        return sharedPref.getString(key, defaultValue)!!
    }
}