package com.demo.placeapidemo

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var hMap: HuaweiMap? = null

    private lateinit var mMapView: MapView

    val TAG = "HmsMap"

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
    }

    override fun onMapReady(huaweiMap: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hMap = huaweiMap
    }


//    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
//
//        var mapViewBundle: Bundle? = outState?.getBundle(MAPVIEW_BUNDLE_KEY)
//        if (mapViewBundle == null) {
//            mapViewBundle = Bundle()
//            outState?.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
//        }
//
//        mMapView?.onSaveInstanceState(mapViewBundle)
//    }

    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }
}