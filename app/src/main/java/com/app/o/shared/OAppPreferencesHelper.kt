package com.app.o.shared

import android.content.Context
import android.preference.PreferenceManager

class OAppPreferencesHelper(context: Context) {

    companion object {
        private const val DEVICE_TOKEN = "data.source.prefs.DEVICE_TOKEN"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var deviceToken: String? = preferences.getString(DEVICE_TOKEN, "")
        set(value) = preferences.edit().putString(DEVICE_TOKEN, value).apply()

}