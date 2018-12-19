package com.app.o.shared

import android.util.Patterns
import com.app.o.OApplication

class OAppUtil {

    companion object {
        const val MINIMUM_CHARS = 6
        const val MINIMUM_PHONE_CHARS = 11

        const val ON_FINISH_SUCCEED = 0
        const val ON_FINISH_FAILED  = 1

        const val SUCCESS_STATUS = 0

        fun isValidEmail(email: String): Boolean =
                email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun getToken(): String? {
            return OApplication.prefHelper.deviceToken
        }

        fun setToken(token: String) {
            OApplication.prefHelper.deviceToken = token
        }
    }

}