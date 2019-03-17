package com.app.o.user.activation

import android.os.Bundle
import com.app.o.R
import com.app.o.base.page.OAppActivity
import com.app.o.shared.util.OAppUserUtil
import kotlinx.android.synthetic.main.activity_register_activation.*

class RegisterActivationActivity : OAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activation)

        initView()
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_registration_token_header)

        if (OAppUserUtil.getActivationType() == "email") {
            text_header_registration_token.text = getString(R.string.text_registration_token_via_email)
        } else {
            text_header_registration_token.text = getString(R.string.text_registration_token_via_sms)
        }
    }

}