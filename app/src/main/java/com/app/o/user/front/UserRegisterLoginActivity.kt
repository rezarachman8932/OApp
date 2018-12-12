package com.app.o.user.front

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.o.R
import com.app.o.base.page.OAppActivity
import com.app.o.user.login.LoginActivity
import kotlinx.android.synthetic.main.activity_user_register_login.*

class UserRegisterLoginActivity : OAppActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register_login)
        supportActionBar?.hide()

        button_sign_up.setOnClickListener(this)
        layout_login_text.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.button_sign_up -> {}
            R.id.layout_login_text -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

}