package com.example.githubapp.ui.favoriteuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.local.entity.Favorite
import com.example.githubapp.data.local.repository.FavoriteRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorite(): LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorites()
}