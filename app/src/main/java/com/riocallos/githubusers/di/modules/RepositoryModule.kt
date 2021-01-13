package com.riocallos.githubusers.di.modules

import com.riocallos.githubusers.local.note.NoteLocalSource
import com.riocallos.githubusers.local.user.UserLocalSource
import com.riocallos.githubusers.network.remote.user.UserRemoteSource
import com.riocallos.githubusers.repositories.note.NoteRepository
import com.riocallos.githubusers.repositories.note.NoteRepositoryImpl
import com.riocallos.githubusers.repositories.user.UserRepository
import com.riocallos.githubusers.repositories.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun providesUserRepository(
        userLocalSource: UserLocalSource,
        userRemoteSource: UserRemoteSource,
    ): UserRepository = UserRepositoryImpl(userLocalSource, userRemoteSource)

    @Provides
    fun providesNoteRepository(
        noteLocalSource: NoteLocalSource,
    ): NoteRepository = NoteRepositoryImpl(noteLocalSource)


}