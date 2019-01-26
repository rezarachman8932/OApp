package com.app.o.base.presenter

import com.app.o.shared.OAppUtil

abstract class OAppPresenter {

    protected fun getHeaderAuth(): String {
        return OAppUtil.generateJWTToken(OAppUtil.getUserName(), OAppUtil.getToken())
    }

    fun saveLastLocation(longitude: String, latitude: String) {
        OAppUtil.setLongitude(longitude)
        OAppUtil.setLatitude(latitude)
    }

}