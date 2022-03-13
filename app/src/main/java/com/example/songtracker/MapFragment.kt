package com.example.songtracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.songtracker.MainActivity.Companion.ClassicLyricNumber
import com.example.songtracker.MainActivity.Companion.CurrentLyricNumber
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {

    val PERMISSION_ID = 42
    private var mMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mMarker: Marker? = null
    private var lyricMarker: Marker? = null
    lateinit var mPreferences: SharedPreferences
    var randNum = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity()!!)
        getLastLocation()
        requestNewLocationData()
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        mMap!!.mapType = MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true

        // Add a marker at the bay campus and move the camera
        val bayCampus = LatLng(51.619263, -3.880025)
        val zoomLevel = 17.0f
        mMarker = mMap!!.addMarker(MarkerOptions().position(bayCampus).title("Where you are"))
        //mMap!!.moveCamera(CameraUpdateFactory.newLatLng(bayCampus))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(bayCampus, zoomLevel))

        val lyricPoint = generatePointOnBay()
        lyricMarker = mMap!!.addMarker(MarkerOptions().position(lyricPoint).title("Lyric (" + lyricPoint.latitude.toString() + ", " + lyricPoint.longitude.toString() + ")" ))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =inflater!!.inflate(R.layout.map_fragment, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (mapFragment == null) {
            val fm = getFragmentManager()
            val ft = fm!!.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            ft.replace(R.id.map, mapFragment!!).commit()
        }
        mapFragment!!.getMapAsync(this)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun checkIfLyricFound() {
        val user = mMarker!!.getPosition()
        val lyric = lyricMarker!!.getPosition()
        //Roughly 10-20 metres radius around marker.
        val radiusLat = 0.000016
        val radiusLon = 0.000108
        //Checks to see if user is within certain range of lyric marker.
        if (user.latitude >= lyric.latitude-radiusLat && user.latitude <= lyric.latitude+radiusLat) {
            if (user.longitude >= lyric.longitude-radiusLon && user.longitude <= lyric.longitude+radiusLon) {
                addFoundLyric()
                Toast.makeText(getActivity()!!, "You found a lyric!", Toast.LENGTH_LONG).show()
                val position = generatePointOnBay()
                lyricMarker!!.setPosition(position)
                lyricMarker!!.setTitle("Lyric (" + position.latitude.toString() + ", " + position.longitude.toString() + ")" )
            }
        }
    }

    fun addFoundLyric() {
        mPreferences = getActivity()!!.getSharedPreferences(MainActivity.sharedPrefFile, Context.MODE_PRIVATE)
        val editor = mPreferences.edit()
        var lyricNumber: Int
                //Increments lyric number shared preference depending on game mode.
                if (mPreferences.getString(MainActivity.GameMode, "Classic").equals("Classic")) {
                    lyricNumber = mPreferences.getInt(ClassicLyricNumber, 0) + 1
                    editor.putInt(ClassicLyricNumber, lyricNumber)
                } else {
                    lyricNumber = mPreferences.getInt(CurrentLyricNumber, 0) + 1
                    editor.putInt(CurrentLyricNumber, lyricNumber)
                }
        editor.commit()
    }

    fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener(getActivity()!!) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        var lat = location.latitude
                        var long = location.longitude

                        var accuracy = location.accuracy
                        val lastLoc = LatLng(lat, long)
                        val zoomLevel = 17.0f
                        mMarker!!.setPosition(lastLoc)
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, zoomLevel))
                    }
                }
            } else {
                Toast.makeText(getActivity()!!, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    private fun requestNewLocationData() {
        Log.i("myLocation", "request")
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 2000
        mLocationRequest.fastestInterval = 1000

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity()!!)
        fusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.i("myLocation", "Callback")
            var mLastLocation: Location = locationResult.lastLocation
            var lat = mLastLocation.latitude
            var long = mLastLocation.longitude
            val lastLoc = LatLng(lat, long)
            mMarker!!.setPosition(lastLoc)
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(lastLoc))
            checkIfLyricFound()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getActivity()!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                getActivity()!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                getActivity()!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            getActivity()!!,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    //Generates random point on Bay Campus.
    fun generatePointOnBay(): LatLng {
        //These are the boundaries of bay campus on google maps.
        var minLat = 51.6173010000
        var maxLat = 51.6197010000
        var minLon = -3.88329900000
        var maxLon = -3.87503200000
        var lyricPoint: LatLng
            var lat: Double
            var lon: Double
            lat = minLat + (randNum.nextDouble() * (maxLat - minLat))
            lon = minLon + (randNum.nextDouble() * (maxLon - minLon))
            lyricPoint = LatLng(lat, lon)
        return lyricPoint
    }
    companion object {
        fun newInstance(): MapFragment = MapFragment()
    }
}