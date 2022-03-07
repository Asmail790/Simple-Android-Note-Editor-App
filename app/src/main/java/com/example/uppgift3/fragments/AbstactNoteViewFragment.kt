package com.example.uppgift3.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.uppgift3.R
import com.example.uppgift3.fragments.viewmodels.AbstractNoteViewModel
import com.example.uppgift3.fragments.viewmodels.SharedViewModel

// abstrakt klass som innehåller all kod gemensam mellan EditNoteViewFragment och CreateNoteViewFragment
abstract class AbstactNoteViewFragment:Fragment() {
    protected lateinit var  editTextTitle: EditText
    protected  lateinit var editTextText: EditText
    protected lateinit var imageView: ImageView
    protected lateinit var saveButton: Button
    protected lateinit var navController: NavController
    protected lateinit var  pm: PackageManager
    abstract val singleNoteViewModel: AbstractNoteViewModel
    val sharedViewModel  by activityViewModels<SharedViewModel>()

    // En  ActivityResultLauncher för koperia bifoga en bild från gallriet
    protected val gallaryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), {
        it.doIfResultOk("action avbryten - ingen bild hämtades") {
            val uri = it.data?.data!!
            singleNoteViewModel.copyFileUriToTempFile(uri)
            imageView.setImageURI(null)
            imageView.setImageURI(singleNoteViewModel.tempfile.toUri())
            imageView.show()
        }
    })

    // En  ActivityResultLauncher för koperia bifoga en bild från kameran
    protected val cameraLuancher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), {
        it.doIfResultOk("action avbryten - ingen bild fotades" ) {
            imageView.setImageURI(null)
            imageView.setImageURI(singleNoteViewModel.tempfile.toUri())
            imageView.show()
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_note, container, false).also {
            init(it)
            setUpeditTextText()
            setUpeditTextTitle()
            setUpSaveButton()
            setUpImage()
        }
    }

    // initerar alla lateinit var och sätter up menyen
    fun init(view: View) {
        navController = findNavController()
        editTextTitle = view.findViewById<EditText>(R.id.title_edit)
        editTextText = view.findViewById<EditText>(R.id.text_edit)
        imageView = view.findViewById<ImageView>(R.id.image_edit)
        saveButton= view.findViewById<Button>(R.id.save_button)
        pm = requireContext().packageManager
        setHasOptionsMenu(true)
    }

    // updaterar editTextText och efteråt observerar ändrningarna
    fun setUpeditTextTitle(){
        editTextText.setText(singleNoteViewModel.note.text)
        editTextText.addTextChangedListener {
            singleNoteViewModel.note.text = it.toString()
            singleNoteViewModel.saveNoteToSavedStateHandle()
        }
    }
    // updaterar editTextTitle och efteråt observerar ändrningarna
    fun setUpeditTextText(){
        editTextTitle.setText(singleNoteViewModel.note.title)
        editTextTitle.addTextChangedListener {
            singleNoteViewModel.note.title = it.toString()
            singleNoteViewModel.saveNoteToSavedStateHandle()
        }
    }
    // logik för spar knappen
    // skiljer sig åt mellan EditNoteViewFragment och CreateNoteViewFragment
    abstract fun setUpSaveButton()

    // metod för navigera vidare till antecknings bild
    // skiljer sig åt mellan EditNoteViewFragment och CreateNoteViewFragment pga navigation componet
    abstract fun goToImage(uri:String)

    // sätter up bilden
    fun setUpImage() {
        imageView.setOnClickListener {  goToImage(singleNoteViewModel.tempfile.toUri().toString()) }

        // visa enbart imageview och om finns en  bild sparad i tempfile
        if(singleNoteViewModel.tempfile.length() != 0L) {
            imageView.setImageURI(singleNoteViewModel.tempfile.toUri())
            imageView.show()
        } else {
            imageView.hide()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.single_note_fragment_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      return   when(item.itemId)  {
            R.id.single_note_clear_text -> {
                editTextText.text.clear()
                editTextTitle.text.clear()
                true
            }
            R.id.single_note_add_image -> {
                val intent =
                    Intent(Intent.ACTION_GET_CONTENT)
                        .setType("image/*")

                //  intent använder sig av extension method
                intent.resolveActivity(
                    onError = { alertMessage("There is no app suitable for this action") },
                    action = { gallaryLauncher.launch(this) })
                true
            }
            R.id.single_note_remove_image -> {
                singleNoteViewModel.tempfile.delete()
                imageView.setImageURI(null)
                imageView.hide()
                true
            }

            R.id.single_note_add_picture -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        singleNoteViewModel.getContentUriForTempFile()
                    )
                //  intent använder sig av extension method
                intent.resolveActivity(
                    onError = { alertMessage("There is no app suitable for this action") },
                    action = { cameraLuancher.launch(this) })
                true
            }
            R.id.single_delete_note -> {
                deleteNote()
                true
            }
            else -> false
        }

    }

    // metod för ta bort en anteckning
    // skiljer sig åt mellan EditNoteViewFragment och CreateNoteViewFragment
    abstract fun deleteNote()



    fun ImageView.show() {
        if (imageView.visibility== View.GONE)   imageView.visibility= View.VISIBLE
    }
    fun ImageView.hide() {
        if (imageView.visibility== View.VISIBLE)   imageView.visibility= View.GONE
    }

    // metod som kör func enbart resultCode == Activity.RESULT_OK annars skapat Toast
    fun ActivityResult.doIfResultOk(ifErrorMessage:String, func:() ->Unit) {
        if (this.resultCode == Activity.RESULT_OK ) func()
        else
            Toast.makeText(context, ifErrorMessage, Toast.LENGTH_SHORT).show()
    }
    // metod som kör func enbart resultCode == Activity.RESULT_OK annars kör onError
    fun ActivityResult.doIfResultOk(onError:(Int)->Unit, func:() ->Unit) {
        if (this.resultCode == Activity.RESULT_OK ) func()
        else
            onError(resultCode)
    }
    // metod som kör action enbart finns program som kan hantera denna intent.
    //  annars skapat Toast
    fun Intent.resolveActivity(ifErrorMessage:String, action: Intent.()->Unit) {
        if (this.resolveActivity(pm)!=null)
            action()
        else
            Toast.makeText(requireContext(),ifErrorMessage, Toast.LENGTH_SHORT).show()
    }
    // metod som kör action enbart finns program som kan hantera denna intent.
    //  annars kör onError
    fun Intent.resolveActivity(onError: Intent.()->Unit, action: Intent.()->Unit) {
        if (this.resolveActivity(pm)!=null)
            action()
        else
            onError()
    }
    // metod för vissa alertbox
    fun alertMessage(message:String ){
        AlertDialog.Builder(requireContext())
            .setMessage("There is no app suitable for this action")
            .setCancelable(true)
            .show()
    }
}