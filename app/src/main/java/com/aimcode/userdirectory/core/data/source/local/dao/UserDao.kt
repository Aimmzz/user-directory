package com.aimcode.userdirectory.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimcode.userdirectory.core.data.source.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<UserEntity>

    @Query("SELECT * FROM user WHERE city = :city")
    suspend fun getUsersByCity(city: String): List<UserEntity>

    @Query("DELETE FROM user")
    suspend fun deleteUsers()
}