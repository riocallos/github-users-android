package com.riocallos.githubusers.network.remote.user

import com.riocallos.githubusers.domain.core.Result
import com.riocallos.githubusers.domain.models.User
import io.reactivex.Single

interface UserRemoteSource {

    fun getAll(since: Int): Single<Result<Array<User>>>

    fun get(username: String): Single<Result<User>>

}