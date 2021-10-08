package com.example.assignmentnewsapp.ui.fragments.allnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentnewsapp.databinding.AllNewsRowBinding
import com.example.assignmentnewsapp.networking.model.Article
import com.example.assignmentnewsapp.ui.fragments.allnews.AllNewsAdapter.DataViewHolder

class AllNewsAdapter() : PagingDataAdapter<Article, DataViewHolder>(ArticlesDifferntiator) {

  class DataViewHolder(val allNewsRowBinding: AllNewsRowBinding) : RecyclerView.ViewHolder(
    allNewsRowBinding.root
  ) {
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): DataViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val allNewsRowBinding = AllNewsRowBinding.inflate(layoutInflater, parent, false)
    return DataViewHolder(allNewsRowBinding)
  }

  override fun onBindViewHolder(
    holder: DataViewHolder,
    position: Int
  ) {
    holder.allNewsRowBinding.article = getItem(position)
  }

  object ArticlesDifferntiator : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(
      oldItem: Article,
      newItem: Article
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: Article,
      newItem: Article
    ): Boolean {
      return oldItem.id == newItem.id
    }
  }
}