package com.example.assignmentnewsapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assignmentnewsapp.ui.fragments.BlankFragment
import com.example.assignmentnewsapp.ui.fragments.CountryNewsFragment
import com.example.assignmentnewsapp.ui.fragments.allnews.AllNewsFragment
import com.example.assignmentnewsapp.ui.fragments.topHeadlines.HeadlinesFragment
import com.example.assignmentnewsapp.utils.Constants

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
  override fun getItemCount(): Int {
    return Constants.topics.size
  }

  override fun createFragment(position: Int): Fragment {
    var loadingFrag: Fragment = BlankFragment();
    when (position) {
      0 -> loadingFrag = AllNewsFragment()
      1 -> loadingFrag = HeadlinesFragment()
      2 -> loadingFrag = CountryNewsFragment()
    }
    return loadingFrag;
  }
}