package com.riocallos.githubusers.features.user

import android.content.SharedPreferences
import android.os.Bundle
import com.riocallos.githubusers.base.BaseViewModel
import com.riocallos.githubusers.domain.models.Note
import com.riocallos.githubusers.domain.models.User
import com.riocallos.githubusers.repositories.note.NoteRepository
import com.riocallos.githubusers.repositories.user.UserRepository
import com.riocallos.githubusers.utils.AppLogger
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository
) : BaseViewModel() {

    override fun isFirstTimeUiCreate(bundle: Bundle?) = Unit

    private val _state: PublishSubject<UserState> by lazy {
        PublishSubject.create<UserState>()
    }

    val state: Observable<UserState> = _state

    var user = User()
    var note = Note()

    fun removeUser() {
        sharedPreferences.edit().putString("user", "").apply()
    }

    fun localUser(username: String) = userRepository
        .local(username)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .doOnSubscribe {
            _state.onNext(UserState.ShowProgressLoading)
        }
        .doOnSuccess {
            _state.onNext(UserState.HideProgressLoading)
        }
        .doOnError {
            _state.onNext(UserState.HideProgressLoading)
        }
        .subscribeBy {
            if(it != null) {
                user = it
                if(noteRepository.has(user.username)) {
                    getNote(user.username)
                }
                sharedPreferences.edit().putString("user", user.username).apply()
                _state.onNext(UserState.ShowUser(it))
            } else {
                _state.onNext(UserState.ShowError("Unable to get user"))
            }
        }

    fun remoteUser(username: String) = userRepository
        .remoteUser(username)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .doOnSubscribe {
            _state.onNext(UserState.ShowProgressLoading)
        }
        .doOnSuccess {
            _state.onNext(UserState.HideProgressLoading)
        }
        .doOnError {
            _state.onNext(UserState.HideProgressLoading)
        }
        .subscribeBy {
            if(!it.isError) {
                user = it.value()
                if(noteRepository.has(user.username)) {
                    getNote(user.username)
                }
                userRepository.update(user)
                sharedPreferences.edit().putString("user", user.username).apply()
                _state.onNext(UserState.ShowUser(it.value()))
            } else {
                localUser(username)
            }
        }

    fun getNote(username: String) = noteRepository
        .local(username)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
        .doOnSubscribe {
            _state.onNext(UserState.ShowProgressLoading)
        }
        .doOnSuccess {
            _state.onNext(UserState.HideProgressLoading)
        }
        .doOnError {
            _state.onNext(UserState.HideProgressLoading)
        }
        .subscribeBy {
            if(it != null) {
                note = it
                _state.onNext(UserState.ShowNote(note))
            }
        }

    fun updateNote(noteText: String) {
        note.username = user.username
        note.text = noteText
        if(note.id != 0) {
            noteRepository.update(note)
        } else {
            noteRepository.add(note)
        }
        _state.onNext(UserState.ShowMessage("Notes saved"))
    }

}