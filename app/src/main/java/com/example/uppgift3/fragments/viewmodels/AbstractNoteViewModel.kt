package com.example.uppgift3.fragments.viewmodels

import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.uppgift3.Variables
import com.example.uppgift3.database.Note
import java.io.File

/*
abstract klass som innehåller allt gemensamt mellan CreateNoteViewModel och EditNoteViewModel
använder SavedStateHandler för Återställa tillstånd
 */
abstract class AbstractNoteViewModel(val stateHandle: SavedStateHandle,  application: Application):ViewModel(){
    // för spara temporära bilder
    val storagelocationCache = application.applicationContext.cacheDir
    // för spara permenta bilder
    val storagelocationPerment = application.applicationContext.filesDir
    // krävs för öppna  en fil från galleriet
    val contentResolver = application.contentResolver
    // krävs för fileProvider
    val context = application.applicationContext

    // en anteckning
    // skiljer sig åt mellan CreateNoteViewModel och EditNoteViewModel
    abstract val note: Note

    // temponär file för spara bilder temponärt
    // när användare klar koperias till en perment fil
    // skiljer sig åt mellan CreateNoteViewModel och EditNoteViewModel
    abstract val tempfile:File

    object keys{
        const val file = "file"
        const val note = "note"
    }

    // metod för koperia fill utanför (galleriet)
    fun copyFileUriToTempFile(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val outputstram  = tempfile.outputStream()
        inputStream!!.copyTo(outputstram)
        inputStream.close()
        outputstram.close()
    }

    // används för camera intent i AbstractNoteViewFragment
    fun getContentUriForTempFile() =
        FileProvider.getUriForFile(context, Variables.authority,tempfile)

    // hämtar ut file från SavedStateHandle eller ger null om ej existerar
    // krävs för Återställa tillstånd
    fun SavedStateHandle.getFile(key:String):File?{
        return if (this.contains(key))
            Uri.parse(this.get<String>(key)).toFile()
        else
            null
    }
    // sparar fil sökväg i form av string
    // krävs för Återställa tillstånd
    fun SavedStateHandle.setFile(key:String,file:File){
        this.set(key, file.absoluteFile)
    }
    // sprar note
    // krävs för Återställa tillstånd
    fun  saveNoteToSavedStateHandle(){
        stateHandle.set(keys.note,note)
    }
    // metod för spara bilden i tempfile till en perment fill
    // skiljer sig åt mellan CreateNoteViewModel och EditNoteViewModel
    abstract   fun saveTempImageToNote()

}

