package com.example.branch_project.SharedPref

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    fun setAuthToken(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getAuthToken(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setThreadId(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getThreadId(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }


    fun setIsLoggedInKey(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getIsLoggedInKey(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}
