package com.app.o.api

import com.app.o.api.comment.CommentResponse
import com.app.o.api.detail.DetailResponse
import com.app.o.api.detail.DetailSpec
import com.app.o.api.home.HomeResponse
import com.app.o.api.location.LocationSpec
import com.app.o.api.location.LocationWithQuerySpec
import com.app.o.api.login.LoginResponse
import com.app.o.api.login.LoginSpec
import com.app.o.api.post.CreatedPostResponse
import com.app.o.api.register.RegisterResponse
import com.app.o.api.register.RegisterSpec
import com.app.o.api.relation.UserConnectedCountResponse
import com.app.o.api.relation.UserConnectedResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIService {

    @POST("login")
    fun login(@Body spec: LoginSpec, @Header("Authorization") tokenAuth: String?): Single<LoginResponse>

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

}