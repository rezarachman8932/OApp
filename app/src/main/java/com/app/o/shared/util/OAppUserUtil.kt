package com.app.o.shared.util

import android.util.Patterns
import com.app.o.shared.helper.OAppPreferencesHelper

class OAppUserUtil {

    companion object {
        fun isValidEmail(email: String): Boolean = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun getFCMToken(): String? {
            return OAppPreferencesHelper.fcmToken
        }

        fun setFCMToken(fcmToken: String?) {
            OAppPreferencesHelper.fcmToken = fcmToken
        }

        fun getToken(): String? {
            return OAppPreferencesHelper.tokenAuth
        }

        fun setToken(token: String) {
            OAppPreferencesHelper.tokenAuth = token
        }

        fun getEmail(): String? {
            return OAppPreferencesHelper.email
        }

        fun setEmail(email: String) {
            OAppPreferencesHelper.email = email
        }

        fun getUserName(): String? {
            return OAppPreferencesHelper.username
        }

        fun setUserName(username: String) {
            OAppPreferencesHelper.username = username
        }

        fun getUserId(): Int {
            return OAppPreferencesHelper.userId
        }

        fun setUserId(userId: Int) {
            OAppPreferencesHelper.userId = userId
        }

        fun getPhoneNumber(): String? {
            return OAppPreferencesHelper.phoneNumber
        }

        fun setPhoneNumber(phone: String) {
            OAppPreferencesHelper.phoneNumber = phone
        }

        fun isLoggedIn(): Boolean {
            return OAppPreferencesHelper.isLoggedIn
        }

        fun setLoggedIn(loggedIn: Boolean) {
            OAppPreferencesHelper.isLoggedIn = loggedIn
        }
    }

}