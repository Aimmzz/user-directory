package com.aimcode.userdirectory.core.data.source

import com.aimcode.userdirectory.core.data.repository.UserRepository
import com.aimcode.userdirectory.core.data.source.remote.UserService
import com.aimcode.userdirectory.core.model.Resource
import com.aimcode.userdirectory.core.model.response.CityResponse
import com.aimcode.userdirectory.core.model.response.UserResponse
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class UserSource @Inject constructor(
    private val userService: UserService,
): UserRepository {
    override suspend fun getUsers(city: String?): Resource<List<UserResponse>> {
        return try {
            val result = userService.getUsers(city)
            Timber.d(result.toString())

            Resource.Success(result)
        } catch (e: HttpException) {
            Timber.e(e)
            Resource.Failed()
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Failed()
        }
    }

    override suspend fun getCities(): Resource<List<CityResponse>> {
        return try {
            val result = userService.getCities()
            Timber.d(result.toString())

            Resource.Success(result)
        } catch (e: HttpException) {
            Timber.e(e)
            Resource.Failed()
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Failed()
        }
    }
}