package com.uka.pokemonandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var playerPower:Double=0.0

    var listOfPokemon = ArrayList<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()

        loadPokemon()
    }

    var ACCESSLOCATION_ID = 112233
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESSLOCATION_ID)
                return
            }
        }

        getUserLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == ACCESSLOCATION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation()
            } else {
                Toast.makeText(this, "Cannot get current location", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        //TODO implement later
        var myLocation = MyLocationListener()
        var locationManger = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

        var myThread = MyThread()
        myThread.start()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions()
                .position(sydney)
                .title("Me")
                .snippet("My Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))
    }

    var mLocation: Location? = null

    inner class MyLocationListener : LocationListener {

        constructor() {
            mLocation = Location("Start")
            mLocation!!.longitude = 0.0
            mLocation!!.latitude = 0.0
        }

        override fun onLocationChanged(location: Location?) {
            mLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

    }

    var oldLocation:Location?=null
    inner class MyThread : Thread {
        constructor() : super(){
            oldLocation = Location("old")
            oldLocation!!.latitude = 0.0
            oldLocation!!.longitude = 0.0
        }

        override fun run() {
            while (true) {
                try {

                    if(oldLocation!!.distanceTo(mLocation) == 0f)
                        continue

                    oldLocation = mLocation

                    runOnUiThread {
                        mMap!!.clear()
                        val sydney = LatLng(mLocation!!.latitude, mLocation!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(sydney)
                                .title("Me")
                                .snippet("My Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))

                        for (p in listOfPokemon) {
                            if(p.isCatch == false){
                                val pokemonLoc = LatLng(p.location!!.latitude, p.location!!.longitude)
                                mMap.addMarker(MarkerOptions()
                                        .position(pokemonLoc)
                                        .title(p.name)
                                        .snippet(p.des + ", power : " + p.power)
                                        .icon(BitmapDescriptorFactory.fromResource(p.image!!)))

                                if(mLocation!!.distanceTo(p.location) < 2){
                                    p.isCatch = true
                                    playerPower += p.power!!

                                    Toast.makeText(applicationContext, "Caught " + p.name + " pokemon, your power now : " + playerPower, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
            }
        }
    }

    fun loadPokemon() {
        listOfPokemon.add(Pokemon("Charmander", "Fire pokemon", R.drawable.charmander, 55.0, 37.77, -122.401))
        listOfPokemon.add(Pokemon("Bulbasaur", "Green pokemon", R.drawable.bulbasaur, 90.5, 37.79, -122.410))
        listOfPokemon.add(Pokemon("Squirtle", "Water pokemon", R.drawable.squirtle, 33.5, 37.78, -122.412))
    }
}
