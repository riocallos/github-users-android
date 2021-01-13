package com.riocallos.githubusers.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.riocallos.githubusers.R

/**
 * Custom data binding for ImageView.
 *
 */
class ImageViewDataBinding {

    /**
     * Bind image url to RecyclerView.
     *
     * @property imageView [ImageView] is the view.
     * @property url [String] for the image resource.
     */
    @BindingAdapter("url")
    fun bind(imageView: ImageView, url: String?) {

        if(url != null && url.isNotEmpty()) {

            Glide.with(imageView.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.centerCropTransform().placeholder(R.drawable.placeholder).error(R.drawable.placeholder)).load(url).into(imageView)

        }

    }

}
