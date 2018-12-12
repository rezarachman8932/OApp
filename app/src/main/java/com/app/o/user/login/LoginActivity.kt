package com.app.o.user.login

import android.os.Bundle
import com.app.o.R
import com.app.o.base.page.OAppActivity

class LoginActivity : OAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
    }

}