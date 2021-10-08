package com.example.assignmentnewsapp.ui.fragments.allnews

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentnewsapp.databinding.FragmentAllNewsBinding
import com.example.assignmentnewsapp.ui.base.BaseFragment
import com.example.assignmentnewsapp.ui.fragments.allnews.loadState.ReposLoadStateAdapter
import com.example.assignmentnewsapp.utils.Constants.Companion.fromDate
import com.example.assignmentnewsapp.utils.Constants.Companion.query
import com.example.assignmentnewsapp.utils.Constants.Companion.toDate
import com.example.assignmentnewsapp.utils.NewsAppBindingAdapters.booleanVisibility

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllNewsFragment : BaseFragment<AllNewsViewModel, FragmentAllNewsBinding>(
  FragmentAllNewsBinding::inflate
) {
  private val viewModel: AllNewsViewModel by viewModels()

  private fun setupUI() {
    binding?.recyclerView?.layoutManager = GridLayoutManager(activity, 2)

    bindFooter()
  }

  private fun bindFooter() {
    binding?.recyclerView?.adapter = AllNewsAdapter().apply {
      withLoadStateFooter(ReposLoadStateAdapter { retry() })
    }
  }

  @ExperimentalPagingApi
  override fun setupObservers() {
    lifecycleScope.launch {
      viewModel.getListData(query, fromDate, toDate).distinctUntilChanged().collectLatest {
        binding?.recyclerView?.visibility = View.VISIBLE
        (binding?.recyclerView?.adapter as AllNewsAdapter).submitData(it)
      }
    }

    // viewLifecycleOwner.lifecycleScope.launch {
    //   (binding?.recyclerView?.adapter as AllNewsAdapter).loadStateFlow.collectLatest { loadStates ->
    //     binding?.progressBar?.booleanVisibility(loadStates.refresh is LoadState.Loading)
    //   }
    // }

  }

  override fun provideViewModel() = viewModel

  override fun setupView(view: View) {
    setupUI()
  }
}