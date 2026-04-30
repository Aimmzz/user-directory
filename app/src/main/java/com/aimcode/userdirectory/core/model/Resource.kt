package com.aimcode.userdirectory.core.model

sealed class Resource<T> {
    data class Success<T>(val data: T? = null) : Resource<T>()
    data class Failed<T>(val code: Int? = null, val message: String? = null) : Resource<T>()
}