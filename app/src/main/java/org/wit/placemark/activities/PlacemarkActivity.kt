package org.wit.placemark.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel
import android.net.Uri
import timber.log.Timber.i

class PlacemarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkBinding
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>

    var placemark = PlacemarkModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        registerImagePickerCallback()
        registerMapCallback()

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        if (intent.hasExtra("placemark_edit")) {
            edit = true

            @Suppress("DEPRECATION")
            val passedPlacemark =
                intent.extras?.getParcelable<PlacemarkModel>("placemark_edit")

            if (passedPlacemark == null) {
                finish()
                return
            }

            placemark = passedPlacemark
            binding.placemarkTitle.setText(placemark.title)
            binding.description.setText(placemark.description)
            binding.btnAdd.setText(R.string.save_placemark)
            if (placemark.image != Uri.EMPTY) {
                Picasso.get()
                    .load(placemark.image)
                    .into(binding.placemarkImage)

                binding.chooseImage.setText(R.string.change_placemark_image)
            }
        }
        binding.placemarkLocation.setOnClickListener {
            i ("Set Location Pressed")
        }
        binding.btnAdd.setOnClickListener {
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.description.text.toString()

            if (placemark.title.isBlank()) {
                Snackbar.make(
                    it,
                    R.string.enter_placemark_title,
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                if (edit) {
                    app.placemarks.update(placemark.copy())
                } else {
                    app.placemarks.create(placemark.copy())
                }

                setResult(RESULT_OK)
                finish()
            }
        }
        binding.placemarkLocation.setOnClickListener {
            i("Set Location Pressed")
            val launcherIntent = Intent(this, MapActivity::class.java)
            mapIntentLauncher.launch(launcherIntent)
        }

    }

    private fun showImagePicker(
        launcher: ActivityResultLauncher<Intent>
    ) {
        val imageIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        launcher.launch(
            Intent.createChooser(imageIntent, "Select image")
        )
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageUri = result.data?.data
                        ?: return@registerForActivityResult

                    placemark.image = imageUri

                    Picasso.get()
                        .load(placemark.image)
                        .into(binding.placemarkImage)

                    binding.chooseImage.setText(
                        R.string.change_placemark_image
                    )
                }
            }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                i("Map Loaded")
            }
    }
}
