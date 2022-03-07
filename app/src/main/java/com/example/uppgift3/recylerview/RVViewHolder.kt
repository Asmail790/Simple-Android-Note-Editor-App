package com.example.uppgift3.recylerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.uppgift3.R
//import com.example.uppgift3.StartFragmentDirections
import com.example.uppgift3.database.Note
import com.example.uppgift3.fragments.StartFragment
import com.example.uppgift3.fragments.viewmodels.SharedViewModel

// enkel RecyclerView.ViewHolder med refernser till alla
// viktiga views i rv_single_item:
class RVViewHolder(view:View):RecyclerView.ViewHolder(view) {
    val titleField = itemView.findViewById<TextView>(R.id.note_title)
    val textField = itemView.findViewById<TextView>(R.id.note_text)
    val timeStamp = itemView.findViewById<TextView>(R.id.note_timeStamp)
    val imageView = itemView.findViewById<ImageView>(R.id.note_image)
    // används för ta bort antecknignar i ItemTochHelper.SimpleCallBack
    lateinit var note: Note
}

