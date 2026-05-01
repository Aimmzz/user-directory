package com.aimcode.userdirectory.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "address")
    val address: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String = "",

    @ColumnInfo(name = "gender")
    val gender: Int = 0,

    @ColumnInfo(name = "city")
    val city: String = "",
)