package com.app.o.api

import com.app.o.api.login.LoginResponse
import com.app.o.api.login.LoginSpec
import com.app.o.api.register.RegisterResponse
import com.app.o.api.register.RegisterSpec
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {

    @POST("login")
    fun login(@Body spec: LoginSpec, @Header("Authorization") tokenAuth: String?): Single<LoginResponse>

    @POST("register")
    fun register(@Body spec: RegisterSpec): Single<RegisterResponse>

}