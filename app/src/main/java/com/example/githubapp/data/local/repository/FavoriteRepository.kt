package com.example.githubapp.data.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubapp.data.local.entity.Favorite
import com.example.githubapp.data.local.room.FavoriteRoomDatabase
import com.example.githubapp.data.local.room.FavoritesDao

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoritesDao

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoriteDao.getAllFavorites()

    suspend fun insert(favorite: Favorite) {
        mFavoriteDao.insert(favorite)
    }

    suspend fun delete(username: String) {
        mFavoriteDao.delete(username)
    }

    suspend fun isUserFavorite(username: String): Boolean {
        return mFavoriteDao.isUserFavorite(username)
    }
}