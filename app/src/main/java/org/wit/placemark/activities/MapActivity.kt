package org.wit.placemark.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityMapBinding
import org.wit.placemark.models.Location

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        location = intent.extras?.getParcelable<Location>("location")!!

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val loc = LatLng(location.lat, location.lng)

        val options = MarkerOptions()
            .title("Placemark")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)

        map.addMarker(options)

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(loc, location.zoom)
        )

        map.setOnMarkerClickListener { marker ->
            marker.snippet = "GPS : ${marker.position}"
            false
        }

        map.setOnMarkerDragListener(
            object : GoogleMap.OnMarkerDragListener {

                override fun onMarkerDragStart(marker: Marker) {
                }

                override fun onMarkerDrag(marker: Marker) {
                    marker.snippet = "GPS : ${marker.position}"
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    location.lat = marker.position.latitude
                    location.lng = marker.position.longitude
                    location.zoom = map.cameraPosition.zoom

                    marker.snippet = "GPS : ${marker.position}"
                    marker.showInfoWindow()
                }
            }
        )
    }
}
