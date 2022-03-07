package com.example.uppgift3.fragments.viewmodels

import android.app.Application
import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.example.uppgift3.database.AppDatabase
import com.example.uppgift3.database.Note

class SharedViewModel(application: Application):AndroidViewModel(application) {
    val db = Room.databaseBuilder(
    application.applicationContext,
    AppDatabase ::class.java, "Note"
    ).build()

    val liveNoteList = db.NoteDao().getAllAsLiveData()

    fun  deleteNote(note: Note)  = Thread {
        db.NoteDao().delete(note)
        note.imageUri?.run {
            Uri.parse(this).toFile().delete()
        }
    }.start()

    fun deleteAll()= Thread {
        val  urilist:List<String> =   db.NoteDao().getAll().map {it.imageUri }.filter { it !=null } as List<String>
        db.NoteDao().deleteAll()
        urilist.forEach {
            Uri.parse(it).toFile().delete()
        }
    }.start()

    fun addNote(note:Note)  = Thread{
        db.NoteDao().insertAll(note)
    }.start()

    fun  updateNote(updatedNote:Note) = Thread{
       val oldNote =  db.NoteDao().getNote(updatedNote.id)
        if (oldNote.imageUri !=  updatedNote.imageUri && oldNote.imageUri !=null) {
          Uri.parse(oldNote.imageUri).toFile().delete()
          // ta bort gammla bild. behövs inte längre.
      }
        db.NoteDao().updateNote(updatedNote)
    }.start()

}