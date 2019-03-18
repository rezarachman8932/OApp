package com.app.o.user.activation

import android.content.Intent
import android.os.Bundle
import com.app.o.R
import com.app.o.api.activation.ActivationTokenResponse
import com.app.o.api.activation.ActivationTokenSpec
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import com.app.o.user.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register_activation.*

class RegisterActivationActivity : OAppActivity(), OAppViewService<ActivationTokenResponse> {

    private lateinit var presenter: RegisterActivationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activation)

        initView()

        presenter = RegisterActivationPresenter(this, mCompositeDisposable)
    }

    override fun showLoading() {
        shouldShowProgress(true)
    }

    override fun hideLoading(statusCode: Int) {
        shouldShowProgress(false)

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            showSnackBar(layout_root_scroll_activation_token, getString(R.string.text_registration_token_validation_failed))
        }
    }

    override fun onDataResponse(data: ActivationTokenResponse) {
        if (isSuccess(data.status)) {
            removeUserState()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_registration_token_header)

        if (OAppUserUtil.getActivationType() == "email") {
            text_header_registration_token.text = getString(R.string.text_registration_token_via_email)
        } else {
            text_header_registration_token.text = getString(R.string.text_registration_token_via_sms)
        }

        button_verify_registration_token.setOnClickListener {
            val token = input_registration_token.text.toString()

            if (!token.isEmpty()) {
                presenter.activateAccount(ActivationTokenSpec(token))
            }
        }
    }

}