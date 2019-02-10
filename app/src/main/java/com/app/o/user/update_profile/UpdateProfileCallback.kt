package com.app.o.user.update_profile

import com.app.o.api.user.profile.UserProfileResponse

interface UpdateProfileCallback {
    fun onLoadCurrentProfile()
    fun onFailedGetCurrentProfile()
    fun onSucceedGetCurrentProfile(userProfileResponse: UserProfileResponse)
    fun onPhoneNumberNotComplete()
    fun onLocationNotComplete()
}