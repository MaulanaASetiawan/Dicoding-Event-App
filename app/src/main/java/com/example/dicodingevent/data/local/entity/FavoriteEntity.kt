package com.example.dicodingevent.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    @field:ColumnInfo(name = "id")
    var id: String = "",

    @field:ColumnInfo(name = "name")
    var name: String = "",

    @field:ColumnInfo(name = "owner")
    var owner: String = "",

    @field:ColumnInfo(name = "imageLogo")
    var imageLogo: String? = null,

    @field:ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
): Parcelable
