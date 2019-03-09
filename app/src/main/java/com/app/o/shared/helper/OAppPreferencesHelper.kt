package com.app.o.shared.helper

import android.content.Context
import android.content.SharedPreferences

object OAppPreferencesHelper {

    private const val NAME = "OAppSharePreference"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val IS_NOTIFICATION_EXIST = Pair("is_notification_exist", false)
    private val LOGGED_IN = Pair("is_logged_in", false)
    private val RECEIVED_NOTIFICATION = Pair("should_receive_notification", true)
    private val TOKEN = Pair("token", "")
    private val FCM_TOKEN = Pair("fcm_token", "")
    private val EMAIL = Pair("email", "")
    private val USER_NAME = Pair("username", "")
    private val USER_ID = Pair("user_id", 0)
    private val RANGE_FINDER = Pair("range_finder", 1)
    private val PHONE_NUMBER = Pair("phone_number", "")
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

    var isNotificationExist: Boolean
        get() = preferences.getBoolean(IS_NOTIFICATION_EXIST.first, IS_NOTIFICATION_EXIST.second)
        set(value) = preferences.edit {
            it.putBoolean(IS_NOTIFICATION_EXIST.first, value)
        }

    var shouldReceivePushNotification: Boolean
        get() = preferences.getBoolean(RECEIVED_NOTIFICATION.first, RECEIVED_NOTIFICATION.second)
        set(value) = preferences.edit {
            it.putBoolean(RECEIVED_NOTIFICATION.first, value)
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

    var email: String?
        get() = preferences.getString(EMAIL.first, EMAIL.second)
        set(value) = preferences.edit {
            it.putString(EMAIL.first, value)
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

    var phoneNumber: String?
        get() = preferences.getString(PHONE_NUMBER.first, PHONE_NUMBER.second)
        set(value) = preferences.edit {
            it.putString(PHONE_NUMBER.first, value)
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

    var rangeFinder: Int
        get() = preferences.getInt(RANGE_FINDER.first, RANGE_FINDER.second)
        set(value) = preferences.edit {
            it.putInt(RANGE_FINDER.first, value)
        }

    var fcmToken: String?
        get() = preferences.getString(FCM_TOKEN.first, FCM_TOKEN.second)
        set(value) = preferences.edit {
            it.putString(FCM_TOKEN.first, value)
        }

}