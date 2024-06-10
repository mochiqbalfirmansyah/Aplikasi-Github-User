package com.dicoding.submissionawal.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.submissionawal.database.FavoriteDao
import com.dicoding.submissionawal.database.FavoriteEntity
import com.dicoding.submissionawal.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavDao : FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavDao = db.favDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavDao.getAllFavorites()

    fun insert(fav : FavoriteEntity) {
        executorService.execute { mFavDao.insert(fav) }
    }

    fun delete(fav: FavoriteEntity) {
        executorService.execute { mFavDao.delete(fav) }
    }

}