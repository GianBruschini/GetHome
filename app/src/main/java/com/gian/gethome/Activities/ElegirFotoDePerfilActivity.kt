package com.gian.gethome.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gian.gethome.Clases.Model
import com.gian.gethome.databinding.ActivityElegirFotoDePerfilBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_elegir_foto_de_perfil.*
import java.io.IOException
import java.util.*

class ElegirFotoDePerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityElegirFotoDePerfilBinding
    private lateinit var mFirebaseAuth: FirebaseAuth
    private val PICK_IMAGE: Int = 1
    private var imageUri: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var mUploadTask: StorageTask<*>? = null
    private  var name:String? = null
    private  var facebook:String? = null
    private  var instagram:String? = null
    private var twitter:String? = null
    private  var web:String? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var countryName: String? = null
    private var provinceName: String? = null
    private var userAccept: Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElegirFotoDePerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeValues()
        openOptionsImage()

    }

    private fun initializeValues() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference("UsersProfilePictures" + "/" + mFirebaseAuth.getCurrentUser()?.uid)
    }


    fun Continuar(view: View?) {
        if (profile.drawable != null) {
            uploadPicture()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Seleccione una foto de perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openOptionsImage() {
        selectImage.setOnClickListener {
            val gallery = Intent()
            gallery.type = "image/*"
            gallery.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            try {
                Picasso.get().load(imageUri).into(profile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun getFileExtension(uri: Uri): String? {
        val cR = this.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadPicture() {
        if (imageUri != null) {
            val fileReference = mStorageRef!!.child(System.currentTimeMillis()
                    .toString() + "." + getFileExtension(imageUri!!))
            mUploadTask = fileReference.putFile(imageUri!!)
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            val model = Model(uri.toString())
                            storeValuesFirebase(model.imageURL)
                            Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener { e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun storeValuesFirebase(imageURL: String) {
        val currentUser = mFirebaseAuth.currentUser
        val userId = currentUser!!.uid
        val imagenRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child("Person").child(userId).child("imageURL")
        imagenRef.setValue(imageURL)
    }


}