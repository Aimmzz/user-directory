package com.aimcode.userdirectory.core.navigation.destinantion

sealed interface AppDestination {
    val route: String
    val topLevel: Boolean
}