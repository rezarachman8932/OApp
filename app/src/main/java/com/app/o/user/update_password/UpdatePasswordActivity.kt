package com.app.o.user.update_password

import android.os.Bundle
import com.app.o.R
import com.app.o.api.user.update.password.UpdatePasswordResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import kotlinx.android.synthetic.main.activity_update_password.*

class UpdatePasswordActivity : OAppActivity(), OAppViewService<UpdatePasswordResponse>, UpdatePasswordCallback {

    private lateinit var presenter: UpdatePasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        initView()

        presenter = UpdatePasswordPresenter(this, this, mCompositeDisposable)
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading(statusCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataResponse(data: UpdatePasswordResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onErrorAllFieldsRequired() {
        showSnackBar(scroll_root_update_password, getString(R.string.text_error_all_fields_required))
    }

    override fun onErrorCurrentPassword() {
        showSnackBar(scroll_root_update_password, getString(R.string.text_error_current_password_invalid))
    }

    override fun onErrorCurrentPasswordEqualsNewPassword() {
        showSnackBar(scroll_root_update_password, getString(R.string.text_error_new_password_same_with_existing))
    }

    override fun onErrorNotMatchedRetype() {
        showSnackBar(scroll_root_update_password, getString(R.string.text_error_retype_new_password))
    }

    override fun onErrorPasswordLessThanMinChars() {
        showSnackBar(scroll_root_update_password, getString(R.string.text_error_submit_password))
    }

    private fun initView() {
        supportActionBar?.title = getString(R.string.text_header_update_password)

        button_update_password.setOnClickListener {
            val currentPassword = input_current_password.text.toString()
            val newPassword = input_new_password.text.toString()
            val reTypeNewPassword = input_retype_new_password.text.toString()

            presenter.validatePasswordChanged(currentPassword, newPassword, reTypeNewPassword)
        }
    }

}