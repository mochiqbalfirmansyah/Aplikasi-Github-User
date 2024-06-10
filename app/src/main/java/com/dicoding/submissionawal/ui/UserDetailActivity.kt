package com.dicoding.submissionawal.ui
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.submissionawal.R
import com.dicoding.submissionawal.Viewmodel.DetailViewModel
import com.dicoding.submissionawal.Viewmodel.ViewModelFactory
import com.dicoding.submissionawal.database.FavoriteEntity
import com.dicoding.submissionawal.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class UserDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var detailViewModel : DetailViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        val getUsername = intent.getStringExtra(EXTRA_USERNAME)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        super.onCreate(savedInstanceState)


        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (getUsername != null) {
            val sectionPagerAdapter = SectionsPagerAdapter(this, getUsername)
            val viewPager: ViewPager2 = findViewById(R.id.viewPager)
            viewPager.adapter = sectionPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tab)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
        detailViewModel = ViewModel(this@UserDetailActivity)

        var favoriteUser: FavoriteEntity = FavoriteEntity().apply {

            if (getUsername != null) {
                this.username = getUsername
            }
            this.avatarUrl = avatar

        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.isFavorite.observe(this){
            ifFavorite(it)
        }

        if (getUsername != null) {
            detailViewModel.getDetailUser(getUsername)
        }


        detailViewModel.detailUser.observe(this) {
            if (it != null) {
                Glide.with(this@UserDetailActivity)
                    .load(it.avatarUrl)
                    .centerCrop()
                    .into(binding.imgUserDetail)
                binding.tvName.text = it.name
                binding.tvUsername.text = it.login
                binding.tvFollower.text = "${it.followers} Follower"
                binding.tvFollowing.text = "${it.following} Following"
            }
        }

        detailViewModel.getListFavorite().observe(this) {favoritedUsers ->
            favoritedUsers?.forEach {
                if (it.username == getUsername) {
                    detailViewModel.setFavorite(true)
                    favoriteUser = it
                }
            }
        }
        binding.detailFabFavorite?.setOnClickListener {
            detailViewModel.updateFavoriteUser(favoriteUser, this)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun ifFavorite(ifFavorite: Boolean){
        if(ifFavorite){
            binding.detailFabFavorite?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_favorite_24))
        }else{
            binding.detailFabFavorite?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_favorite_border_24))
        }
    }
    private fun ViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
        const val EXTRA_USERNAME = "USERNAME"
        const val EXTRA_AVATAR ="AVATAR"
    }



}
