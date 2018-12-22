package com.app.o

import android.app.Application
import android.content.Context
import com.app.o.shared.OAppPreferencesHelper

class OApplication : Application() {

    companion object {
        lateinit var instance: OApplication
            private set

        fun applicationContext() : Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        OAppPreferencesHelper.init(instance)
    }

}