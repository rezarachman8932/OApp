package com.app.o.user.update_profile

import com.app.o.api.user.update.avatar.UpdateAvatarResponse

interface UpdateProfileAvatarCallback {
    fun onSucceedGettingImage(updateAvatarResponse: UpdateAvatarResponse)
    fun onFailedGettingImage()
    fun onLoadingImage()
}