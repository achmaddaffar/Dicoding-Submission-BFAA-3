package com.example.githubapp.ui.searchuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.utils.Event
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.data.remote.response.User
import com.example.githubapp.data.remote.response.UsersResponse
import retrofit2.Call
import retrofit2.Response

class SearchUserViewModel : ViewModel() {

    private val mUserList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = mUserList

    private val mIsLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = mIsLoading

    private val mSnackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = mSnackBarText

    fun getUserList(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserByUsername(username)
        client.enqueue(object : retrofit2.Callback<UsersResponse> {
            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        mUserList.value = responseBody.items
                        val userCount = responseBody.totalCount
                        if (userCount > 0)
                            mSnackBarText.value = Event(SUCCESS)
                        else
                            mSnackBarText.value = Event(USER_NOT_FOUND)
                    }
                } else {
                    mSnackBarText.value = Event(response.message().toString())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                showLoading(false)
                mSnackBarText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        mIsLoading.value = isLoading
    }

    companion object {
        private const val TAG = "SearchUserViewModel"
        private const val SUCCESS = "Success"
        private const val USER_NOT_FOUND = "User tidak dapat ditemukan"
    }
}
