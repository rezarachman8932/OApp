package com.app.o.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.app.o.R
import com.app.o.home.HomeActivity
import com.app.o.shared.OAppUtil
import com.app.o.user.front.UserRegisterLoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var mDelayHandler: Handler
    private lateinit var mIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        mDelayHandler = Handler()
        mDelayHandler.postDelayed(mRunnable, OAppUtil.SPLASH_DELAY)
    }

    public override fun onDestroy() {
        mDelayHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            mIntent = if (OAppUtil.isLoggedIn()) {
                Intent(this, HomeActivity::class.java)
            } else {
                Intent(this, UserRegisterLoginActivity::class.java)
            }
            startActivity(mIntent)
            finish()
        }
    }

}