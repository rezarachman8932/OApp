package com.app.o.shared.util

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

class OAppSocialMediaUtil {

    companion object {
        fun getInstagramProfileIntent(pm: PackageManager, username: String?): Intent {
            val intent = Intent(Intent.ACTION_VIEW)
            val packageIntent = "com.instagram.android"

            try {
                if (pm.getPackageInfo(packageIntent, 0) != null) {
                    intent.data = Uri.parse("http://instagram.com/_u/$username")
                    intent.setPackage(packageIntent)
                    return intent
                }
            } catch (ignored: PackageManager.NameNotFoundException) {}

            return intent
        }
    }

}