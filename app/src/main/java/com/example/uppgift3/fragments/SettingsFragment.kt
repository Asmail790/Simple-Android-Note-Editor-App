package com.example.uppgift3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.uppgift3.R
import com.example.uppgift3.Variables


/*
fragment för app inställningar
använder sig av SharedPreferences för spara inställningar
*/

class SettingsFragment : Fragment() {
    private lateinit var sortSpinner: Spinner
    private lateinit var orderSpinner: Spinner
    private lateinit var checkBox: CheckBox
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false).also {
            init(it)
            setUpCheckBox()
            setUpOrder()
            setUpSortSpinner()
        }
    }
    // initerar alla  private lateinit var
    fun init(view: View) {
        navController = findNavController()
        sortSpinner = view.findViewById<Spinner>(R.id.sort_recyler_by_spinner)
        orderSpinner = view.findViewById<Spinner>(R.id.choose_layout_recyler_spinner)
        checkBox = view.findViewById<CheckBox>(R.id.show_image_checkbox)
        sharedPreferences =  requireContext().getSharedPreferences(Variables.SharedPref.Prefname, Context.MODE_PRIVATE)
    }


    fun setUpCheckBox(){
        // updaterar vyn så matchar värderna i sharedPreferences
        checkBox.isChecked = sharedPreferences.getBoolean(Variables.SharedPref.image.key,Variables.SharedPref.image.defualt)

        // observerar ändrningar och updaterar  sharedPreferences
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(Variables.SharedPref.image.key, isChecked).apply()
        }
    }
    fun setUpSortSpinner() {
        // array adapter över möjliga inställning för sort
        val sortSpinnerAdapter = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_item,
            Variables.SharedPref.listOfSortOptions
        )
        // updaterar vyn så matchar värderna i sharedPreferences
        sortSpinner.adapter = sortSpinnerAdapter
         val value = sharedPreferences.getString(Variables.SharedPref.sort.key,Variables.SharedPref.sort.defualt )
        val index = Variables.SharedPref.sortOptionMap[value] as Int
        sortSpinner.setSelection(index)

        // observerar ändrningar och updaterar  sharedPreferences
        sortSpinner.onItemSelectedListener =  object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val choosenOne :String = Variables.SharedPref.listOfSortOptions[position]
                sharedPreferences.edit().putString(Variables.SharedPref.sort.key, choosenOne).apply()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    fun setUpOrder(){
        // array adapter över möjliga inställning för order
        val orderSpinnerAdapter = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_spinner_item,
            Variables.SharedPref.listOfOrderOptions
        )
        // updaterar vyn så matchar värderna i sharedPreferences
        orderSpinner.adapter = orderSpinnerAdapter
        val value = sharedPreferences.getString(Variables.SharedPref.order.key,Variables.SharedPref.order.defualt )
        val index = Variables.SharedPref.orderOptionMap[value] as Int
        orderSpinner.setSelection(index)

        // observerar ändrningar och updaterar  sharedPreferences
        orderSpinner.onItemSelectedListener =  object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val choosenOne :String = Variables.SharedPref.listOfOrderOptions[position]
                sharedPreferences.edit().putString(Variables.SharedPref.order.key, choosenOne).apply()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}