package com.example.uppgift3.database

import androidx.lifecycle.LiveData
import androidx.room.*

// databas metoder
@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAll(): List<Note>

    @Query("SELECT * FROM Note")
    fun getAlls(): List<Note>

    @Query("SELECT * FROM Note")
    fun getAllAsLiveData(): LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :id")
    fun getNote(id:Int): Note

    @Insert
    fun insertAll(vararg users: Note)

    @Delete
    fun delete(user: Note)

    @Query("DELETE FROM Note")
    fun deleteAll()

    @Update
    fun updateNote(note: Note)
}