package com.example.githubapp.ui.detailuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.data.remote.response.UserFollowItem
import com.example.githubapp.databinding.ItemRowUserBinding

class UserFollowAdapter(private val userFollow: List<UserFollowItem>) :
    RecyclerView.Adapter<UserFollowAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvUsername.text = userFollow[position].login.toString()

        Glide.with(holder.itemView.context)
            .load(userFollow[position].avatarUrl)
            .error(R.drawable.githublogo)
            .circleCrop()
            .into(holder.binding.ivUserProfilePicture)
    }

    override fun getItemCount(): Int {
        return userFollow.size
    }
}