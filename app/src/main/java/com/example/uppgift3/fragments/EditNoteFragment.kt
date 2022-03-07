package com.example.uppgift3.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.uppgift3.fragments.viewmodels.EditNoteViewModel
import com.example.uppgift3.fragments.viewmodels.EditNoteViewModelFactory


class EditNoteFragment : AbstactNoteViewFragment() {
    val returnAction = EditNoteFragmentDirections.actionEditNoteFragmentToStartFragment2()


    val args by navArgs<EditNoteFragmentArgs>()
    override val singleNoteViewModel by viewModels<EditNoteViewModel> {
        EditNoteViewModelFactory(
            this,
            null,
            requireActivity().application, args
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      return   super.onCreateView(inflater, container, savedInstanceState)
    }

    //sparar anteckning och återvänder till StartFragment
    override fun setUpSaveButton() {
        saveButton.setOnClickListener {
            singleNoteViewModel.saveTempImageToNote()
            sharedViewModel.updateNote(singleNoteViewModel.note)
            navController.navigate(returnAction)
        }
    }

    /*
    Tar användare vidare till NoteImageFragment
    Så användare kan se bilden bifogad med anteckningen i fullskärm.
     */
    override fun goToImage(uri: String) {
        val goToNoteImageAction = EditNoteFragmentDirections.actionEditNoteFragmentToNoteImageFragment(uri)
        navController.navigate(goToNoteImageAction)
    }

    // tar bort anteckningen som är sparad i databas och dens bild ifall existerar.
    // Återvänder sen till StartFragment
    override fun deleteNote() {
        singleNoteViewModel.tempfile.delete()
        sharedViewModel.deleteNote(singleNoteViewModel.note)
        navController.navigate(returnAction)
    }
}
