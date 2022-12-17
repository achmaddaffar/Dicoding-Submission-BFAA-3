package com.example.githubapp.ui.searchuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.data.remote.response.User
import com.example.githubapp.databinding.ActivitySearchUserBinding
import com.example.githubapp.ui.detailuser.DetailUserActivity
import com.example.githubapp.ui.favoriteuser.FavoriteUserActivity
import com.example.githubapp.ui.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar

class SearchUserActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchUserBinding
    private val viewModel: SearchUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.mainactionbartitle)

        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.apply {
            rvUserlist.layoutManager = layoutManager
            rvUserlist.addItemDecoration(itemDecoration)
            rvUserlist.setHasFixedSize(true)
        }

        viewModel.userList.observe(this) { userList ->
            setUserList(userList)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_user_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                viewModel.getUserList(s)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                return true
            }
        })

        viewModel.snackBarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(
                        ContextCompat.getColor(
                            this,
                            R.color.github_black
                        )
                    )
                    .setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.github_white
                        )
                    )
                    .show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserList(userList: List<User>) {
        val adapter = SearchUserAdapter(userList)
        binding.rvUserlist.adapter = adapter
        adapter.setOnItemClickCallback(object : SearchUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@SearchUserActivity, DetailUserActivity::class.java)
                intent.putExtra(USER, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_favorite -> {
                val intent = Intent(this@SearchUserActivity, FavoriteUserActivity::class.java)
                startActivity(intent)
            }
            R.id.action_setting -> {
                val intent = Intent(this@SearchUserActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "SearchUserActivity"
        const val USER = "USER"
    }
}
