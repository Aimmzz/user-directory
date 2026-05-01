package com.aimcode.userdirectory.core.data.mapper

import com.aimcode.userdirectory.core.data.source.local.entity.PendingUserEntity
import com.aimcode.userdirectory.core.data.source.local.entity.UserEntity
import com.aimcode.userdirectory.core.model.request.UserRequest
import com.aimcode.userdirectory.core.model.response.UserResponse

fun UserResponse.toEntity() = UserEntity(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    gender = gender,
    city = city,
)

fun UserEntity.toResponse() = UserResponse(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    gender = gender,
    city = city,
)

fun UserRequest.toEntity() = PendingUserEntity(
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = gender,
)