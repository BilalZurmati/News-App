package com.zurmati.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.zurmati.newsapp.R
import com.zurmati.newsapp.adapters.NewsAdapter
import com.zurmati.newsapp.databinding.FragmentHomeBinding
import com.zurmati.newsapp.helpers.SharedPref
import com.zurmati.newsapp.sealed.NewsState
import com.zurmati.newsapp.viewmodel.DataViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null


    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[DataViewModel::class.java]
    }

    private val controller by lazy {
        (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        viewModel.fetchTopHeadlinesFrom(SharedPref.getString("source", "bbc-news"))

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.news.onEach { value ->
            when (value) {
                is NewsState.Empty -> {

                }

                is NewsState.Loading -> {
                    binding?.loading?.visibility = View.VISIBLE
                }

                is NewsState.Data -> {
                    binding?.loading?.visibility = View.GONE
                    val adapter = NewsAdapter(requireContext(), value.response.articles) {
                        viewModel.setSelectedNews(it)
                        controller.navigate(R.id.action_home_fragment_to_detail_fragment)
                    }
                    binding?.recyclerHeadlines?.adapter = adapter
                }

                is NewsState.Error -> {
                    binding?.loading?.visibility = View.GONE
                    Toast.makeText(requireContext(), value.message, Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}