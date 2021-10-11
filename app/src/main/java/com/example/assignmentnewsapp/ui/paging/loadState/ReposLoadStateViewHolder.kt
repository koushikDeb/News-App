package com.example.assignmentnewsapp.ui.paging.loadState

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentnewsapp.R
import com.example.assignmentnewsapp.databinding.FooterLoadingStateBinding

class ReposLoadStateViewHolder(
  private val binding: FooterLoadingStateBinding,
  retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

  init {
    binding.retryButton.also {
      it.setOnClickListener { retry.invoke() }
    }
  }

  fun bind(loadState: LoadState) {
    if (loadState is LoadState.Error) {
      binding.errorMsg.text = loadState.error.localizedMessage
    }
    binding.progressBar.isVisible = loadState is LoadState.Loading
    binding.retryButton.isVisible = loadState is LoadState.Error
    binding.errorMsg.isVisible = loadState is LoadState.Error
  }

  companion object {
    fun create(
      parent: ViewGroup,
      retry: () -> Unit
    ): ReposLoadStateViewHolder {
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.footer_loading_state, parent, false)
      val binding = FooterLoadingStateBinding.bind(view)
      return ReposLoadStateViewHolder(binding, retry)

      // val layoutInflater = LayoutInflater.from(parent.context)
      // val footerLoadingStateBinding =
      //   FooterLoadingStateBinding.inflate(layoutInflater, parent, false)
      // return ReposLoadStateViewHolder(footerLoadingStateBinding, retry)
    }
  }
}