 package com.example.assignmentnewsapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.assignmentnewsapp.databinding.ActivityMainBinding
import com.example.assignmentnewsapp.utils.Constants
import com.google.android.material.tabs.TabLayoutMediator

 class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.newsViewpager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.newsTabs,binding.newsViewpager){tab,position ->
            tab.text = Constants.topics[position]
        }.attach()
    }
}