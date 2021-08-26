package com.example.favouriteplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favouriteplaces.R
import com.example.favouriteplaces.adapter.FavoritePlacesAdapter
import com.example.favouriteplaces.database.DatabaseHandler
import com.example.favouriteplaces.models.FavoritePlaceModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddHappyPlace.setOnClickListener{
            val intent = Intent(this, AddFavoritePlaceActivity::class.java)
            startActivity(intent)
        }
        getFavoritePlacesListFromLocalDB()
    }

    private fun setupFavoritePlacesRecyclerView(favoritePlaceList:ArrayList<FavoritePlaceModel>){
        rv_favorite_places_list.layoutManager = LinearLayoutManager(this)
        rv_favorite_places_list.setHasFixedSize(true)
        val placesAdapter = FavoritePlacesAdapter(this,favoritePlaceList)
        rv_favorite_places_list.adapter = placesAdapter
    }

    private fun getFavoritePlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getFavoritePlaceList : ArrayList<FavoritePlaceModel> = dbHandler.getFavoritePlacesList()

        if (getFavoritePlaceList.size>0){
          //  for(i in getFavoritePlaceList){
              //  Log.e("Title", i.title)
           // }

            rv_favorite_places_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupFavoritePlacesRecyclerView(getFavoritePlaceList)
        }else{
            rv_favorite_places_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }
}