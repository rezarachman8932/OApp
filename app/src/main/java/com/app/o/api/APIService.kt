package com.app.o.api

import com.app.o.api.home.HomePostItem
import com.app.o.api.login.LoginResponse
import com.app.o.api.login.LoginSpec
import com.app.o.api.register.RegisterResponse
import com.app.o.api.register.RegisterSpec
import io.reactivex.Single
import retrofit2.http.*

interface APIService {

    @POST("login")
    fun login(@Body spec: LoginSpec, @Header("Authorization") tokenAuth: String?): Single<LoginResponse>

    @POST("register")
    fun register(@Body spec: RegisterSpec): Single<RegisterResponse>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("posts")
    fun post(
            @Field("longitude") longitude: String,
            @Field("latitude") latitude: String,
            @Header("Authorization") tokenAuth: String?): Single<List<HomePostItem>>

}