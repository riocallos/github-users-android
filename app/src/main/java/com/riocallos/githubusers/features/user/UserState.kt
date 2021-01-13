package com.riocallos.githubusers.features.user

import com.riocallos.githubusers.domain.models.Note
import com.riocallos.githubusers.domain.models.User

sealed class UserState {

    object ShowProgressLoading : UserState()

    object HideProgressLoading : UserState()

    data class ShowError(val error: String) : UserState()

    data class ShowMessage(val message: String) : UserState()

    data class ShowUser(val user: User) : UserState()

    data class ShowNote(val note: Note) : UserState()

}