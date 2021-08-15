package com.example.favouriteplaces

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_add_favorite_place.*
import java.text.SimpleDateFormat
import java.util.*

class AddFavoritePlaceActivity : AppCompatActivity(), View.OnClickListener {

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_favorite_place)

        setSupportActionBar(toolbar_add_place)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_add_place.setNavigationOnClickListener {
            onBackPressed()
        }

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,month)
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDateInView()
        }

        et_date.setOnClickListener(this)
        tv_add_image.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.et_date -> {
                DatePickerDialog(this@AddFavoritePlaceActivity,dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.iv_place_image ->{
                val picktureDialog = AlertDialog.Builder(this) // Using builder as context this to build Alert Dialog
                picktureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select Photo from Gallery","Capture photo from Camera")
                picktureDialog.setItems(pictureDialogItems){ //Using picture dialog to set it's items
                    dialog, which ->    // Defining some action depending on dialog which is selected
                    when(which){
                        0 -> choosePhotoFromGallery()
                        1 -> Toast.makeText(this@AddFavoritePlaceActivity,"Camera selction coming soon",
                    Toast.LENGTH_SHORT).show()
                    }
                }
                picktureDialog.show() //Showing the dialogue
            }


        }
    }

    private fun choosePhotoFromGallery(){

    }

    private fun updateDateInView(){
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        et_date.setText(sdf.format(cal.time).toString())
    }
}










