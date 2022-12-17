package com.example.githubapp.ui.favoriteuser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.databinding.ActivityFavoriteUserBinding
import com.example.githubapp.ui.detailuser.DetailUserActivity
import com.example.githubapp.ui.settings.SettingPreferences
import com.example.githubapp.utils.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var viewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.favorite_user_title)

        val pref = SettingPreferences.getInstance(dataStore)
        val adapter = FavoriteAdapter()

        viewModel = obtainViewModel(this@FavoriteUserActivity, pref)
        viewModel.getAllFavorite().observe(this) { favoriteList ->
            if (favoriteList != null) {
                adapter.setFavoriteList(favoriteList)
            }
        }


        binding.apply {
            val layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            val itemDecoration =
                DividerItemDecoration(this@FavoriteUserActivity, DividerItemDecoration.VERTICAL)
            rvFavoriteuserlist.apply {
                this.layoutManager = layoutManager
                this.adapter = adapter
                adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: String) {
                        val intent = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                        intent.putExtra(USERNAME, data)
                        startActivity(intent)
                    }
                })
                addItemDecoration(itemDecoration)
                setHasFixedSize(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavorite().observe(this) { favoriteList ->
            if (favoriteList != null) {
                val adapter = FavoriteAdapter()
                adapter.setFavoriteList(favoriteList)
                binding.rvFavoriteuserlist.adapter = adapter
                adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: String) {
                        val intent = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                        intent.putExtra(USERNAME, data)
                        startActivity(intent)
                    }
                })
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

    companion object {
        const val USERNAME = "Username"
    }
}