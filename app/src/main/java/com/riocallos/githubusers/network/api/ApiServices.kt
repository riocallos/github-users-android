package com.riocallos.githubusers.network.api

import com.riocallos.githubusers.domain.models.User
import io.reactivex.Single
import retrofit2.http.*

interface ApiServices {

    /**
     * Users API endpoint.
     *
     * @property since [Int] filter.
     */
    @GET("users")
    fun getUsers(@Query("since") since: Int): Single<Array<User>>

    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Single<User>

}