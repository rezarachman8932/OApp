package com.app.o.shared

import android.content.Context
import android.content.SharedPreferences

object OAppPreferencesHelper {

    private const val NAME = "OAppSharePreference"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val LOGGED_IN = Pair("is_logged_in", false)
    private val TOKEN = Pair("token", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isLoggedIn: Boolean
        get() = preferences.getBoolean(LOGGED_IN.first, LOGGED_IN.second)
        set(value) = preferences.edit {
            it.putBoolean(LOGGED_IN.first, value)
        }

    var tokenAuth: String?
        get() = preferences.getString(TOKEN.first, TOKEN.second)
        set(value) = preferences.edit {
            it.putString(TOKEN.first, value)
        }

}