package com.dicoding.submissionawal.ui
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionawal.data.response.ItemsItem
import com.dicoding.submissionawal.database.FavoriteEntity
import com.dicoding.submissionawal.databinding.UserListBinding

class FavoriteAdapter : ListAdapter<FavoriteEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gituser: FavoriteEntity){
            binding.tvUser.text = gituser.username
            Glide.with(binding.root)
                .load(gituser.avatarUrl)
                .into(binding.userPhoto)
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, UserDetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, gituser.username)
                intent.putExtra(EXTRA_AVATAR, gituser.avatarUrl)
                binding.root.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem == newItem
            }
        }

        const val EXTRA_USERNAME = "USERNAME"
        const val EXTRA_AVATAR = "AVATAR"
    }
}
