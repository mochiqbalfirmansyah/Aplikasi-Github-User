package com.dicoding.submissionawal.Viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionawal.data.response.DetailUserResponse
import com.dicoding.submissionawal.data.response.ItemsItem
import com.dicoding.submissionawal.data.retrofit.ApiConfig
import com.dicoding.submissionawal.database.FavoriteEntity
import com.dicoding.submissionawal.repository.FavoriteRepository
import com.dicoding.submissionawal.ui.UserDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel( application: Application) : ViewModel() {

    var detailLoading = false
    var followerLoading = false
    var followingLoading = false

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listFollower = MutableLiveData<List<ItemsItem>>()
    val userFollower: LiveData<List<ItemsItem>> = _listFollower

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val userFollowing: LiveData<List<ItemsItem>> = _listFollowing

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean> = _isFavorite




    fun getDetailUser(username: String) {
        if (!detailLoading) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getDetailUser(username)
            client.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _detailUser.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            detailLoading = true
        }
    }
    fun getFollower(username: String) {
        if (!followerLoading ) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getFollowers(username)
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listFollower.postValue(response.body())

                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            followerLoading = true
        }
    }

    fun getFollowing(username: String) {
        if (!followingLoading ) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getFollowing(username)
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listFollowing.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            followingLoading = true
        }

    }
    fun getListFavorite(): LiveData<List<FavoriteEntity>> = mFavoriteRepository.getAllFavorites()

    fun setFavorite(isFavorite: Boolean){
        _isFavorite.value = isFavorite
    }

    private fun addFavorite(favoriteUser: FavoriteEntity){
        setFavorite(true)
        mFavoriteRepository.insert(favoriteUser)
    }

    private fun removeFavorite(favoriteUser: FavoriteEntity){
        setFavorite(false)
        mFavoriteRepository.delete(favoriteUser)
    }

    fun updateFavoriteUser(favUser: FavoriteEntity, activity: UserDetailActivity){
        if( isFavorite.value != true ){
            addFavorite(favUser)
            Toast.makeText(activity, "User Ditambahkan di Favorite", Toast.LENGTH_SHORT).show()
        }else{
            removeFavorite(favUser)
            Toast.makeText(activity, "User Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {
        private const val TAG = "DetailViewModel"
    }
}