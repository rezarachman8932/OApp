package com.app.o.base.presenter

import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil

abstract class OAppPresenter {

    protected fun getHeaderAuth(): String {
        return OAppUtil.generateJWTToken(OAppUserUtil.getUserName(), getToken())
    }

    private fun getToken(): String? {
        return OAppUserUtil.getToken()
    }

    fun getFCMToken(): String? {
        return OAppUserUtil.getFCMToken()
    }

    fun saveLastLocation(longitude: String, latitude: String) {
        OAppUtil.setLongitude(longitude)
        OAppUtil.setLatitude(latitude)
    }

}