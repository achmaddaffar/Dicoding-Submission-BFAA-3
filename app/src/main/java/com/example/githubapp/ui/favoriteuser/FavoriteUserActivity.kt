package com.example.githubapp.ui.favoriteuser

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.databinding.ActivityFavoriteUserBinding
import com.example.githubapp.ui.settings.SettingPreferences
import com.example.githubapp.utils.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.favorite_user_title)

        val pref = SettingPreferences.getInstance(dataStore)
        val adapter = FavoriteAdapter()

        val viewModel = obtainViewModel(this@FavoriteUserActivity, pref)
        viewModel.getAllFavorite().observe(this) { favoriteList ->
            if (favoriteList != null) {
                adapter.setFavoriteList(favoriteList)
            }
            Toast.makeText(this, "list: ${favoriteList.isEmpty()}".toString(), Toast.LENGTH_SHORT)
                .show()
        }


        binding.apply {
            val layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            val itemDecoration =
                DividerItemDecoration(this@FavoriteUserActivity, DividerItemDecoration.VERTICAL)
            rvFavoriteuserlist.apply {
                this.layoutManager = layoutManager
                this.adapter = adapter
                addItemDecoration(itemDecoration)
                setHasFixedSize(true)
            }
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity,
        preferences: SettingPreferences
    ): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, preferences)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }
}