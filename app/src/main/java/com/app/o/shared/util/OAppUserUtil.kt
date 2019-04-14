package com.app.o.shared.util

import android.util.Patterns
import com.app.o.shared.helper.OAppPreferencesHelper

class OAppUserUtil {

    companion object {
        const val USER_STATE_LOGGED_IN = 98
        const val USER_STATE_NOT_LOGGED_IN = 99
        const val USER_STATE_REGISTRATION_NOT_COMPLETED = 100

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

        fun setPhoneNumber(phone: String?) {
            OAppPreferencesHelper.phoneNumber = phone
        }

        fun getUserState(): Int {
            return OAppPreferencesHelper.userState
        }

        fun setUserState(state: Int) {
            OAppPreferencesHelper.userState = state
        }

        fun getActivationType(): String? {
            return OAppPreferencesHelper.registerActivationType
        }

        fun setActivationType(state: String) {
            OAppPreferencesHelper.registerActivationType = state
        }

        fun getThirdPartyLoginType(): String? {
            return OAppPreferencesHelper.thirdPartyLoginType
        }

        fun setThirdPartyLoginType(type: String?) {
            OAppPreferencesHelper.thirdPartyLoginType = type
        }

        fun getFacebookUserName(): String? {
            return OAppPreferencesHelper.facebookUserName
        }

        fun setFacebookUserName(username: String?) {
            OAppPreferencesHelper.facebookUserName = username
        }

        fun getFacebookUserId(): String? {
            return OAppPreferencesHelper.facebookUserId
        }

        fun setFacebookUserId(userId: String?) {
            OAppPreferencesHelper.facebookUserId = userId
        }
    }

}