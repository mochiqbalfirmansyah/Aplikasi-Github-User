package com.dicoding.submissionawal.ui
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionawal.data.response.ItemsItem
import com.dicoding.submissionawal.databinding.UserListBinding


class MainAdapter : ListAdapter<ItemsItem, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
    class MyViewHolder(val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(getUser: ItemsItem){
            binding.tvUser.text = getUser.login
            Glide.with(binding.root)
                .load(getUser.avatarUrl)
                .into(binding.userPhoto)
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, UserDetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, getUser.login)
                intent.putExtra(EXTRA_AVATAR, getUser.avatarUrl)
                intent.putExtra(EXTRA_ID, getUser.id)
                binding.root.context.startActivity(intent)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
        const val EXTRA_USERNAME = "USERNAME"
        const val EXTRA_AVATAR = "AVATAR"
        const val EXTRA_ID = "ID"
    }
}