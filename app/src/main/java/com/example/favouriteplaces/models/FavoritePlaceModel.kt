package com.example.favouriteplaces.models

import java.io.Serializable

data class FavoritePlaceModel(
        val id:Int,
        val title:String,
        val image:String,
        val description:String,
        val date:String,
        val location:String,
        val latitude:Double,
        val longitude:Double
) : Serializable