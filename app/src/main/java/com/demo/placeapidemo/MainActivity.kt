package com.demo.placeapidemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var hMap: HuaweiMap? = null

    private lateinit var mMapView: MapView

    val TAG = "HmsMap"

    lateinit var addressInput_1: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ==== HMS Map Initialization ====
        MapsInitializer.setApiKey("DQEDALznOogbIVi6SGNWfcG3VGe8s5KXymjnaGBIyJK/QmtooUdAxisyycLz1i3Apo4k2U3tmRpKBP5dGpUbikGQsYRBZca3koMmFw==")
        MapsInitializer.initialize(this);

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mMapView = findViewById(R.id.mapview_mapviewdemo)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey")
        }
        mMapView.onCreate(mapViewBundle)
        mMapView.getMapAsync(this)

        // ==== Google Place ====
        val apiKey = "AIzaSyARvjUVkc1jBIUPT5lPGPQEHA6aWLdy2kg"

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        // Initialize the SDK
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)
        addressInput_1 = findViewById(R.id.autocomplete_address1)
        addressInput_1.setOnClickListener(startAutocompleteIntentListener)

    }

    override fun onMapReady(huaweiMap: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hMap = huaweiMap
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    // Google Place API

    private var startAutocompleteIntentListener = View.OnClickListener { view: View ->
        view.setOnClickListener(null)
        startAutocompleteIntent()
    }

    // [START maps_solutions_android_autocomplete_define]
    private val startAutocomplete = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result: ActivityResult ->
           // binding.autocompleteAddress1.setOnClickListener(startAutocompleteIntentListener)
            addressInput_1.setOnClickListener(startAutocompleteIntentListener)
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    Log.d(TAG, "Place: " + place.addressComponents)

                    locatePlaceInMap(place)
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "User canceled autocomplete")
            }
        } as ActivityResultCallback<ActivityResult>)
    // [END maps_solutions_android_autocomplete_define]

    private fun locatePlaceInMap(place: Place) {
        hMap?.apply {
            val latLng = LatLng(place.latLng.latitude, place.latLng.longitude)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(latLng,
                12.0F, 2.2F, 31.5F
            ))
            hMap?.animateCamera(cameraUpdate)
        }
    }

    // [START maps_solutions_android_autocomplete_intent]
    private fun startAutocompleteIntent() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG, Place.Field.VIEWPORT
        )

        // Build the autocomplete intent with field, country, and type filters applied
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setCountries(listOf("US"))
            .setTypesFilter(listOf(PlaceTypes.ADDRESS))
            .build(this)
        startAutocomplete.launch(intent)
    }
}