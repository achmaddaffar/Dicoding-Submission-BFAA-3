package com.example.githubapp.data.remote.retrofit

import com.example.githubapp.BuildConfig
import com.example.githubapp.data.remote.response.UserDetail
import com.example.githubapp.data.remote.response.UserFollowItem
import com.example.githubapp.data.remote.response.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

//https://api.github.com/search/users?q=sidiqpermana
//https://api.github.com/users/sidiqpermana
//https://api.github.com/users/damieroliverroot/followers

interface ApiService {
    companion object {
        private const val tokenAuth: String = BuildConfig.API_KEY
    }

    @GET("search/users")
    @Headers("Authorization: token $tokenAuth")
    fun getUserByUsername(
        @Query("q") q: String
    ): Call<UsersResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $tokenAuth")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetail>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $tokenAuth")
    fun getUserFollower(
        @Path("username") username: String
    ): Call<List<UserFollowItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $tokenAuth")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UserFollowItem>>
}