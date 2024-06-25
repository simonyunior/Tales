package com.example.tales.maps

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tales.R
import com.example.tales.databinding.ActivityMapsBinding
import com.example.tales.model.Story
import com.example.tales.viewmodel.StoryViewModel
import com.example.tales.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import com.example.tales.Injector

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = ViewModelProvider(this, ViewModelFactory(Injector.provideStoryRepository(this))).get(StoryViewModel::class.java)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val defaultLocation = LatLng(-34.0, 151.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))

        loadStoriesWithLocation()
    }

    private fun loadStoriesWithLocation() {
        storyViewModel.getStoriesWithLocation().observe(this) { stories ->
            addMarkers(stories)
        }
    }

    private fun addMarkers(stories: List<Story>) {
        for (story in stories) {
            story.lat?.let { lat ->
                story.lon?.let { lon ->
                    val location = LatLng(lat, lon)
                    val marker = mMap.addMarker(MarkerOptions().position(location).title(story.name))
                    marker?.tag = story
                }
            }
        }

        mMap.setOnMarkerClickListener { marker ->
            val story = marker.tag as? Story
            story?.let {
                AlertDialog.Builder(this)
                    .setTitle(story.name)
                    .setMessage("Description: ${story.description}")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            true
        }
    }
}
