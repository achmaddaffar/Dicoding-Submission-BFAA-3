package com.example.githubapp.ui.detailuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.local.entity.Favorite
import com.example.githubapp.data.local.repository.FavoriteRepository
import com.example.githubapp.data.remote.response.UserDetail
import com.example.githubapp.data.remote.response.UserFollowItem
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.utils.Event
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite) {
        viewModelScope.launch {
            mFavoriteRepository.insert(favorite)
        }
    }

    fun delete(username: String) {
        viewModelScope.launch {
            mFavoriteRepository.delete(username)
        }
    }

    fun isUserFavorite(username: String): Boolean = runBlocking {
        mFavoriteRepository.isUserFavorite(username)
    }

    private val mFollowerList = MutableLiveData<List<UserFollowItem>>()
    val followerList: LiveData<List<UserFollowItem>> = mFollowerList

    private val mFollowingList = MutableLiveData<List<UserFollowItem>>()
    val followingList: LiveData<List<UserFollowItem>> = mFollowingList

    private val mUsername = MutableLiveData<String>()
    val username: LiveData<String> = mUsername

    private val mName = MutableLiveData<String>()
    val name: LiveData<String> = mName

    private val mBio = MutableLiveData<String>()
    val bio: LiveData<String> = mBio

    private val mPublicRepos = MutableLiveData<Int>()
    val publicRepos: LiveData<Int> = mPublicRepos

    private val mFollowersCount = MutableLiveData<Int>()
    val followerCount: LiveData<Int> = mFollowersCount

    private val mFollowingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> = mFollowingCount

    private val mIsLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = mIsLoading

    private val mSnackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = mSnackBarText

//    private val mIsFavorite = MutableLiveData<Boolean>()
//    val isFavorite: LiveData<Boolean> = mIsFavorite

    private fun showLoading(isLoading: Boolean) {
        mIsLoading.value = isLoading
    }

    fun getUserDetail(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        getFollowerList(username)
                        getFollowingList(username)
                        mFollowersCount.value = responseBody.followers
                        mFollowingCount.value = responseBody.following
                        mPublicRepos.value = responseBody.publicRepos
                        mBio.value = responseBody.bio
                        mName.value = responseBody.name
                        mUsername.value = responseBody.login
                    }
                } else {
                    mSnackBarText.value = Event(response.message().toString())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                showLoading(false)
                mSnackBarText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getFollowerList(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserFollower(username)
        client.enqueue(object : Callback<List<UserFollowItem>> {
            override fun onResponse(
                call: Call<List<UserFollowItem>>,
                response: Response<List<UserFollowItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        mFollowerList.value = responseBody.toList()
                    }
                } else {
                    mSnackBarText.value = Event(response.message().toString())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserFollowItem>>, t: Throwable) {
                showLoading(false)
                mSnackBarText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getFollowingList(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserFollowItem>> {
            override fun onResponse(
                call: Call<List<UserFollowItem>>,
                response: Response<List<UserFollowItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null)
                        mFollowingList.value = responseBody.toList()
                } else {
                    mSnackBarText.value = Event(response.message().toString())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserFollowItem>>, t: Throwable) {
                showLoading(false)
                mSnackBarText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailUserViewModel"
    }
}