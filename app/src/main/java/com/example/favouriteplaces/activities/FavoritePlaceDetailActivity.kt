package com.example.favouriteplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favouriteplaces.R
import com.example.favouriteplaces.models.FavoritePlaceModel
import kotlinx.android.synthetic.main.activity_favorite_place_detail.*

class FavoritePlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_place_detail)

        var favoritePlaceDetailModel : FavoritePlaceModel? = null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            favoritePlaceDetailModel = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as FavoritePlaceModel
        }

        if (favoritePlaceDetailModel!=null){
            setSupportActionBar(toolbar_favorite_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = favoritePlaceDetailModel.title

            toolbar_favorite_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(favoritePlaceDetailModel.image))
            tv_description.text = favoritePlaceDetailModel.description
            tv_location.text = favoritePlaceDetailModel.location

        }
    }
}