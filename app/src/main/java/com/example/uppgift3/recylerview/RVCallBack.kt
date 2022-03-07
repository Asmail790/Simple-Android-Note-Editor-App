package com.example.uppgift3.recylerview

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.example.uppgift3.fragments.viewmodels.SharedViewModel

// enkel  ItemTouchHelper.SimpleCallback class så svepa bort anteckningar från höger sida
class  RVCallback(val sharedViewModel: SharedViewModel,val context: Context): ItemTouchHelper.SimpleCallback(0, RIGHT){
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val note = (viewHolder as RVViewHolder).note
        sharedViewModel.deleteNote(note)
        Toast.makeText(context, "note deleted", Toast.LENGTH_SHORT).show()
    }


}