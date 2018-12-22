package com.app.o.shared

import android.content.Context
import android.util.Patterns
import android.util.TypedValue
import com.app.o.OApplication
import com.app.o.R
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.text.SimpleDateFormat
import java.util.*

class OAppUtil {

    companion object {
        const val MINIMUM_CHARS = 6
        const val MINIMUM_PHONE_CHARS = 11

        const val ON_FINISH_SUCCEED = 0
        const val ON_FINISH_FAILED  = 1

        const val SUCCESS_STATUS = 0

        const val SPLASH_DELAY: Long = 3000

        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

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

        fun getTimeAgo(time: Long): String? {
            var timestamp: Long = time
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            if (timestamp < 1000000000000L) {
                timestamp *= 1000
            }

            if (time > now || time <= 0) {
                return null
            }

            return when {
                diff < MINUTE_MILLIS -> OApplication.applicationContext().getString(R.string.text_label_recently)
                diff < 2 * MINUTE_MILLIS -> OApplication.applicationContext().getString(R.string.text_label_one_minute_ago)
                diff < 50 * MINUTE_MILLIS -> (diff / MINUTE_MILLIS).toString() + OApplication.applicationContext().getString(R.string.text_label_minutes_ago)
                diff < 90 * MINUTE_MILLIS -> OApplication.applicationContext().getString(R.string.text_label_one_hour_ago)
                diff < 24 * HOUR_MILLIS -> (diff / HOUR_MILLIS).toString() + OApplication.applicationContext().getString(R.string.text_label_hours_ago)
                diff < 48 * HOUR_MILLIS -> OApplication.applicationContext().getString(R.string.text_label_yesterday)
                else -> (diff / DAY_MILLIS).toString() + OApplication.applicationContext().getString(R.string.text_label_days_ago)
            }
        }

        fun generateStringToTimestamp(date: String): Long {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = formatter.parse(date)
            return date.time
        }
    }

}