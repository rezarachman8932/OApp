package com.app.o.shared

import android.content.Context
import android.content.SharedPreferences

object OAppPreferencesHelper {

    private const val NAME = "OAppSharePreference"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val LOGGED_IN = Pair("is_logged_in", false)
    private val TOKEN = Pair("token", "")
    private val USER_NAME = Pair("username", "")
    private val USER_ID = Pair("user_id", 0)
    private val LAST_LOCATION_LONGITUDE = Pair("longitude", "")
    private val LAST_LOCATION_LATITUDE = Pair("latitude", "")

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

    var userId: Int
        get() = preferences.getInt(USER_ID.first, USER_ID.second)
        set(value) = preferences.edit {
            it.putInt(USER_ID.first, value)
        }

    var username: String?
        get() = preferences.getString(USER_NAME.first, USER_NAME.second)
        set(value) = preferences.edit {
            it.putString(USER_NAME.first, value)
        }

    var longitude: String?
        get() = preferences.getString(LAST_LOCATION_LONGITUDE.first, LAST_LOCATION_LONGITUDE.second)
        set(value) = preferences.edit {
            it.putString(LAST_LOCATION_LONGITUDE.first, value)
        }

    var latitude: String?
        get() = preferences.getString(LAST_LOCATION_LATITUDE.first, LAST_LOCATION_LATITUDE.second)
        set(value) = preferences.edit {
            it.putString(LAST_LOCATION_LATITUDE.first, value)
        }

}