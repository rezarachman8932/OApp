package com.app.o.user.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.o.R
import com.app.o.api.login.LoginResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : OAppActivity(),
        View.OnClickListener,
        OAppViewService<LoginResponse>,
        LoginCallback {

    private var shouldShowPassword = false
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        icon_password_preview.setOnClickListener(this)
        button_login.setOnClickListener(this)

        presenter = LoginPresenter(this, this, mCompositeDisposable)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun onDataResponse(data: LoginResponse) {
        if (isSuccess(data.message, data.status)) {
            setToken(data.token)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            showSnackBar(root_login, getString(R.string.text_error_after_login))
        }
    }

    override fun onEmptyInput() {
        showSnackBar(root_login, getString(R.string.text_error_submit_login))
    }

    override fun onPasswordNotComplete() {
        showSnackBar(root_login, getString(R.string.text_error_submit_login_password))
    }

    override fun onUsernameNotComplete() {
        showSnackBar(root_login, getString(R.string.text_error_submit_login_username))
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.icon_password_preview -> {
                shouldShowPassword = !shouldShowPassword
                setPasswordPreview(shouldShowPassword, input_password)
            }

            R.id.button_login -> {
                val username = input_username.text.toString()
                val password = input_password.text.toString()
                presenter.validateLogin(username, password)
            }
        }
    }

}