package com.example.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dicodingevent.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM FavoriteEntity ORDER BY id ASC")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT * FROM FavoriteEntity WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun delete(favoriteEntity: FavoriteEntity)
}