package com.aimcode.userdirectory.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimcode.userdirectory.core.data.source.local.entity.CityEntity

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("SELECT * FROM city")
    suspend fun getCities(): List<CityEntity>

    @Query("DELETE FROM city")
    suspend fun deleteCities()
}