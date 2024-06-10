package com.dicoding.submissionawal.Viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionawal.database.FavoriteEntity
import com.dicoding.submissionawal.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavrepository : FavoriteRepository = FavoriteRepository(application)
    fun getAllFavorites() : LiveData<List<FavoriteEntity>> = mFavrepository.getAllFavorites()
}