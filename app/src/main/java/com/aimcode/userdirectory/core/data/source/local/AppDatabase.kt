package com.aimcode.userdirectory.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aimcode.userdirectory.core.data.source.local.dao.CityDao
import com.aimcode.userdirectory.core.data.source.local.dao.UserDao
import com.aimcode.userdirectory.core.data.source.local.entity.CityEntity
import com.aimcode.userdirectory.core.data.source.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, CityEntity::class],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = false,
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun cityDao(): CityDao

    companion object {
        const val DATABASE_VERSION: Int = 1;
    }
}