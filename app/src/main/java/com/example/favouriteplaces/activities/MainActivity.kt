package com.example.favouriteplaces.activities

import android.app.Activity
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
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getFavoritePlacesListFromLocalDB()
    }

    private fun setupFavoritePlacesRecyclerView(favoritePlaceList:ArrayList<FavoritePlaceModel>){
        rv_favorite_places_list.layoutManager = LinearLayoutManager(this)
        rv_favorite_places_list.setHasFixedSize(true)
        val placesAdapter = FavoritePlacesAdapter(this,favoritePlaceList)
        rv_favorite_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : FavoritePlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: FavoritePlaceModel) {
                val intent = Intent(this@MainActivity,FavoritePlaceDetailActivity::class.java)

                intent.putExtra(EXTRA_PLACE_DETAILS,model)
                startActivity(intent)
            }
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                getFavoritePlacesListFromLocalDB()
            }else{
                Log.e("Activity","Cancelled or Backpressed")
            }
        }
    }

    companion object{
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}