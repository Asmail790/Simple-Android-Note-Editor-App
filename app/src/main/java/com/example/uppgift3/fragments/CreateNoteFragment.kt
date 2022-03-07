package com.example.uppgift3.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.example.uppgift3.fragments.viewmodels.*

/*
* fragment för skapa nya anteckningar
* mycket av koden ärvs av AbstactNoteViewFragment */
class CreateNoteFragment : AbstactNoteViewFragment() {
    // för navigera tillbaks till startfragment
    val returnAction = CreateNoteFragmentDirections.actionCreateNoteFragmentToStartFragment2()
    override val singleNoteViewModel  by viewModels<CreateNoteViewModel>
    {
        CreateNoteViewModelFactory(
        this,
        null,
        requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return  super.onCreateView(inflater, container, savedInstanceState)
    }

    //sparar anteckning och återvänder till StartFragment
    override fun setUpSaveButton() {
        saveButton.setOnClickListener {
            singleNoteViewModel.saveTempImageToNote()
            sharedViewModel.addNote(singleNoteViewModel.note)
            navController.navigate(returnAction)
        }
    }

    /*
    Tar användare vidare till NoteImageFragment
    Så användare kan se bilden bifogad med anteckningen i fullskärm.
     */
    override fun goToImage(uri: String) {
        val goToNoteImageAction = CreateNoteFragmentDirections.actionCreateNoteFragmentToNoteImageFragment(uri)
        navController.navigate(goToNoteImageAction)
    }
    // tar bort anteckningen som inte sparad i databas och dens bild ifall existerar.
    // Återvänder sen till StartFragment
    override fun deleteNote() {
        singleNoteViewModel.tempfile.delete()
        navController.navigate(returnAction)
    }
}