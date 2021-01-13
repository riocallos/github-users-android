package com.riocallos.githubusers.di.modules

import android.app.Application
import com.riocallos.githubusers.database.AppDatabase
import com.riocallos.githubusers.local.note.NoteLocalSource
import com.riocallos.githubusers.local.note.NoteLocalSourceImpl
import com.riocallos.githubusers.local.user.UserLocalSource
import com.riocallos.githubusers.local.user.UserLocalSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        return AppDatabase.buildDatabase(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideUserLocalSource(
        appDatabase: AppDatabase
    ): UserLocalSource = UserLocalSourceImpl(appDatabase)


    @Provides
    @Singleton
    fun provideNoteLocalSource(
        appDatabase: AppDatabase
    ): NoteLocalSource = NoteLocalSourceImpl(appDatabase)

}