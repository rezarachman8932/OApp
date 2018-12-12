package com.app.o.user.front

import android.os.Bundle
import com.app.o.R
import com.app.o.base.page.OAppActivity

class UserRegisterLoginActivity : OAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register_login)
        supportActionBar?.hide()
    }

}