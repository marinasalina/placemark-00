package org.wit.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel
import timber.log.Timber
import timber.log.Timber.i
//App screen, it inheritis Android screen features from AppCompatActivity
class PlacemarkActivity : AppCompatActivity() {
    //Variable declaration
    //bindings give access to the screen's fields and button.
    //lateinit means give a value later, in onCreate()
    private lateinit var binding: ActivityPlacemarkBinding
    var placemark = PlacemarkModel()
    //create model that holds tit,e and description
    var app : MainApp? = null
//Android calls onCreate() when the activity first starts.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //create the layot and display it
        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
//connect to main APP
    //application is the app’s Application object. as MainApp tells Kotlin
    // to treat it as your MainApp class, giving you access to its placemarks list.
        app = application as MainApp
    //write message to Logcat
        i("Placemark Activity started...")
    //The code inside this listener runs each time the user presses Add.
        binding.btnAdd.setOnClickListener() {
            //Read both text fields and puts their values into the model
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.description.text.toString()
            //Check the title and add the coppy
            if (placemark.title.isNotEmpty()) {
                //app!! - it is app, not null
                //copy()keeps previoisly added placemarks separate from model
                app!!.placemarks.add(placemark.copy())
                //Log every placemark
                //$is the possition number
                i("add Button Pressed: ${placemark}")
                for (i in app!!.placemarks.indices)
                { i("Placemark[$i]:${this.app!!.placemarks[i]}") }
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}

