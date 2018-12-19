package com.app.o

import android.app.Application
import com.app.o.shared.OAppPreferencesHelper

class OApplication : Application() {

    companion object {
        lateinit var prefHelper: OAppPreferencesHelper
            private set
    }

    override fun onCreate() {
        super.onCreate()

        prefHelper = OAppPreferencesHelper(this)
    }

}