package com.example.githubapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubapp.ui.settings.SettingPreferences

class SplashViewModel(private val preferences: SettingPreferences): ViewModel() {
    fun getTheme() = preferences.getThemeSetting().asLiveData()
}