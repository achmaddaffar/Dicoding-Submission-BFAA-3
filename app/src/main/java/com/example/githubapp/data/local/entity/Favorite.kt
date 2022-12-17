package com.example.githubapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Favorite")
@Parcelize
data class Favorite(
    @ColumnInfo(name = "profile_picture")
    var profile_picture: String? = null,

    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String = ""
) : Parcelable
