package com.aimcode.userdirectory.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimcode.userdirectory.core.data.source.local.entity.PendingUserEntity

@Dao
interface PendingUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: PendingUserEntity)

    @Query("SELECT * FROM pending_user")
    suspend fun getAll(): List<PendingUserEntity>

    @Query("DELETE FROM pending_user WHERE id = :id")
    suspend fun deleteById(id: Int)
}