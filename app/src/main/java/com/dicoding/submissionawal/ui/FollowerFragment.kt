package com.dicoding.submissionawal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionawal.Viewmodel.DetailViewModel
import com.dicoding.submissionawal.Viewmodel.ViewModelFactory
import com.dicoding.submissionawal.databinding.FragmentFollowerBinding


class FollowerFragment : Fragment() {
    private var position = 0
    private var username: String = ""
    private lateinit var binding : FragmentFollowerBinding
    private lateinit var detailViewModel : DetailViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFollowerBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = DetailViewModel(requireActivity())
        arguments?.let { foll ->
            position = foll.getInt(POSITION)
            username = foll.getString(USERNAME) ?: "iqbal"
        }
        detailViewModel.getFollower(username)
        detailViewModel.getFollowing(username)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollower.layoutManager = layoutManager

        detailViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        if (position == 1) {
            detailViewModel.userFollower.observe(viewLifecycleOwner) { followers ->
                val adapter = MainAdapter()
                adapter.submitList(followers)
                binding.rvFollower.adapter = adapter
            }
        }else{
            detailViewModel.userFollowing.observe(viewLifecycleOwner){following ->
                val adapter = MainAdapter()
                adapter.submitList(following)
                binding.rvFollower.adapter = adapter
            }
        }
    }


    companion object {
       const val USERNAME = "iqbal"
       const val POSITION = "0"

    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    private fun DetailViewModel(activity: FragmentActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(requireActivity(), factory)[DetailViewModel::class.java]
    }
}