package com.riocallos.githubusers.base

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClick(v: View, item: T, position: Int)
}