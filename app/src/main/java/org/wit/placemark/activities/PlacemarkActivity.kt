package org.wit.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel
import timber.log.Timber.i
//App screen, it inheritis Android screen features from AppCompatActivity
class PlacemarkActivity : AppCompatActivity() {
    //Variable declaration
    //bindings give access to the screen's fields and button.
    //lateinit means give a value later, in onCreate()
    private lateinit var binding: ActivityPlacemarkBinding
    var placemark = PlacemarkModel()
    //create model that holds tit,e and description
    lateinit var app : MainApp
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
        placemark.title = binding.placemarkTitle.text.toString()
        placemark.description = binding.description.text.toString()
        if (placemark.title.isNotEmpty()) {
            app.placemarks.add(placemark.copy())
            i("add Button Pressed: ${placemark}")
            for (i in app.placemarks.indices) {
                i("Placemark[$i]:${this.app.placemarks[i]}")
            }
            setResult(RESULT_OK)
            finish()
        }
        else {
            Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                .show()
        }
    }
    }
}

