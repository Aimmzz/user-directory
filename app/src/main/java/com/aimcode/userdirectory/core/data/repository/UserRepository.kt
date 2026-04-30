package com.aimcode.userdirectory.core.data.repository

import com.aimcode.userdirectory.core.model.Resource
import com.aimcode.userdirectory.core.model.response.CityResponse
import com.aimcode.userdirectory.core.model.response.UserResponse

interface UserRepository {
    suspend fun getUsers(city: String? = null): Resource<List<UserResponse>>
    suspend fun getCities(): Resource<List<CityResponse>>
}