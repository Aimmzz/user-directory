package com.aimcode.userdirectory.core.navigation

import com.aimcode.userdirectory.core.navigation.destinantion.AddUserDestination
import com.aimcode.userdirectory.core.navigation.destinantion.AppDestination
import com.aimcode.userdirectory.core.navigation.destinantion.HomeDestination

object AppDestinationRegistry {
    val destinations: List<AppDestination> = listOf(
        HomeDestination,
        AddUserDestination,
    )
}