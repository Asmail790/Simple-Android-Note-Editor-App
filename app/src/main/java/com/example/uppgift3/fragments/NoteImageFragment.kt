package com.example.uppgift3.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.example.uppgift3.R


// framgent för vissa en bild i fullskärm
class NoteImageFragment : Fragment() {
    val noteImageFragmentArgs by navArgs<NoteImageFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_image, container, false).also {
            it.findViewById<ImageView>(R.id.fullsize_image_view).setImageURI(
                Uri.parse(noteImageFragmentArgs.uriString)
            )
        }
    }
}