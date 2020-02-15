package com.example.movieticket.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.movieticket.R


object PrefUtils {

    fun getSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.user_pref_file_key),
            Context.MODE_PRIVATE
        )
    }
}
