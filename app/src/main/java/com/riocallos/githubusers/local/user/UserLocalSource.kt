package com.riocallos.githubusers.local.user

import com.riocallos.githubusers.domain.models.User
import io.reactivex.Single

interface UserLocalSource {
    fun getAll(): Single<Array<User>>
    fun get(username: String): Single<User>
    fun search(query: String): Single<Array<User>>
    fun update(user: User)
}