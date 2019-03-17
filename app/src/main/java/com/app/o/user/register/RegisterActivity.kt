package com.app.o.user.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.app.o.R
import com.app.o.api.register.RegisterResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
import com.app.o.user.activation.RegisterActivationActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : OAppActivity(),
        View.OnClickListener,
        OAppViewService<RegisterResponse>,
        RegisterCallback, TextWatcher {

    private var shouldShowPassword = false
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        setViewListener()

        presenter = RegisterPresenter(this, this, mCompositeDisposable)
    }

    override fun showLoading() {
        shouldShowProgress(true)
    }

    override fun hideLoading(statusCode: Int) {
        shouldShowProgress(false)

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            showSnackBar(scroll_root_register, getString(R.string.text_error_register_fetched))
        }
    }

    override fun onDataResponse(data: RegisterResponse) {
        if (isSuccess(data.status)) {
            saveActivationUserTokenState(data.action_type)

            showSnackBar(scroll_root_register, getString(R.string.text_success_register))

            val intent = Intent(this, RegisterActivationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            showSnackBar(scroll_root_register, getString(R.string.text_error_register_already_existed))
        }
    }

    override fun onAllInputEmpty() {
        showSnackBar(scroll_root_register, getString(R.string.text_error_submit_register))
    }

    override fun onNameNotComplete() {
        showSnackBar(scroll_root_register, getString(R.string.text_error_submit_name))
    }

    override fun onPhoneNumberNotComplete() {
        showSnackBar(scroll_root_register, getString(R.string.text_error_submit_phone))
    }

    override fun onEmailNotValid() {
        showSnackBar(scroll_root_register, getString(R.string.text_error_submit_email))
    }

    override fun onUsernameNotComplete() {
        showSnackBar(scroll_root_register, getString(R.string.text_error_submit_username))
    }

    override fun onPasswordNotComplete() {
        showSnackBar(scroll_root_register, getString(R.string.text_error_submit_password))
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.icon_password_preview -> {
                shouldShowPassword = !shouldShowPassword
                setPasswordPreview(shouldShowPassword, input_sign_up_password)
            }

            R.id.button_register -> {
                register()
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(input: CharSequence, p1: Int, p2: Int, p3: Int) {
        if (!input.isEmpty() && input.length >= OAppUtil.MINIMUM_CHARS) {
            icon_username_checklist.visibility = View.VISIBLE
        } else {
            icon_username_checklist.visibility = View.GONE
        }
    }

    private fun register() {
        val name = input_name.text.toString()
        val phone = input_phone.text.toString()
        val email = input_email.text.toString()
        val username = input_username.text.toString()
        val password = input_sign_up_password.text.toString()

        presenter.validateSignUp(name, phone, email, username, password, getActivationType())
    }

    private fun setViewListener() {
        icon_password_preview.setOnClickListener(this)
        button_register.setOnClickListener(this)
        input_username.addTextChangedListener(this)

        option_via_email.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                option_via_sms.isChecked = false
            }
        }

        option_via_sms.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                option_via_email.isChecked = false
            }
        }
    }

    private fun getActivationType(): String {
        lateinit var activationType: String

        if (option_via_email.isChecked) {
            activationType = "email"
        } else if (option_via_sms.isChecked) {
            activationType = "sms"
        }

        return activationType
    }

}