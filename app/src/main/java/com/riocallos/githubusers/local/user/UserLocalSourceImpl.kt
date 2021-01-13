package com.riocallos.githubusers.local.user

import com.riocallos.githubusers.database.AppDatabase
import com.riocallos.githubusers.domain.models.User
import io.reactivex.Single
import javax.inject.Inject

class UserLocalSourceImpl @Inject constructor(
        private val appDatabase: AppDatabase
) : UserLocalSource {

    override fun getAll(): Single<Array<User>> {
        return appDatabase.userDao().getAll()
    }

    override fun get(username: String): Single<User> {
        return appDatabase.userDao().get(username)
    }

    override fun search(query: String): Single<Array<User>> {
        return appDatabase.userDao().search("%$query%")
    }

    override fun update(user: User) {
        appDatabase.userDao().update(user)
    }

}