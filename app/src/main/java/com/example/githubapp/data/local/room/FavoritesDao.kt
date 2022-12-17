package com.example.githubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubapp.data.local.entity.Favorite

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM Favorite")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM Favorite WHERE username = :username")
    suspend fun delete(username: String)

    @Query("SELECT EXISTS (SELECT * FROM Favorite WHERE username = :username)")
    suspend fun isUserFavorite(username: String): Boolean
}