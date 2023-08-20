package com.zurmati.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.zurmati.newsapp.databinding.FragmentDetailBinding
import com.zurmati.newsapp.viewmodel.DataViewModel

class DetailFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[DataViewModel::class.java]
    }

    var binding: FragmentDetailBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedArticle.observe(viewLifecycleOwner) { article ->

            Log.i("MyInfo", "onViewCreated: ${article.content}")


            binding?.title?.text = article.title
            binding?.description?.text = article.description
            binding?.content?.text = article.content

            Glide.with(requireActivity()).load(article.urlToImage).into(binding?.img!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}