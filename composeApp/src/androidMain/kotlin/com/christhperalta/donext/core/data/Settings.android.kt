package com.christhperalta.donext.core.data

import android.content.Context

actual class Settings {
    private val prefs by lazy {
        AndroidAppContext.context.getSharedPreferences("donext_prefs", Context.MODE_PRIVATE)
    }

    actual fun getString(key: String, default: String): String =
        prefs.getString(key, default) ?: default

    actual fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean =
        prefs.getBoolean(key, default)

    actual fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
}
