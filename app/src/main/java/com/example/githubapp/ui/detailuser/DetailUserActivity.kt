package com.example.githubapp.ui.detailuser

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.data.local.entity.Favorite
import com.example.githubapp.databinding.ActivityDetailUserBinding
import com.example.githubapp.ui.searchuser.SearchUserActivity
import com.example.githubapp.ui.settings.SettingPreferences
import com.example.githubapp.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val viewModel = obtainViewModel(this, pref)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val item = intent.getStringExtra(SearchUserActivity.USERNAME) as String
        supportActionBar?.title = item

        viewModel.apply {
            getUserDetail(item)
            profPicLink.observe(this@DetailUserActivity) {
                Glide.with(this@DetailUserActivity)
                    .load(it)
                    .error(R.drawable.githublogo)
                    .into(binding.ivUserProfilePicture)
            }
            name.observe(this@DetailUserActivity) {
                binding.tvName.text = it
            }
            bio.observe(this@DetailUserActivity) {
                binding.tvBio.text = it
            }
            publicRepos.observe(this@DetailUserActivity) {
                binding.tvPublicRepos.text = it.toString()
            }
            followerCount.observe(this@DetailUserActivity) {
                binding.tvFollowers.text = it.toString()
            }
            followingCount.observe(this@DetailUserActivity) {
                binding.tvFollowing.text = it.toString()
            }
            isLoading.observe(this@DetailUserActivity) {
                showLoading(it)
            }
            snackBarText.observe(this@DetailUserActivity) {
                it.getContentIfNotHandled()?.let { snackBarText ->
                    Snackbar.make(
                        binding.root,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(
                            ContextCompat.getColor(
                                this@DetailUserActivity,
                                R.color.github_orange
                            )
                        )
                        .setTextColor(
                            ContextCompat.getColor(
                                this@DetailUserActivity,
                                R.color.github_white
                            )
                        )
                        .show()
                }
            }
            setFavoriteDrawable(isUserFavorite(item))
        }

        binding.apply {
            fabFavorite.setOnClickListener {
                if (viewModel.isUserFavorite(item)) {
                    viewModel.delete(item)
                    setFavoriteDrawable(false)
                    Toast.makeText(this@DetailUserActivity, DELETE_FAV, Toast.LENGTH_SHORT).show()
                } else {
                    val favorite = Favorite()
                    favorite.let {
                        it.username = item
                        it.profile_picture = viewModel.profPicLink.value.toString()
                    }
                    viewModel.insert(favorite)
                    setFavoriteDrawable(true)
                    Toast.makeText(this@DetailUserActivity, ADD_FAV, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setFavoriteDrawable(isUserFavorite: Boolean) {
        if (isUserFavorite) {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fabFavorite.context,
                    R.drawable.ic_favorite
                )
            )
        } else {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fabFavorite.context,
                    R.drawable.ic_favorite_no
                )
            )
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity,
        preferences: SettingPreferences
    ): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, preferences)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            fabFavorite.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_1,
            R.string.tan_title_2
        )

        private const val ADD_FAV = "Sukses menambahkan ke Favorit"
        private const val DELETE_FAV = "Sukses menghapus dari Favorit"
    }
}
