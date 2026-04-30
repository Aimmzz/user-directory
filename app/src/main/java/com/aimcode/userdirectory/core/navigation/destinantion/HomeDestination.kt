package com.aimcode.userdirectory.core.navigation.destinantion

object HomeDestination : AppDestination {
    override val route: String
        get() = "/home"
    override val topLevel: Boolean
        get() = true
}