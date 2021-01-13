package com.riocallos.githubusers.domain.core

data class Error(
        val detail: String = "",
        val code: Int? = null,
        val status: Int? = null
)