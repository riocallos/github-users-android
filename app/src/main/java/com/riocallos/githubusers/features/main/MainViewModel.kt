package com.riocallos.githubusers.features.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.riocallos.githubusers.base.BaseViewModel
import com.riocallos.githubusers.database.AppDatabase
import com.riocallos.githubusers.domain.models.User
import com.riocallos.githubusers.repositories.user.UserRepository
import com.riocallos.githubusers.utils.AppLogger
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val appDatabase: AppDatabase,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    override fun isFirstTimeUiCreate(bundle: Bundle?) = Unit

    private val _state: PublishSubject<MainState> by lazy {
        PublishSubject.create<MainState>()
    }

    val state: Observable<MainState> = _state

    var page = 0
    val users = mutableListOf<User>()
    val retrievedUsers = mutableListOf<User>()
    val searchedUsers = mutableListOf<User>()

    fun getRetrieved() {
        val retrieved = sharedPreferences.getString("retrieved", "")

        if(retrieved!!.isNotEmpty()) {
            val sdf = SimpleDateFormat("MMM. d, yyyy h:mm a", Locale.ENGLISH)
            _state.onNext(MainState.ShowRetrieved("Retrieved ${sdf.format(Date(retrieved.toLong()))}"))
        }
    }

    fun localUsers() = userRepository
        .local()
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .doOnSubscribe {
            _state.onNext(MainState.ShowProgressLoading)
        }
        .doOnSuccess {
            _state.onNext(MainState.HideProgressLoading)
        }
        .doOnError {
            _state.onNext(MainState.HideProgressLoading)
        }
        .subscribeBy {
            if(it.isNotEmpty()) {
                retrievedUsers.addAll(it)
                page = retrievedUsers.lastOrNull()?.id!!
                _state.onNext(MainState.ShowUsers(retrievedUsers))
            }
        }

    fun remoteUsers() = userRepository
        .remoteUsers(page)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .doOnSubscribe {
            _state.onNext(MainState.ShowProgressLoading)
        }
        .doOnSuccess {
            _state.onNext(MainState.HideProgressLoading)
        }
        .doOnError {
            _state.onNext(MainState.HideProgressLoading)
        }
        .subscribeBy {
            if(!it.isError) {
                retrievedUsers.addAll(it.value())
                users.clear()
                users.addAll(retrievedUsers)
                page = retrievedUsers.lastOrNull()?.id!!
                if (retrievedUsers.isNotEmpty()) {
                    sharedPreferences.edit().putString("retrieved", Date().time.toString()).apply()
                    getRetrieved()
                    appDatabase.userDao().insertAll(it.value())
                    _state.onNext(MainState.ShowUsers(users))
                } else {
                    localUsers()
                }
            } else {
                localUsers()
            }
        }

    fun hasNote(username: String) = if(appDatabase.noteDao().has(username)) View.VISIBLE else View.GONE

    fun search(query: String) = userRepository
        .search(query)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .doOnSubscribe {
            _state.onNext(MainState.ShowProgressLoading)
        }
        .doOnSuccess {
            _state.onNext(MainState.HideProgressLoading)
        }
        .doOnError {
            _state.onNext(MainState.HideProgressLoading)
        }
        .subscribeBy {
            AppLogger.error("SEARCH USERS ${it.size}")
            searchedUsers.clear()
            searchedUsers.addAll(it)
            users.clear()
            users.addAll(searchedUsers)
            _state.onNext(MainState.ShowUsers(users))
        }

    fun clearSearch() {
        searchedUsers.clear()
        users.clear()
        users.addAll(retrievedUsers)
        _state.onNext(MainState.ShowUsers(users))
    }


}