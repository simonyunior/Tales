package com.example.tales.ui.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tales.databinding.ActivityStoryDetailBinding
import com.example.tales.model.Story

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Story>("story")
        story?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDescription.text = it.description
            Glide.with(this).load(it.photoUrl).into(binding.ivDetailPhoto)
        }
    }
}
