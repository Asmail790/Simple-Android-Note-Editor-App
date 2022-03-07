package com.example.uppgift3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uppgift3.R
import com.example.uppgift3.fragments.viewmodels.SharedViewModel
import com.example.uppgift3.recylerview.RVAdapter
import com.example.uppgift3.recylerview.RVCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.uppgift3.Variables.SharedPref
import com.example.uppgift3.database.Note
import com.example.uppgift3.database.formatter

class StartFragment : Fragment() {
    val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var  navController:NavController
    private lateinit var recyclerView:RecyclerView
    private lateinit var fab:FloatingActionButton
    private lateinit var settings:SharedPreferences
    private lateinit var sortoption:String
    private lateinit var orderoption:String
    private val actionGoToSettings = StartFragmentDirections.actionStartFragment2ToSettingsFragment2()





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false).also {
            init(it)
            setUpfab()
            setUpRecylerView()
        }
        return view

    }
    // initerar alla  private lateinit var
    fun init(view:View){
        navController = findNavController()
        recyclerView = view.findViewById(R.id.note_rv)
        fab = view.findViewById(R.id.add_note)
        settings = requireContext().getSharedPreferences(SharedPref.Prefname, Context.MODE_PRIVATE)
        sortoption = settings.getString(SharedPref.sort.key, SharedPref.sort.defualt) as String
        orderoption = settings.getString(SharedPref.order.key, SharedPref.order.defualt) as String
        setHasOptionsMenu(true)
    }

    // sätter up fab så att använderna navigeras till CreateNoteFragment
    fun setUpfab(){
        fab.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragment2ToCreateNoteFragment()
            navController.navigate(action)
        }
    }

    // sätter up recylerview
    fun setUpRecylerView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val rvCallback = RVCallback(sharedViewModel,requireContext())
        val itemTouchHelper = ItemTouchHelper(rvCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // observar ändrningar från en liveNoteList koplad till databasen och updater  recyclerView
        sharedViewModel.liveNoteList.observe(this.viewLifecycleOwner, {
            recyclerView.adapter = RVAdapter(it.sortedByPrefence(), navController,requireActivity().application)
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.start_fragment_option_menu, menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.start_fragment_option_delete_all -> {
                sharedViewModel.deleteAll()
            }
            R.id.start_fragment_option_settings -> {
                navController.navigate(actionGoToSettings)
            }
        }
        return true
}

    // beronde inställningar SharedPref.order.ascending
    // ska noter ordnas i fallande eller stigande ordning
    private fun   <R:Comparable<R>> List<Note>.sortNote(b:(Note) -> R):List<Note> {
        return  if (orderoption == SharedPref.order.ascending)
            this.sortedBy(b)
        else
            this.sortedByDescending(b)
    }

    // beronde inställningar SharedPref.order.ascending
    // ska noter sorteras efter titel i alfabetisk ordning , datum eller efter mängden text
    private  fun List<Note>.sortedByPrefence():List<Note>{
   return  when (sortoption) {
    SharedPref.sort.alpha ->this.sortNote{it.text}
    SharedPref.sort.date ->  this.sortNote{formatter.parse(it.timestamp)}
    SharedPref.sort.size -> this.sortNote{it.text.length + it.title.length}
    else -> throw IllegalArgumentException()
        }
    }

}