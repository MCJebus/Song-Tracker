package com.example.songtracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MapFragment : Fragment(), OnMapReadyCallback {


    private var mMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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
    fun addFoundLyric() {
        //This block will retrieve the lyrics previously stored in the text file.
        val list2 = ArrayList<String?>()
        try {
            var fileInputStream: FileInputStream? = null
            fileInputStream = getActivity()!!.openFileInput("lyrics.txt")
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                list2.add(text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //This block writes the previously found lyrics and after writes the newly found lyric.
        try {
            val fileout = getActivity()!!.openFileOutput("lyrics.txt", Context.MODE_PRIVATE)
            val outputWriter = OutputStreamWriter(fileout)
            for (line in list2) {
                outputWriter.write(line)
                outputWriter.append("\n")
            }
            //This is the newly found lyric.
            outputWriter.write("She says im crazy.")
            outputWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    companion object {
        fun newInstance(): MapFragment = MapFragment()
    }
}