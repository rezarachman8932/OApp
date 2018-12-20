package com.app.o

import android.app.Application
import com.app.o.shared.OAppPreferencesHelper

class OApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        OAppPreferencesHelper.init(this)
    }

}