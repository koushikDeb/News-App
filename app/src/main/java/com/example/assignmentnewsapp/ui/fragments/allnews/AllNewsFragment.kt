package com.example.assignmentnewsapp.ui.fragments.allnews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentnewsapp.R
import com.example.assignmentnewsapp.databinding.FragmentAllNewsBinding
import com.example.assignmentnewsapp.networking.model.Response
import com.example.assignmentnewsapp.networking.retrofit.ApiHelper
import com.example.assignmentnewsapp.networking.retrofit.RetrofitBuilder
import com.example.assignmentnewsapp.ui.base.ViewModelProviderFactory

import com.example.assignmentnewsapp.utils.Status.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllNewsFragment : Fragment() {
    private  val viewModel: AllNewsViewModel by viewModels()
    private lateinit var adapter: AllNewsAdapter
    lateinit var binding: FragmentAllNewsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentAllNewsBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUI()
        setupObservers()

    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AllNewsAdapter(arrayListOf())
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
      //  viewModel =ViewModelProvider(requireActivity(),ViewModelProviderFactory(ApiHelper(RetrofitBuilder.apiService))).get(AllNewsViewModel::class.java)
    }

    private fun setupObservers() {
        viewModel.getNews().observe(viewLifecycleOwner) { it ->
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { news -> retrieveList(news as Response) }
                    }
                    ERROR -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun retrieveList(news: Response) {
        adapter.apply {
            addNews(news.articles)
            notifyDataSetChanged()
        }
    }
}