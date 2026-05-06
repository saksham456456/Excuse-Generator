package com.example.savageexcuse

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savageexcuse.databinding.ActivityMyStashBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyStashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyStashBinding
    private val viewModel: StashViewModel by viewModels()
    private lateinit var adapter: StashAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyStashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        adapter = StashAdapter { excuse ->
            viewModel.removeFavorite(excuse)
        }
        binding.rvStash.layoutManager = LinearLayoutManager(this)
        binding.rvStash.adapter = adapter
    }

    private fun observeFavorites() {
        lifecycleScope.launch {
            viewModel.favoriteExcuses.collectLatest { favorites ->
                if (favorites.isEmpty()) {
                    binding.rvStash.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.VISIBLE
                } else {
                    binding.rvStash.visibility = View.VISIBLE
                    binding.tvEmptyState.visibility = View.GONE
                    adapter.submitList(favorites)
                }
            }
        }
    }
}
