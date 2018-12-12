package com.app.o

import android.app.Application

class OApplication : Application() {

    companion object {
        lateinit var instance: OApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

}