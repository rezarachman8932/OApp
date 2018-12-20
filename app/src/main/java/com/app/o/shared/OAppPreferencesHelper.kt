package com.app.o.shared

import android.content.Context
import android.preference.PreferenceManager

class OAppPreferencesHelper(context: Context) {

    companion object {
        private const val DEVICE_TOKEN = "data.source.prefs.DEVICE_TOKEN"
        private const val LOGGED_IN = "data.source.prefs.LOGGED_IN"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var deviceToken: String? = preferences.getString(DEVICE_TOKEN, "")
        set(value) = preferences.edit().putString(DEVICE_TOKEN, value).apply()

    var isLoggedIn: Boolean = preferences.getBoolean(LOGGED_IN, false)
        set(value) = preferences.edit().putBoolean(LOGGED_IN, value).apply()

}