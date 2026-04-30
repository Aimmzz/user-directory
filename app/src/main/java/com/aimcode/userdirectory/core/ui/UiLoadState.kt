package com.aimcode.userdirectory.core.ui

sealed class UiLoadState<T> {
    class Idle<T> : UiLoadState<T>()
    class Loading<T> : UiLoadState<T>()
    class Failed<T>(val code: Int? = null) : UiLoadState<T>()
    data class Success<T>(val data: T?) : UiLoadState<T>()
}