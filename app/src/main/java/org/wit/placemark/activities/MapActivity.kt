package org.wit.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityMapBinding
import org.wit.placemark.models.Location

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //location = intent.extras?.getParcelable("location",Location::class.java)!!
        location = intent.extras?.getParcelable<Location>("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val loc = LatLng(location.lat, location.lng)

        val options = MarkerOptions()
            .title("Placemark")
            .snippet("GPS: $loc")
            .draggable(true)
            .position(loc)

        mMap.addMarker(options)
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(loc, location.zoom)
        )
    }
}
