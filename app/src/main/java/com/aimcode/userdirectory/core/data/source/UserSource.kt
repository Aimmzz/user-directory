package com.aimcode.userdirectory.core.data.source

import com.aimcode.userdirectory.core.data.mapper.toEntity
import com.aimcode.userdirectory.core.data.mapper.toResponse
import com.aimcode.userdirectory.core.data.repository.UserRepository
import com.aimcode.userdirectory.core.data.source.local.dao.CityDao
import com.aimcode.userdirectory.core.data.source.local.dao.UserDao
import com.aimcode.userdirectory.core.data.source.remote.UserService
import com.aimcode.userdirectory.core.model.Resource
import com.aimcode.userdirectory.core.model.request.UserRequest
import com.aimcode.userdirectory.core.model.response.CityResponse
import com.aimcode.userdirectory.core.model.response.UserResponse
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class UserSource @Inject constructor(
    private val userService: UserService,
    private val userDao: UserDao,
    private val cityDao: CityDao,
): UserRepository {
    override suspend fun getUsers(city: String?): Resource<List<UserResponse>> {
        return try {
            val result = userService.getUsers(city)
            Timber.d(result.toString())

            userDao.deleteUsers()
            userDao.insertUsers(result.map { it.toEntity() })

            Resource.Success(result)
        } catch (e: HttpException) {
            Timber.e(e)
            Resource.Failed()
        } catch (e: Exception) {
            Timber.e(e)
            val cached = if (city != null) {
                userDao.getUsersByCity(city)
            } else {
                userDao.getUsers()
            }
            if (cached.isNotEmpty()) {
                Resource.Success(cached.map { it.toResponse() }, fromCache = true)
            } else {
                Resource.Failed()
            }
        }
    }

    override suspend fun getCities(): Resource<List<CityResponse>> {
        return try {
            val result = userService.getCities()

            cityDao.deleteCities()
            cityDao.insertCities(result.map { it.toEntity() })

            Resource.Success(result)
        } catch (e: HttpException) {
            Timber.e(e)
            Resource.Failed()
        } catch (e: Exception) {
            Timber.e(e)
            val cached = cityDao.getCities()
            if (cached.isNotEmpty()) {
                Resource.Success(cached.map { it.toResponse() }, fromCache = true)
            } else {
                Resource.Failed()
            }
        }
    }

    override suspend fun addUser(request: UserRequest): Resource<UserResponse> {
        return try {

            val result = userService.addUser(
                request = request
            )

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