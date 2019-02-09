package com.app.o.user.update_profile

import android.os.Bundle
import android.view.View
import com.app.o.R
import com.app.o.api.user.UserProfileResponse
import com.app.o.api.user.UserUpdateProfileResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import kotlinx.android.synthetic.main.activity_update_profile.*

class UpdateProfileActivity : OAppActivity(),
        View.OnClickListener,
        OAppViewService<UserUpdateProfileResponse>,
        UpdateProfileCallback {

    private lateinit var presenter: UpdateProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        initView()

        presenter = UpdateProfilePresenter(this, this, mCompositeDisposable)
        presenter.getCurrentProfile()
    }

    override fun onClick(p0: View?) {
        updateData()
    }

    override fun showLoading() {
        shouldShowProgress(true)
    }

    override fun hideLoading(statusCode: Int) {
        shouldShowProgress(false)

        if (statusCode == OAppUtil.ON_FINISH_FAILED) {
            showSnackBar(scroll_root_update_profile, getString(R.string.text_update_failed))
        }
    }

    override fun onDataResponse(data: UserUpdateProfileResponse) {
        if (isSuccess(data.status)) {
            showSnackBar(scroll_root_update_profile, getString(R.string.text_update_succeed))
            finish()
        }
    }

    override fun onPhoneNumberNotComplete() {
        showSnackBar(scroll_root_update_profile, getString(R.string.text_update_profile_not_valid_phone))
    }

    override fun onLocationNotComplete() {
        showSnackBar(scroll_root_update_profile, getString(R.string.text_update_profile_not_valid_location))
    }

    override fun onLoadCurrentProfile() {}

    override fun onFailedGetCurrentProfile() {}

    override fun onSucceedGetCurrentProfile(userProfileResponse: UserProfileResponse) {
        input_updated_name.setText(userProfileResponse.name)
        input_updated_phone.setText(OAppUserUtil.getPhoneNumber())
        input_updated_email.setText(OAppUserUtil.getEmail())
        input_location.setText(userProfileResponse.location)
        input_facebook.setText(userProfileResponse.facebook)
        input_twitter.setText(userProfileResponse.twitter)
        input_instagram.setText(userProfileResponse.instagram)
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.text_label_update_profile_header)

        input_updated_name.isEnabled = false
        input_updated_email.isEnabled = false

        button_update_profile.setOnClickListener(this)
    }

    private fun updateData() {
        val phone = input_updated_phone.text.toString()
        val location = input_location.text.toString()
        val facebook = input_facebook.text.toString()
        val twitter = input_twitter.text.toString()
        val instagram = input_instagram.text.toString()

        presenter.validateProfileUpdated(phone, location, facebook, twitter, instagram)
    }

}