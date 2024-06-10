package com.dicoding.submissionawal.ui
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionawal.R
import com.dicoding.submissionawal.Viewmodel.MainViewModel
import com.dicoding.submissionawal.data.response.ItemsItem
import com.dicoding.submissionawal.databinding.ActivityMainBinding
import com.dicoding.submissionawal.settings.SettingPreferences
import com.dicoding.submissionawal.Viewmodel.SettingViewModel
import com.dicoding.submissionawal.Viewmodel.SettingViewModelFactory
import com.dicoding.submissionawal.settings.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val ViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textViev, actionId, event ->

                searchBar.setText(searchView.text)
                searchView.hide()
                ViewModel.SearchUser(searchView.text.toString())
                ViewModel.listUser.observe(this@MainActivity) { user ->

                    if (user.isNullOrEmpty()) {
                        NotFound(true)
                    } else {
                        NotFound(false)
                    }
                }
                false
            }
        }

        ViewModel.isLoading.observe(this){
            showLoading(it)
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager


        ViewModel.listUser.observe(this){
            if (it != null) {
                setUserData(it)
            }
        }

        binding.toolbar.setOnMenuItemClickListener { menuitem ->
            when (menuitem.itemId) {
                R.id.favorites -> {
                    val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings -> {
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else-> false
            }
        }
    }
    private fun setUserData(dataUser: List<ItemsItem>) {
        val adapter = MainAdapter()
        adapter.submitList(dataUser)
        binding.rvUser.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    private fun NotFound(dataNotFound: Boolean) {
        binding.apply {
            if (dataNotFound) {
                rvUser.visibility = View.GONE
                notFound.visibility = View.VISIBLE
            } else {
                rvUser.visibility = View.VISIBLE
                notFound.visibility = View.GONE
            }
        }
    }

}