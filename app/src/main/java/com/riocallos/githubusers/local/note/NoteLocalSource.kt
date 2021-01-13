package com.riocallos.githubusers.local.note

import com.riocallos.githubusers.domain.models.Note
import io.reactivex.Single

interface NoteLocalSource {
    fun add(note: Note)
    fun has(username: String): Boolean
    fun get(username: String): Single<Note>
    fun update(note: Note)
}