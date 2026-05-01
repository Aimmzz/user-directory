package com.aimcode.userdirectory.core.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.aimcode.userdirectory.core.data.source.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context = context,
        klass = AppDatabase::class.java,
        name = "app.db").build()

    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    fun provideCityDao(database: AppDatabase) = database.cityDao()

    @Provides
    fun providePendingUserDao(database: AppDatabase) = database.pendingUserDao()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}