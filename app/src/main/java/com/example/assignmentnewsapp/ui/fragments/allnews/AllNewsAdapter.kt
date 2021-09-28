package com.example.assignmentnewsapp.ui.fragments.allnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentnewsapp.databinding.AllNewsRowBinding
import com.example.assignmentnewsapp.networking.model.Articles


class AllNewsAdapter(private val news: ArrayList<Articles>):
    RecyclerView.Adapter<AllNewsAdapter.DataViewHolder>() {
    class DataViewHolder(val allNewsRowBinding: AllNewsRowBinding) : RecyclerView.ViewHolder(allNewsRowBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
       val layoutInflater =LayoutInflater.from(parent.context)
        val allNewsRowBinding= AllNewsRowBinding.inflate(layoutInflater,parent,false)
        return DataViewHolder(allNewsRowBinding)
    }
    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.allNewsRowBinding.articles = news[position]
    }

    fun addNews(news: ArrayList<Articles>) {
        this.news.apply {
            clear()
            addAll(news)
        }

    }
}