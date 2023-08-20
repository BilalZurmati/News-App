package com.zurmati.newsapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zurmati.newsapp.R
import com.zurmati.newsapp.adapters.SourcesAdapter
import com.zurmati.newsapp.databinding.ActivityMainBinding
import com.zurmati.newsapp.databinding.SoucesSheetBinding
import com.zurmati.newsapp.helpers.SharedPref
import com.zurmati.newsapp.models.SourcesResponse
import com.zurmati.newsapp.sealed.SourcesState
import com.zurmati.newsapp.viewmodel.DataViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[DataViewModel::class.java]
    }

    private val controller by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    companion object {
        fun launch(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }
    }


    private var sources: SourcesResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.fetchSources()

        viewModel.sources.onEach { value ->
            when (value) {
                is SourcesState.Empty -> {
                }

                is SourcesState.Data -> {
                    sources = value.response
                }

                is SourcesState.Error -> {
                    Toast.makeText(this@MainActivity, value.message, Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(lifecycleScope)

        binding.title.text = SharedPref.getString("name", getString(R.string.app_name))


        binding.txtChangeSource.setOnClickListener {
            sources?.let {
                showSourcesBottomSheetDialog(it)
            } ?: Toast.makeText(
                this@MainActivity,
                "Sources have not been fetchaed yet",
                Toast.LENGTH_SHORT
            ).show()
        }


        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home -> {
                    binding.txtChangeSource.visibility = View.VISIBLE
                }

                R.id.detail_screen -> {
                    binding.txtChangeSource.visibility = View.GONE
                }
            }
        }
    }

    private fun showSourcesBottomSheetDialog(sourcesResponse: SourcesResponse) {
        val dialog = BottomSheetDialog(this)
        val sourcesSheet = SoucesSheetBinding.inflate(layoutInflater)

        val adapter = SourcesAdapter(this@MainActivity, sourcesResponse.sources) { source ->
            SharedPref.putString("source", source.id!!)
            SharedPref.putString("name", source.name!!)

            binding.title.text = source.name
            viewModel.fetchTopHeadlinesFrom(source.id)

            dialog.dismiss()
        }

        sourcesSheet.recyclerSources.adapter = adapter

        dialog.setContentView(sourcesSheet.root)
        dialog.show()
    }
}