package com.example.uppgift3.fragments.viewmodels

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.uppgift3.Variables
import com.example.uppgift3.database.Note
import com.example.uppgift3.database.formatter
import java.io.File
import java.util.*

class CreateNoteViewModel(stateHandle: SavedStateHandle,  application: Application):AbstractNoteViewModel(stateHandle,application) {
   override val note:Note
    override val tempfile:File
    init {
            note = stateHandle.get<Note>(keys.note) ?: Note().also {
            stateHandle.set(keys.note, it)
        }
         tempfile = stateHandle.getFile(keys.file) ?: File.createTempFile("temp",null,storagelocationCache).also {
             stateHandle.set("file", it.toUri().toString())
        }
        tempfile.deleteOnExit()
    }



    override fun saveTempImageToNote(){
        // vet att imageuri är null
        if (tempfile.length() != 0L) {
            val permentFile = File(storagelocationPerment,"image_${formatter.format(Date()).toString()}")
            tempfile.copyTo(permentFile)
            note.imageUri = permentFile.toUri().toString()
        }
    }
}

// fabrik för skapa CreateNoteViewModel
class CreateNoteViewModelFactory(private val savedStateRegistryOwner: SavedStateRegistryOwner,bundle: Bundle?,private val application: Application): AbstractSavedStateViewModelFactory(savedStateRegistryOwner,bundle){
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return CreateNoteViewModel(handle, application) as T
    }


}