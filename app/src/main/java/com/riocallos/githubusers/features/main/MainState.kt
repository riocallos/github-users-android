package com.riocallos.githubusers.features.main

import com.riocallos.githubusers.domain.models.User

sealed class MainState {

    object ShowProgressLoading : MainState()

    object HideProgressLoading : MainState()

    data class ShowRetrieved(val retrieved: String) : MainState()

    data class ShowUsers(val users: List<User>) : MainState()

    data class ShowUser(val user: User) : MainState()

}