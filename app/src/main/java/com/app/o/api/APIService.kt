package com.app.o.api

import com.app.o.api.activation.ActivationTokenResponse
import com.app.o.api.activation.ActivationTokenSpec
import com.app.o.api.comment.CommentResponse
import com.app.o.api.comment.CommentSpec
import com.app.o.api.comment.SubmitCommentResponse
import com.app.o.api.detail.DetailResponse
import com.app.o.api.detail.DetailSpec
import com.app.o.api.home.HomeResponse
import com.app.o.api.location.LocationSpec
import com.app.o.api.location.LocationWithQuerySpec
import com.app.o.api.login.account.LoginResponse
import com.app.o.api.login.account.LoginSpec
import com.app.o.api.login.third_party.LoginSocialMediaSpec
import com.app.o.api.logout.LogoutResponse
import com.app.o.api.notification.PushNotificationReadResponse
import com.app.o.api.notification.PushNotificationReadSpec
import com.app.o.api.notification.PushNotificationResponse
import com.app.o.api.post.CreatedPostResponse
import com.app.o.api.post.LikedPostResponse
import com.app.o.api.post.LikedPostSpec
import com.app.o.api.register.RegisterResponse
import com.app.o.api.register.RegisterSpec
import com.app.o.api.relation.UserConnectedCountResponse
import com.app.o.api.relation.UserConnectedResponse
import com.app.o.api.user.blocked.BlockedUserResponse
import com.app.o.api.user.blocked.UserBlockingResponse
import com.app.o.api.user.blocked.UserBlockingSpec
import com.app.o.api.user.profile.UserProfileResponse
import com.app.o.api.user.profile.UserProfileSpec
import com.app.o.api.user.update.avatar.UpdateAvatarResponse
import com.app.o.api.user.update.password.UpdatePasswordResponse
import com.app.o.api.user.update.password.UpdatePasswordSpec
import com.app.o.api.user.update.profile.UserUpdateProfileResponse
import com.app.o.api.user.update.profile.UserUpdateProfileSpec
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIService {

    @POST("login")
    fun login(@Body spec: LoginSpec): Single<LoginResponse>

    @POST("logout")
    fun logout(@Header("Authorization") tokenAuth: String?): Single<LogoutResponse>

    @POST("register")
    fun register(@Body spec: RegisterSpec): Single<RegisterResponse>

    @POST("posts")
    fun post(@Body spec: LocationSpec, @Header("Authorization") tokenAuth: String?): Single<HomeResponse>

    @POST("post_search")
    fun postSearch(@Body spec: LocationWithQuerySpec, @Header("Authorization") tokenAuth: String?): Single<HomeResponse>

    @POST("people_connected")
    fun getPeopleConnected(@Body spec: LocationSpec, @Header("Authorization") tokenAuth: String?): Single<UserConnectedResponse>

    @POST("people_connected_amount")
    fun getPeopleConnectedCount(@Body spec: LocationSpec, @Header("Authorization") tokenAuth: String?): Single<UserConnectedCountResponse>

    @Multipart
    @POST("create_post")
    fun createPost(
            @Part multimedia: List<MultipartBody.Part>?,
            @Part("title") title: RequestBody,
            @Part("subtitle") subtitle: RequestBody,
            @Part("type") type: RequestBody,
            @Part("latitude") latitude: RequestBody,
            @Part("longitude") longitude: RequestBody,
            @Part("content") content: RequestBody,
            @Header("Authorization") tokenAuth: String?): Single<CreatedPostResponse>

    @POST("post_detail")
    fun getDetailContent(@Body spec: DetailSpec, @Header("Authorization") tokenAuth: String?): Single<DetailResponse>

    @POST("post_comment")
    fun getDetailCommentList(@Body spec: DetailSpec, @Header("Authorization") tokenAuth: String?): Single<CommentResponse>

    @POST("post_comment_create")
    fun submitNewComment(@Body spec: CommentSpec, @Header("Authorization") tokenAuth: String?): Single<SubmitCommentResponse>

    @POST("post_comment_reply")
    fun submitReplyComment(@Body spec: CommentSpec, @Header("Authorization") tokenAuth: String?): Single<SubmitCommentResponse>

    @POST("profile")
    fun getUserProfile(@Body spec: UserProfileSpec, @Header("Authorization") tokenAuth: String?): Single<UserProfileResponse>

    @POST("profile")
    fun getOwnProfile(@Header("Authorization") tokenAuth: String?): Single<UserProfileResponse>

    @POST("user_posts")
    fun getUserPostedItems(@Body spec: UserProfileSpec, @Header("Authorization") tokenAuth: String?): Single<HomeResponse>

    @POST("user_posts")
    fun getOwnPostedItems(@Header("Authorization") tokenAuth: String?): Single<HomeResponse>

    @POST("update_profile")
    fun updateProfile(@Body spec: UserUpdateProfileSpec, @Header("Authorization") tokenAuth: String?): Single<UserUpdateProfileResponse>

    @Multipart
    @POST("update_avatar")
    fun updateAvatar(@Part avatar: MultipartBody.Part, @Header("Authorization") tokenAuth: String?): Single<UpdateAvatarResponse>

    @POST("change_password")
    fun updatePassword(@Body spec: UpdatePasswordSpec, @Header("Authorization") tokenAuth: String?): Single<UpdatePasswordResponse>

    @POST("list_block_user")
    fun getBlockedUsers(@Header("Authorization") tokenAuth: String?): Single<BlockedUserResponse>

    @POST("unblock_user")
    fun unBlockedUser(@Body spec: UserBlockingSpec, @Header("Authorization") tokenAuth: String?): Single<UserBlockingResponse>

    @POST("block_user")
    fun blockedUser(@Body spec: UserBlockingSpec, @Header("Authorization") tokenAuth: String?): Single<UserBlockingResponse>

    @POST("post_like")
    fun likeUserPost(@Body spec: LikedPostSpec, @Header("Authorization") tokenAuth: String?): Single<LikedPostResponse>

    @POST("post_unlike")
    fun unLikeUserPost(@Body spec: LikedPostSpec, @Header("Authorization") tokenAuth: String?): Single<LikedPostResponse>

    @POST("notification_user")
    fun getPushNotificationList(@Header("Authorization") tokenAuth: String?): Single<PushNotificationResponse>

    @POST("notification_update_read")
    fun setPushNotificationAsRead(@Body spec: PushNotificationReadSpec, @Header("Authorization") tokenAuth: String?): Single<PushNotificationReadResponse>

    @POST("user_activation")
    fun activateUserToken(@Body spec: ActivationTokenSpec): Single<ActivationTokenResponse>

    @POST("oauth")
    fun loginWithThirdParty(@Body spec: LoginSocialMediaSpec): Single<LoginResponse>

}