package com.app.o.user.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.app.o.R
import com.app.o.base.page.OAppActivity
import com.app.o.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : OAppActivity(), View.OnClickListener {

    private var shouldShowPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        icon_password_preview.setOnClickListener(this)
        button_login.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.icon_password_preview -> {
                if (!shouldShowPassword) {
                    shouldShowPassword = true
                    input_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    shouldShowPassword = false
                    input_password.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }

            R.id.button_login -> {
                val username = input_username.text.toString()
                val password = input_password.text.toString()

                //TODO Validate input

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

}