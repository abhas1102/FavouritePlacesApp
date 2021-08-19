package com.example.favouriteplaces.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.favouriteplaces.models.FavoritePlaceModel

class DatabaseHandler(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION =1
        private const val DATABASE_NAME = "FavoritePlacesDatabase"
        private const val TABLE_FAVORITE_PLACE = "FavoritePlacesTable"

        //All the columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"




    }

    override fun onCreate(db: SQLiteDatabase?) {
        //Creating table with fields

        val CREATE_FAVORITE_PLACE_TABLE =("CREATE TABLE " + TABLE_FAVORITE_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_FAVORITE_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${TABLE_FAVORITE_PLACE}")
        onCreate(db)
    }

    //Function to insert Favorite place details to sqlite database

    fun addHappyPlace(favoritePlace: FavoritePlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, favoritePlace.title) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_IMAGE, favoritePlace.image) // HappyPlaceModelClass IMAGE
        contentValues.put(
                KEY_DESCRIPTION,
                favoritePlace.description
        ) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_DATE, favoritePlace.date) // HappyPlaceModelClass DATE
        contentValues.put(KEY_LOCATION, favoritePlace.location) // HappyPlaceModelClass LOCATION
        contentValues.put(KEY_LATITUDE, favoritePlace.latitude) // HappyPlaceModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, favoritePlace.longitude) // HappyPlaceModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_FAVORITE_PLACE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }
}