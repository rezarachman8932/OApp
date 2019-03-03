package com.app.o.user.update_password

import android.os.Bundle
import com.app.o.R
import com.app.o.api.user.update.password.UpdatePasswordResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUtil
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
        shouldShowProgress(true)
    }

    override fun hideLoading(statusCode: Int) {
        shouldShowProgress(false)

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            showSnackBar(scroll_root_update_password, getString(R.string.text_unexpected_error_change_password))
        }
    }

    override fun onDataResponse(data: UpdatePasswordResponse) {
        if (isSuccess(data.status)) {
            resetAllFields()

            showSnackBar(scroll_root_update_password, getString(R.string.text_update_password_succeed))
        } else {
            showSnackBar(scroll_root_update_password, getString(R.string.text_error_current_password_invalid))
        }
    }

    override fun onErrorAllFieldsRequired() {
        showSnackBar(scroll_root_update_password, getString(R.string.text_error_all_fields_required))
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

    private fun resetAllFields() {
        input_current_password.setText("")
        input_new_password.setText("")
        input_retype_new_password.setText("")
    }

}