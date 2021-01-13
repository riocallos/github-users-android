package com.riocallos.githubusers.repositories.user

import com.riocallos.githubusers.domain.core.Result
import com.riocallos.githubusers.domain.models.User
import com.riocallos.githubusers.local.user.UserLocalSource
import com.riocallos.githubusers.network.remote.user.UserRemoteSource
import com.riocallos.githubusers.utils.AppLogger
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalSource: UserLocalSource,
    private val userRemoteSource: UserRemoteSource
) : UserRepository {

    override fun local(): Single<Array<User>> {
        return userLocalSource.getAll()
    }

    override fun local(username: String): Single<User> {
        return userLocalSource.get(username)
    }

    override fun update(user: User) {
        userLocalSource.update(user)
    }

    override fun remoteUsers(since: Int): Single<Result<Array<User>>> {
        return userRemoteSource.getAll(since)
    }

    override fun remoteUser(username: String): Single<Result<User>> {
        return userRemoteSource.get(username)
    }

    override fun search(query: String): Single<Array<User>> {
        return userLocalSource.search(query)
    }
}