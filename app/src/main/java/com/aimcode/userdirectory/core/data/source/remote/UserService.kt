package com.aimcode.userdirectory.core.data.source.remote

import com.aimcode.userdirectory.core.model.response.CityResponse
import com.aimcode.userdirectory.core.model.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/user")
    suspend fun getUsers(
        @Query("city") city: String? = null
    ): List<UserResponse>

    @GET("/city")
    suspend fun getCities(): List<CityResponse>
}