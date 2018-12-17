package com.app.o.api.register

data class RegisterSpec (
        val name: String,
        val phone_number: String,
        val email: String,
        val password: String)