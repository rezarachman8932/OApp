package com.app.o.shared

import android.util.Patterns

class OAppUtil {

    companion object {
        const val MINIMUM_CHARS = 6
        const val MINIMUM_PHONE_CHARS = 11

        const val ON_FINISH_SUCCEED = 0
        const val ON_FINISH_FAILED  = 1

        const val SUCCESS_STATUS = 0

        const val SPLASH_DELAY: Long = 3000

        fun isValidEmail(email: String): Boolean =
                email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun getToken(): String? {
            return OAppPreferencesHelper.tokenAuth
        }

        fun setToken(token: String) {
            OAppPreferencesHelper.tokenAuth = token
        }

        fun isLoggedIn(): Boolean {
            return OAppPreferencesHelper.isLoggedIn
        }

        fun setLoggedIn(loggedIn: Boolean) {
            OAppPreferencesHelper.isLoggedIn = loggedIn
        }
    }

}