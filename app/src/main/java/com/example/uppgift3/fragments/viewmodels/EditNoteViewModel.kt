package com.example.uppgift3.fragments.viewmodels

import android.app.Application
import android.net.Uri
import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.uppgift3.Variables
import com.example.uppgift3.database.Note
import com.example.uppgift3.database.formatter
import com.example.uppgift3.fragments.EditNoteFragmentArgs
import java.io.File
import java.util.*

// ärver stor del av kod från AbstractNoteViewModel
class EditNoteViewModel(stateHandle: SavedStateHandle, application: Application, val args: EditNoteFragmentArgs):AbstractNoteViewModel(stateHandle,application){
    override val note: Note
    override val tempfile: File

    init {
        note = stateHandle.get<Note>(keys.note) ?: args.note.also {
            stateHandle.set(keys.note, it)
        }
        tempfile = stateHandle.getFile(keys.file) ?: File.createTempFile("temp",null,storagelocationCache).also {
            note.imageUri?.run {
                Uri.parse(this).toFile().copyTo(it,overwrite = true)
            }
            stateHandle.set("file", it.toUri().toString())
        }
        tempfile.deleteOnExit()
    }

   override fun saveTempImageToNote(){
        if (tempfile.length() != 0L) {
        val file =    if(note.imageUri != null) {
              Uri.parse(note.imageUri).toFile()
            } else {
             val newfile = File(storagelocationPerment,"image_${formatter.format(Date())}")
            note.imageUri = newfile.toUri().toString()
            newfile
            }
            tempfile.copyTo(file,overwrite = true)
        }
    }
}

// fabrik för skapa EditNoteViewModel
class EditNoteViewModelFactory(private val savedStateRegistryOwner: SavedStateRegistryOwner, bundle: Bundle?, private val application: Application, val args: EditNoteFragmentArgs): AbstractSavedStateViewModelFactory(savedStateRegistryOwner,bundle){
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return EditNoteViewModel(handle, application,args) as T
    }


}