package com.riocallos.githubusers.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.riocallos.githubusers.database.daos.NoteDao
import com.riocallos.githubusers.database.daos.UserDao
import com.riocallos.githubusers.domain.models.Note
import com.riocallos.githubusers.domain.models.User

@Database(version = 1, exportSchema = false, entities = [User::class, Note::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun noteDao(): NoteDao

    companion object {

         fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "githubusers.db")
            .allowMainThreadQueries()
            .build()
    }

}
