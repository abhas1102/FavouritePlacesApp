package com.example.favouriteplaces.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favouriteplaces.R
import com.example.favouriteplaces.models.FavoritePlaceModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(),OnMapReadyCallback {

    private var mFavoritePlaceDetails:FavoritePlaceModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){

            mFavoritePlaceDetails = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as FavoritePlaceModel


        }

        if(mFavoritePlaceDetails!=null){
            setSupportActionBar(toolbar_map)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mFavoritePlaceDetails!!.title
            toolbar_map.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }
}