package com.aimcode.userdirectory.core.data.mapper

import com.aimcode.userdirectory.core.data.source.local.entity.CityEntity
import com.aimcode.userdirectory.core.model.response.CityResponse

fun CityResponse.toEntity() = CityEntity(id = id, name = name)
fun CityEntity.toResponse() = CityResponse(id = id, name = name)