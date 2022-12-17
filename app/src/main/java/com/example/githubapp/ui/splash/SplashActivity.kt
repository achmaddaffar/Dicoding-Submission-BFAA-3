package com.example.githubapp.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubapp.R
import com.example.githubapp.databinding.ActivitySplashBinding
import com.example.githubapp.ui.searchuser.SearchUserActivity
import com.example.githubapp.ui.settings.SettingPreferences
import com.example.githubapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_settings.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(dataStore)
        val viewModel = obtainViewModel(this, pref)

        val handler = Handler(Looper.getMainLooper())
        viewModel.getTheme().observe(this@SplashActivity) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.github_black
                    )
                )
                binding.ivSplashlogo.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.ivSplashlogo.context,
                        R.drawable.githublogo_darkmode
                    )
                )
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
                binding.ivSplashlogo.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.ivSplashlogo.context,
                        R.drawable.githublogo
                    )
                )
            }
        }

        handler.postDelayed({
            val intent = Intent(this@SplashActivity, SearchUserActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000L)
    }

    private fun obtainViewModel(
        activity: AppCompatActivity,
        pref: SettingPreferences
    ): SplashViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[SplashViewModel::class.java]
    }
}