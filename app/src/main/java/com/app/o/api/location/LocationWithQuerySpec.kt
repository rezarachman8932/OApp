package com.app.o.api.location

data class LocationWithQuerySpec (val keyword: String?, override val latitude: String, override val longitude: String) : BaseLocation