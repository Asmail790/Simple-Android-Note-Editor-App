package com.example.uppgift3.recylerview

import android.app.Application
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uppgift3.R
import com.example.uppgift3.Variables
import com.example.uppgift3.database.Note
import com.example.uppgift3.fragments.StartFragmentDirections

// en enkel RecylerViewAdapter klass
class RVAdapter(val list:List<Note>, val navController: NavController,  application: Application):RecyclerView.Adapter<RVViewHolder>() {
   // behövs för se checkboxen i inställningar är inkrysat
    val  pref = application
        .applicationContext
        .getSharedPreferences(
            Variables.SharedPref.Prefname,
            Context.MODE_PRIVATE
        )
    // används av Glide
    val context = application.applicationContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val view =   LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_single_item, parent, false)
        return RVViewHolder(view)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
    val note = list[position]
        holder.note = note
        holder.titleField.text  = note.title
        holder.textField.text = note.text
        holder.timeStamp.text = note.timestamp


        val showImage = pref.getBoolean(Variables.SharedPref.image.key,Variables.SharedPref.image.defualt)

        // visa bild enbart om finns om anteckning som har bild och checkboxen i inställningar är inkrysat
        val showImageCondtion = showImage.and(note.imageUri != null )

        if(showImageCondtion  ) {
            Glide.with(context).load(note.imageUri).placeholder(R.drawable.gray_background)
                .into(holder.imageView)
        } else
            holder.imageView.visibility = View.GONE
        holder.itemView.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragment2ToEditNoteFragment(note)
            navController.navigate(action)
        }
    }

    override fun getItemCount() = list.size

}