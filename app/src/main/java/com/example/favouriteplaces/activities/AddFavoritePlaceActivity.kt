package com.example.favouriteplaces.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.favouriteplaces.R
import com.example.favouriteplaces.database.DatabaseHandler
import com.example.favouriteplaces.models.FavoritePlaceModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_favorite_place.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class AddFavoritePlaceActivity : AppCompatActivity(), View.OnClickListener {

    private var cal = Calendar.getInstance()
    private var saveImageToInternalStorage: Uri? = null

    private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
    private var mLongitude: Double = 0.0 // A variable which will hold the longitude value.
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
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        updateDateInView() // It automatically populates the date when screen opens

        et_date.setOnClickListener(this)
        tv_add_image.setOnClickListener(this)
        btn_save.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.et_date -> {
                DatePickerDialog(this@AddFavoritePlaceActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.tv_add_image -> {
                val picktureDialog = AlertDialog.Builder(this) // Using builder as context this to build Alert Dialog
                picktureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select Photo from Gallery", "Capture photo from Camera")
                picktureDialog.setItems(pictureDialogItems) { //Using picture dialog to set it's items
                    dialog, which ->    // Defining some action depending on dialog which is selected
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera() /*Toast.makeText(this@AddFavoritePlaceActivity, "Camera selction coming soon",
                                Toast.LENGTH_SHORT).show()*/
                    }
                }
                picktureDialog.show() //Showing the dialogue
            }

            R.id.btn_save ->{

                when{

                    et_title.text.isNullOrEmpty() -> {
                        Toast.makeText(this,"please enter title",Toast.LENGTH_SHORT).show()
                    }

                    et_description.text.isNullOrEmpty() -> {
                        Toast.makeText(this,"please enter title",Toast.LENGTH_SHORT).show()
                    }

                    et_location.text.isNullOrEmpty() -> {
                        Toast.makeText(this,"please enter title",Toast.LENGTH_SHORT).show()
                    }

                    saveImageToInternalStorage == null ->{
                        Toast.makeText(this,"please seleect an image", Toast.LENGTH_SHORT).show()
                    } else ->{
                        val favoritePlaceModel = FavoritePlaceModel(
                                0,et_title.text.toString(),
                                saveImageToInternalStorage.toString(),
                                et_description.text.toString(),
                                et_date.text.toString(),
                                et_location.text.toString(),
                                mLatitude,
                                mLongitude
                        )

                    val dbHandler = DatabaseHandler(this)
                    val addFavoritePlace = dbHandler.addHappyPlace(favoritePlaceModel)

                    if (addFavoritePlace>0){
                        Toast.makeText(this,"The Favorite Place details are inserted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    }

                }

            }


        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY){
                if (data!=null){
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,contentURI)
                         saveImageToInternalStorage = saveImageToInternalStorage(selectedImageBitmap)
                        Log.e("Saved Image: ", "Path :: $saveImageToInternalStorage")

                        iv_place_image.setImageBitmap(selectedImageBitmap)
                    }catch (e:IOException){
                        e.printStackTrace()
                        Toast.makeText(this@AddFavoritePlaceActivity,"Failed to load the image", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (requestCode == CAMERA){
                val thumbnail : Bitmap = data!!.extras!!.get("data") as Bitmap // Getting data from function parameter and transforming it in a Bitmap format
                val saveImageToInternalStorage = saveImageToInternalStorage(thumbnail)
                Log.e("Saved Image: ", "Path :: $saveImageToInternalStorage")
                iv_place_image.setImageBitmap(thumbnail)
            }
        }
    }

    private fun takePhotoFromCamera(){

        Dexter.withActivity(this).withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA

        ).withListener(object: MultiplePermissionsListener{
            override fun onPermissionsChecked(report: MultiplePermissionsReport?){
                if (report!!.areAllPermissionsGranted()){
                    //  Toast.makeText(this@AddFavoritePlaceActivity,"Storage Read Write Permission Granted",
                    //  Toast.LENGTH_SHORT).show()

                    val galleryIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(galleryIntent, CAMERA)

                }

            }

            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) { // To show user why it is needed
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()

    }

    private fun choosePhotoFromGallery(){
    Dexter.withActivity(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    ).withListener(object: MultiplePermissionsListener{
        override fun onPermissionsChecked(report: MultiplePermissionsReport?){
            if (report!!.areAllPermissionsGranted()){
              //  Toast.makeText(this@AddFavoritePlaceActivity,"Storage Read Write Permission Granted",
              //  Toast.LENGTH_SHORT).show()

                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, GALLERY)

            }

        }

        override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) { // To show user why it is needed
            showRationalDialogForPermissions()
        }
    }).onSameThread().check()
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It looks like you have " +
                "turned off permissions").setPositiveButton("Go to Settings")
        { _,_ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",packageName,null) // Sending the user to Settings
                intent.data = uri //Adding the uri data to intent
                startActivity(intent)

            }catch (e:ActivityNotFoundException){
                e.printStackTrace()
            }

        }.setNegativeButton("cancel"){dialog,which ->
            dialog.dismiss()
        }.show()
    }

    private fun updateDateInView(){
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        et_date.setText(sdf.format(cal.time).toString())
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap):Uri{ // It will return the location of where image is stored
        val wrapper = ContextWrapper(applicationContext) // Context wrapper extends Context and we can use application context
        var file = wrapper.getDir(IMAGE_DIRECTORY,Context.MODE_PRIVATE) // In order to get the directory we need context wrapper. It has specific places in phone to store data
        file = File(file,"${UUID.randomUUID()}.jpg")

        try {
            val stream : OutputStream = FileOutputStream(file) // Creating Output stream of file which contains image
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream) // Compressing the bitmap in jpeg format
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }

    return Uri.parse(file.absolutePath) // parsing the file path into Uri

    }

    companion object{
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "FavoritePlacesImages"
    }
}










