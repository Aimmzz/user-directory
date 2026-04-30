package com.aimcode.userdirectory.core.di

import com.aimcode.userdirectory.core.data.repository.UserRepository
import com.aimcode.userdirectory.core.data.source.UserSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindUserRepository(source: UserSource): UserRepository
}