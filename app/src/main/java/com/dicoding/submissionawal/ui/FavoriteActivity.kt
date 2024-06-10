package com.dicoding.submissionawal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionawal.Viewmodel.FavoriteViewModel
import com.dicoding.submissionawal.Viewmodel.ViewModelFactory
import com.dicoding.submissionawal.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        viewModel = obtainViewModel(this@FavoriteActivity)

        viewModel.getAllFavorites().observe(this) {
            val adapter = FavoriteAdapter()
            adapter.submitList(it)
            binding.rvUser.adapter = adapter
        }


    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

}