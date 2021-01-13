package com.riocallos.githubusers.repositories.note

import com.riocallos.githubusers.domain.models.Note
import com.riocallos.githubusers.local.note.NoteLocalSource
import io.reactivex.Single
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteLocalSource: NoteLocalSource,
) : NoteRepository {

    override fun add(note: Note) {
        noteLocalSource.add(note)
    }

    override fun has(username: String): Boolean {
        return noteLocalSource.has(username)
    }

    override fun local(username: String): Single<Note> {
        return noteLocalSource.get(username)
    }

    override fun update(note: Note) {
        noteLocalSource.update(note)
    }

}