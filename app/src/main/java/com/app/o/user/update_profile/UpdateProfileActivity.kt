package com.app.o.user.update_profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.app.o.R
import com.app.o.api.user.profile.UserProfileResponse
import com.app.o.api.user.update.avatar.UpdateAvatarResponse
import com.app.o.api.user.update.profile.UserUpdateProfileResponse
import com.app.o.base.page.OAppActivity
import com.app.o.base.service.OAppViewService
import com.app.o.shared.util.OAppMultimediaUtil
import com.app.o.shared.util.OAppUserUtil
import com.app.o.shared.util.OAppUtil
import kotlinx.android.synthetic.main.activity_update_profile.*

class UpdateProfileActivity : OAppActivity(),
        View.OnClickListener,
        OAppViewService<UserUpdateProfileResponse>,
        UpdateProfileCallback, UpdateProfileAvatarCallback {

    private lateinit var presenter: UpdateProfilePresenter
    private lateinit var uriImageProfile: String
    private lateinit var bitmapImageProfile: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        initView()

        presenter = UpdateProfilePresenter(this, this, this, mCompositeDisposable)
        presenter.getCurrentProfile()
    }

    override fun onClick(view: View) {
        when {
            view.id == R.id.button_update_profile -> updateData()
            view.id == R.id.button_select_photo -> openMedia()
            view.id == R.id.button_complete_selected_photo -> doUpdateAvatar()
        }
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

        OAppMultimediaUtil.setImage(userProfileResponse.avatar, R.drawable.ic_logo, image_user_profile)
    }

    override fun onSucceedGettingImage(updateAvatarResponse: UpdateAvatarResponse) {
        shouldShowProgress(false)

        if (isSuccess(updateAvatarResponse.status)) {
            showSnackBar(scroll_root_update_profile, getString(R.string.text_update_avatar_succeed))
        } else {
            showSnackBar(scroll_root_update_profile, getString(R.string.text_update_avatar_failed))
        }
    }

    override fun onFailedGettingImage() {
        shouldShowProgress(false)
        showSnackBar(scroll_root_update_profile, getString(R.string.text_update_avatar_failed))
    }

    override fun onLoadingImage() {
        shouldShowProgress(true)
    }

    override fun onSuccessGetImage(values: ArrayList<String>) {
        super.onSuccessGetImage(values)

        try {
            if (values.size > 0) {
                uriImageProfile = values[0]
                bitmapImageProfile = BitmapFactory.decodeFile(uriImageProfile)

                image_user_profile.setImageBitmap(bitmapImageProfile)

                button_complete_selected_photo.visibility = View.VISIBLE
            }
        } catch (exception: Exception) {}
    }

    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.text_label_update_profile_header)

        input_updated_name.isEnabled = false
        input_updated_email.isEnabled = false

        button_update_profile.setOnClickListener(this)
        button_complete_selected_photo.setOnClickListener(this)
        button_select_photo.setOnClickListener(this)
    }

    private fun updateData() {
        val phone = input_updated_phone.text.toString()
        val location = input_location.text.toString()
        val facebook = input_facebook.text.toString()
        val twitter = input_twitter.text.toString()
        val instagram = input_instagram.text.toString()

        presenter.validateProfileUpdated(phone, location, facebook, twitter, instagram)
    }

    private fun doUpdateAvatar() {
        val body = OAppMultimediaUtil.prepareFileImagePart("avatar", bitmapImageProfile, uriImageProfile)
        presenter.updateAvatar(body)
    }

}