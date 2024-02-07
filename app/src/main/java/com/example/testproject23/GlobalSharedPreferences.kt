package com.example.testproject23

import android.content.Context
import android.content.Context.MODE_PRIVATE

class GlobalSharedPreferences(context: Context) {
    private val settings =
        context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE)
    private val editor = settings.edit()
    var token
        get() = settings.getString("user_token", null)
        set(value) {
            editor.putString("user_token", value)
            editor.apply()
        }
}


