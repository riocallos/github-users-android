package com.riocallos.githubusers.repositories.user

import com.riocallos.githubusers.domain.core.Result
import com.riocallos.githubusers.domain.models.User
import io.reactivex.Single

interface UserRepository {

    fun local(): Single<Array<User>>

    fun local(username: String): Single<User>

    fun update(user: User)

    fun remoteUsers(since: Int): Single<Result<Array<User>>>

    fun remoteUser(username: String): Single<Result<User>>

    fun search(query: String): Single<Array<User>>

}