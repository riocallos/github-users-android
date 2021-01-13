package com.riocallos.githubusers.network.remote.user

import com.riocallos.githubusers.domain.core.ErrorHandler
import com.riocallos.githubusers.domain.core.Result
import com.riocallos.githubusers.domain.models.User
import com.riocallos.githubusers.network.api.ApiServices
import io.reactivex.Single
import javax.inject.Inject

class UserRemoteSourceImpl @Inject constructor(
    private val apiServices: ApiServices
) : UserRemoteSource {
    override fun getAll(since: Int): Single<Result<Array<User>>> {
        return apiServices.getUsers(since).map { response ->
            run {

                Result.success(response)
            }
        }.onErrorReturn {
            Result.error(ErrorHandler.handleError(it))
        }
    }

    override fun get(username: String): Single<Result<User>> {
        return apiServices.getUser(username).map { response ->
            run {

                Result.success(response)
            }
        }.onErrorReturn {
            Result.error(ErrorHandler.handleError(it))
        }
    }
}