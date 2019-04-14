package com.app.o.api.home

import com.app.o.api.notification.PushNotificationResponse
import com.app.o.api.relation.UserConnectedCountResponse
import com.app.o.api.user.profile.UserProfileResponse

data class HomeResponseZip (
        val homeResponse: HomeResponse,
        val userConnectedCount: UserConnectedCountResponse,
        val userProfileResponse: UserProfileResponse,
        val pushNotificationListResponse : PushNotificationResponse)