package com.riocallos.githubusers.database.daos

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.riocallos.githubusers.database.AppDatabase
import com.riocallos.githubusers.domain.models.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class UserDaoTest {

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
    fun insertUsers() {
        val user = User()
        user.id = 0
        user.username = "user1"
        appDatabase.userDao().insert(user)

        assert(appDatabase.userDao().getAll().blockingGet().isNotEmpty())
    }

    @Test
    fun getUsers() {
        val user = User()
        user.id = 0
        user.username = "user1"
        appDatabase.userDao().insert(user)

        val users = Array(1){user}

        users.forEach {
            appDatabase.userDao().insert(it)
        }

        val retrievedUsers = appDatabase.userDao().getAll().blockingGet()
        assert(retrievedUsers.contentEquals(users))
    }

    @Test
    fun deleteUsers() {
        val user = User()
        user.id = 0
        user.username = "user1"
        appDatabase.userDao().insert(user)

        appDatabase.userDao().deleteAll().blockingAwait()

        assert(appDatabase.userDao().getAll().blockingGet().isEmpty())
    }

}