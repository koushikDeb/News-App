package com.example.assignmentnewsapp.ui.fragments.allnews

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentnewsapp.databinding.FragmentAllNewsBinding
import com.example.assignmentnewsapp.ui.base.BaseFragment
import com.example.assignmentnewsapp.ui.paging.loadState.ReposLoadStateAdapter
import com.example.assignmentnewsapp.utils.Constants.Companion.fromDate
import com.example.assignmentnewsapp.utils.Constants.Companion.query
import com.example.assignmentnewsapp.utils.Constants.Companion.toDate

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllNewsFragment : BaseFragment<AllNewsViewModel, FragmentAllNewsBinding>(
  FragmentAllNewsBinding::inflate
) {
  private val viewModel: AllNewsViewModel by viewModels()
  private var allNewsAdapter: AllNewsAdapter? = null

  private fun setupUI() {
    binding?.recyclerView?.layoutManager = LinearLayoutManager(activity)
    bindFooter()
  }

  private fun bindFooter() {
    allNewsAdapter = AllNewsAdapter()
    val reposLoadStateAdapter = ReposLoadStateAdapter() { allNewsAdapter?.retry() }
    binding?.recyclerView?.adapter = allNewsAdapter?.withLoadStateFooter(reposLoadStateAdapter)
  }

  @ExperimentalPagingApi
  override fun setupObservers() {
    lifecycleScope.launch {
      viewModel.getListData(query, fromDate, toDate).distinctUntilChanged().collectLatest {
        binding?.recyclerView?.visibility = View.VISIBLE
        allNewsAdapter?.submitData(it)
      }
    }
  }

  override fun provideViewModel() = viewModel

  override fun setupView(view: View) {
    setupUI()
  }
}