package com.riocallos.githubusers.local.note

import com.riocallos.githubusers.database.AppDatabase
import com.riocallos.githubusers.domain.models.Note
import io.reactivex.Single
import javax.inject.Inject

class NoteLocalSourceImpl @Inject constructor(
        private val appDatabase: AppDatabase
) : NoteLocalSource {
    override fun add(note: Note) {
        appDatabase.noteDao().insert(note)
    }

    override fun has(username: String): Boolean {
        return appDatabase.noteDao().has(username)
    }

    override fun get(username: String): Single<Note> {
        return appDatabase.noteDao().get(username)
    }

    override fun update(note: Note) {
        appDatabase.noteDao().update(note)
    }


}