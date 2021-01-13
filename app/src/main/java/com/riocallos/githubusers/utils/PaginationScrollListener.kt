package com.riocallos.githubusers.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener protected constructor(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)
		val totalItemCount = layoutManager.itemCount
		val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
		if (!isLoading() && lastVisibleItemPosition == totalItemCount - 1) {
			loadMoreItems()
		}
	}

	protected abstract fun loadMoreItems()
	protected abstract fun isLoading(): Boolean

}
