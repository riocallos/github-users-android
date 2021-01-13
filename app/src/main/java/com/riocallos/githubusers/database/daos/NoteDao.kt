package com.riocallos.githubusers.database.daos

import androidx.room.*
import com.riocallos.githubusers.domain.models.Note
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): Single<Array<Note>>

    @Query("SELECT * FROM notes WHERE username = :username")
    fun get(username: String): Single<Note>

    @Query("SELECT EXISTS(SELECT * FROM notes WHERE username = :username)")
    fun has(username: String): Boolean

    @Query("DELETE FROM notes")
    fun deleteAll(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(note: Array<Note>)

    @Update
    fun update(note: Note)

}