package com.example.githubapp.ui.searchuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.data.remote.response.User
import com.example.githubapp.databinding.ItemRowUserBinding

class SearchUserAdapter(private val listUser: List<User>) :
    RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvUsername.text = listUser[position].login.toString()

        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .error(R.drawable.githublogo)
            .circleCrop()
            .into(holder.binding.ivUserProfilePicture)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.absoluteAdapterPosition].login.toString())
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}