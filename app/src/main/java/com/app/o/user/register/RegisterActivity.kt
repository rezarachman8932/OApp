package com.app.o.user.register

import android.os.Bundle
import android.view.View
import com.app.o.R
import com.app.o.base.page.OAppActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : OAppActivity(), View.OnClickListener {

    private var shouldShowPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        setViewListener()
    }

    private fun setViewListener() {
        layout_image_profile.setOnClickListener(this)
        icon_password_preview.setOnClickListener(this)
        button_register.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.layout_image_profile -> {

            }

            R.id.icon_password_preview -> {
                shouldShowPassword = !shouldShowPassword
                setPasswordPreview(shouldShowPassword, input_sign_up_password)
            }

            R.id.button_sign_up -> {

            }
        }
    }

}