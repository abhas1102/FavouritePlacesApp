package com.example.favouriteplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.favouriteplaces.R
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

    private fun getFavoritePlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getFavoritePlaceList : ArrayList<FavoritePlaceModel> = dbHandler.getFavoritePlacesList()

        if (getFavoritePlaceList.size>0){
            for(i in getFavoritePlaceList){
                Log.e("Title", i.title)
            }
        }
    }
}