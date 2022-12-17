package com.example.githubapp.ui.favoriteuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.data.local.entity.Favorite
import com.example.githubapp.databinding.ItemRowUserBinding

class FavoriteAdapter :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val favoriteList = ArrayList<Favorite>()

    fun setFavoriteList(favoriteList: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.favoriteList, favoriteList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.favoriteList.clear()
        this.favoriteList.addAll(favoriteList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvUsername.text = favoriteList[position].username.toString()

        Glide.with(holder.itemView.context)
            .load(favoriteList[position].profile_picture)
            .error(R.drawable.githublogo)
            .circleCrop()
            .into(holder.binding.ivUserProfilePicture)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }
}