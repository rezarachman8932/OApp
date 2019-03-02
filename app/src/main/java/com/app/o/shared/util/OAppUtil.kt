package com.app.o.shared.util

import android.content.Context
import android.graphics.drawable.LayerDrawable
import com.app.o.OApplication
import com.app.o.R
import com.app.o.api.location.LocationSpec
import com.app.o.custom.CountDrawable
import com.app.o.shared.helper.OAppPreferencesHelper
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

        const val ON_ENABLE_GPS_SETTING = 99
        const val REQUEST_CODE_FOR_LOCATION = 1

        const val MAX_IMAGES_SELECTION_COUNT = 3

        const val IMAGE_DETAIL_PREVIEW = "image_detail_preview"

        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        fun shouldReceiveNotification(): Boolean {
            return OAppPreferencesHelper.shouldReceivePushNotification
        }

        fun setReceiveNotification(receivedNotification: Boolean) {
            OAppPreferencesHelper.shouldReceivePushNotification = receivedNotification
        }

        fun getLongitude(): String? {
            return OAppPreferencesHelper.longitude
        }

        fun setLongitude(longitude: String) {
            OAppPreferencesHelper.longitude = longitude
        }

        fun getLatitude(): String? {
            return OAppPreferencesHelper.latitude
        }

        fun setLatitude(latitude: String) {
            OAppPreferencesHelper.latitude = latitude
        }

        fun getRangeFinder(): Int {
            return OAppPreferencesHelper.rangeFinder
        }

        fun setRangeFinder(range: Int) {
            OAppPreferencesHelper.rangeFinder = range
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
                diff < 50 * MINUTE_MILLIS -> (diff / MINUTE_MILLIS).toString() + " " + OApplication.applicationContext().getString(R.string.text_label_minutes_ago)
                diff < 90 * MINUTE_MILLIS -> OApplication.applicationContext().getString(R.string.text_label_one_hour_ago)
                diff < 24 * HOUR_MILLIS -> (diff / HOUR_MILLIS).toString() + " " + OApplication.applicationContext().getString(R.string.text_label_hours_ago)
                diff < 48 * HOUR_MILLIS -> OApplication.applicationContext().getString(R.string.text_label_yesterday)
                else -> (diff / DAY_MILLIS).toString() + " " + OApplication.applicationContext().getString(R.string.text_label_days_ago)
            }
        }

        fun generateStringToTimestamp(dateString: String): Long {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = formatter.parse(dateString)
            return date.time
        }

        fun setIconCount(context: Context, count: String, icon: LayerDrawable, layerId: Int) {
            val badge: CountDrawable
            val reuse = icon.findDrawableByLayerId(layerId)

            badge = if (reuse != null && reuse is CountDrawable) {
                reuse
            } else {
                CountDrawable(context)
            }

            badge.setCount(count)
            icon.mutate()
            icon.setDrawableByLayerId(layerId, badge)
        }

        fun generateLocationSpec(longitude: String, latitude: String): LocationSpec {
            return LocationSpec(latitude, longitude)
        }
    }

}