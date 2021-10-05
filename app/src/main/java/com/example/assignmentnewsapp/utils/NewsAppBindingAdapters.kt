package com.example.assignmentnewsapp.utils

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.assignmentnewsapp.R

object NewsAppBindingAdapters {


    @BindingAdapter("booleanVisibility")
    @JvmStatic
    fun ProgressBar.booleanVisibility(visibility: Boolean) {
      if (visibility) {
        this.visibility = View.VISIBLE
      } else {
        this.visibility = View.GONE
      }
    }

  @BindingAdapter("loadImage")
  @JvmStatic
  fun AppCompatImageView.loadImage(imageUrl: String?) {
    imageUrl?.let { Log.d("imageUrl", it)
      Glide.with(this.context).load(imageUrl).centerCrop()
        .into(this)}

    }
}