package com.app.o.shared

import android.content.Context
import android.util.Patterns
import android.util.TypedValue
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

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

        fun getUserName(): String? {
            return OAppPreferencesHelper.username
        }

        fun setUserName(username: String) {
            OAppPreferencesHelper.username = username
        }

        fun isLoggedIn(): Boolean {
            return OAppPreferencesHelper.isLoggedIn
        }

        fun setLoggedIn(loggedIn: Boolean) {
            OAppPreferencesHelper.isLoggedIn = loggedIn
        }

        fun generateJWTToken(username: String?, token: String?): String {
            val timestamp = System.currentTimeMillis()

            val params : HashMap<String, Any?> = HashMap()
            params["username"] = username
            params["iat"] = timestamp.toString()

            return Jwts.builder()
                    .setClaims(params)
                    .signWith(SignatureAlgorithm.HS256, token?.toByteArray())
                    .compact()
        }

        fun dpToPx(context: Context, dp: Int): Int {
            val r = context.resources
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.getDisplayMetrics()))
        }
    }

}