package com.example.assignmentnewsapp.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.assignmentnewsapp.databinding.FragmentAllNewsBinding

import javax.inject.Inject

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VM:BaseViewModel, B: ViewDataBinding>(
  private val inflate: Inflate<B>
): Fragment() {


  protected abstract fun provideViewModel(): VM


    var binding: B? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupView(view)
    setupObservers()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = inflate.invoke(inflater, container, false)
    return binding?.root
  }

  protected open fun setupObservers() {
    provideViewModel().commonMessageChannel.observe(viewLifecycleOwner, Observer {
      Log.d(TAG,it)
    })
  }

  private fun showMessage(message: String) = context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }

  fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

  protected abstract fun setupView(view: View)

  companion object {
    private const val TAG = "BaseFragment"
  }
}