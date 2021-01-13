package com.riocallos.githubusers.database.daos

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.riocallos.githubusers.database.AppDatabase
import com.riocallos.githubusers.domain.models.Note
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class NoteDaoTest {

    private lateinit var appDatabase: AppDatabase

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun insertNotes() {
        val note = Note()
        note.username = "user1"
        note.text = "user notes"
        appDatabase.noteDao().insert(note)

        assert(appDatabase.noteDao().getAll().blockingGet().isNotEmpty())
    }

    @Test
    fun deleteeNotes() {
        val note = Note()
        note.username = "user1"
        note.text = "user notes"
        appDatabase.noteDao().insert(note)

        appDatabase.noteDao().deleteAll().blockingAwait()

        assert(appDatabase.noteDao().getAll().blockingGet().isEmpty())
    }

}