package com.dicoding.submissionawal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity, val username :String) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        val fragment = FollowerFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowerFragment.POSITION, position + 1)
            putString(FollowerFragment.USERNAME, username)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}
