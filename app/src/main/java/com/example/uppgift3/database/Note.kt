package com.example.uppgift3.database

import android.os.Parcelable
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import kotlinx.parcelize.Parcelize
import java.util.*


// repsenterar en anteckning
@Parcelize
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var title:String="",
    var text:String="",
    var imageUri:String?=null,
    var timestamp: String = formatter.format(Date())
): Parcelable

@Database(entities = arrayOf(Note::class),version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun NoteDao(): NoteDao
}
