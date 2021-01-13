package com.riocallos.githubusers.repositories.note

import com.riocallos.githubusers.domain.models.Note
import io.reactivex.Single

interface NoteRepository {

    fun add(note: Note)

    fun has(username: String): Boolean

    fun local(username: String): Single<Note>

    fun update(note: Note)

}