package com.aimcode.userdirectory.core.navigation.destinantion

object AddUserDestination: AppDestination {
    override val route: String
        get() = "/add-user"
    override val topLevel: Boolean
        get() = false
}