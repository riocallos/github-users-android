package com.riocallos.githubusers.database.daos

import androidx.room.*
import com.riocallos.githubusers.domain.models.Note
import com.riocallos.githubusers.domain.models.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): Single<Array<User>>

    @Query("SELECT * FROM users WHERE username LIKE :query COLLATE BINARY")
    fun search(query: String): Single<Array<User>>

    @Query("SELECT * FROM users WHERE username = :username")
    fun get(username: String): Single<User>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE id = :id)")
    fun has(id: Int): Boolean

    @Query("DELETE FROM users")
    fun deleteAll(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(user: Array<User>)

    @Update
    fun update(user: User)

}