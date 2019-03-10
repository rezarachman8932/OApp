package com.app.o.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.app.o.R
import com.app.o.home.HomeActivity
import com.app.o.notification.page.NotificationListActivity
import com.app.o.shared.util.OAppNotificationUtil
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import com.app.o.user.front.UserRegisterLoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var mDelayHandler: Handler
    private lateinit var mIntent: Intent

    private var isFromNotification = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        getParameter()

        mDelayHandler = Handler()
        mDelayHandler.postDelayed(mRunnable, OAppUtil.SPLASH_DELAY)
    }

    public override fun onDestroy() {
        mDelayHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    private fun getParameter() {
        isFromNotification = intent.getBooleanExtra(OAppNotificationUtil.PUSH_NOTIFICATION, false)
    }

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            mIntent = if (OAppUserUtil.isLoggedIn()) {
                if (isFromNotification) {
                    OAppNotificationUtil.setPushNotificationExist(isFromNotification)

                    Intent(this, NotificationListActivity::class.java)
                } else {
                    Intent(this, HomeActivity::class.java)
                }
            } else {
                Intent(this, UserRegisterLoginActivity::class.java)
            }
            startActivity(mIntent)
            finish()
        }
    }

}