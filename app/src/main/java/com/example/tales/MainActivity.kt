package com.example.tales

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tales.api.ApiConfiguration
import com.example.tales.authenticator.SignActivity
import com.example.tales.databinding.ActivityMainBinding
import com.example.tales.maps.MapsActivity
import com.example.tales.ui.story.CreateStoryActivity
import com.example.tales.ui.story.StoryPagingAdapter
import com.example.tales.viewmodel.StoryViewModel
import com.example.tales.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var storyPagingAdapter: StoryPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = ViewModelProvider(this, ViewModelFactory(Injector.provideStoryRepository(this))).get(StoryViewModel::class.java)

        storyPagingAdapter = StoryPagingAdapter(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = storyPagingAdapter

        lifecycleScope.launch {
            val token = storyViewModel.getToken().first()
            Log.d("MainActivity", "Retrieved token: $token")
            if (!token.isNullOrEmpty()) {
                ApiConfiguration.setToken(token)
                Log.d("MainActivity", "Token set in ApiConfiguration")
                storyViewModel.getStories().collectLatest { pagingData ->
                    storyPagingAdapter.submitData(pagingData)
                }
            } else {
                Log.e("MainActivity", "Token is null or empty")
            }
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, CreateStoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                storyViewModel.clearToken()
                Injector.provideAuthRepository(this@MainActivity).clearSession()
                val intent = Intent(this@MainActivity, SignActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.btnMaps.setOnClickListener {
            Log.d("MainActivity", "Maps button clicked")
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}
