package com.gian.gethome.Activities

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gian.gethome.Clases.Model
import com.gian.gethome.databinding.ActivityImagenesAnimalBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso


class ImagenesAnimalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImagenesAnimalBinding
    private lateinit var tipoAnimal:String
    private lateinit var nombreAnimal: String
    private lateinit var sexoAnimal: String
    private lateinit var transitoUrgente: String
    private lateinit var edadAnimal:String
    private lateinit var descripcionAnimal: String
    private lateinit var mFirebaseAuth:FirebaseAuth
    private lateinit var mStorageReference:StorageReference
    private lateinit var currentImageURL:String
    private var mUploadTask: StorageTask<*>? = null
    private var imageList:MutableList<ImageView> = mutableListOf()
    private var imageUri: Uri? = null
    private val arrayUrisNulls = arrayOfNulls<Uri>(4)
    private val arrayUris:MutableList<Uri> = mutableListOf()
    private lateinit var imagenPerfil:String
    private var cont: Int = 0
    private lateinit var progressDialog: ProgressDialog
    private var total:Int = 0
    private var esIgual: Boolean = false
    private lateinit var pais:String
    private  lateinit var provincia:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagenesAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this@ImagenesAnimalActivity)
        progressDialog.setTitle("Publicando animal")
        progressDialog.setMessage("Por favor, espere")
        progressDialog.setCancelable(false)

        setMutableList()
        getValues()
        setOnClickListenerAddButtons()
        setOnClickListenerDeleteButtons()

    }

    private fun setOnClickListenerDeleteButtons() {
        binding.delete1.setOnClickListener {
            binding.imagen1.setImageDrawable(null)
            arrayUrisNulls[0] = null
        }

        binding.delete2.setOnClickListener {
            binding.imagen2.setImageDrawable(null)
            arrayUrisNulls[1] = null
        }

        binding.delete3.setOnClickListener {
            binding.imagen3.setImageDrawable(null)
            arrayUrisNulls[2] = null
        }

        binding.delete4.setOnClickListener {
            binding.imagen4.setImageDrawable(null)
            arrayUrisNulls[3] = null
        }
    }

    private fun setMutableList() {
        imageList.add(binding.imagen1)
        imageList.add(binding.imagen2)
        imageList.add(binding.imagen3)
        imageList.add(binding.imagen4)
    }

    private fun setOnClickListenerAddButtons() {
        binding.add1.setOnClickListener {
            openImageOptions(1)
        }
        binding.add2.setOnClickListener {
            openImageOptions(2)
        }
        binding.add3.setOnClickListener {
            openImageOptions(3)
        }
        binding.add4.setOnClickListener {
            openImageOptions(4)
        }
    }

    private fun openImageOptions(requestCode: Int) {
        
        val gallery = Intent()
        gallery.type = "image/*"
        gallery.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(gallery, "Seleccione una imagen"), requestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == -1 && data != null){
            when(requestCode){
                1 -> setImageViewWith(0, data)
                2 -> setImageViewWith(1, data)
                3 -> setImageViewWith(2, data)
                4 -> setImageViewWith(3, data)
            }
        }
    }

    private fun setImageViewWith(numberImageView: Int, data: Intent) {
        imageUri = data.data
        arrayUrisNulls[numberImageView]=imageUri
        //arrayUris.add(imageUri!!)
        Picasso.get().load(imageUri).fit().into(imageList[numberImageView])
    }

    private fun getValues() {
        nombreAnimal = intent.getStringExtra("nombreAnimal").toString()
        tipoAnimal = intent.getStringExtra("tipoAnimal").toString()
        sexoAnimal = intent.getStringExtra("sexoAnimal").toString()
        transitoUrgente = intent.getStringExtra("transitoUrgente").toString()
        edadAnimal = intent.getStringExtra("edadAnimal").toString()
        descripcionAnimal = intent.getStringExtra("descripcionAnimal").toString()
        pais = intent.getStringExtra("Pais").toString()
        provincia = intent.getStringExtra("Provincia").toString()
    }

    fun publicarAnimal(view: View) {
        if(arrayUrisNulls.isNotEmpty()){
            progressDialog.show()
            storeValuesOnDatabase()
        }else{
            Toast.makeText(applicationContext, "Debe agregar al menos una imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun storeValuesOnDatabase() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser = mFirebaseAuth.currentUser!!
        val userId = currentUser.uid
        val key = FirebaseDatabase.getInstance().getReference("Animales").push().key
        val nombre = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key!!).child("nombre")
        val tipo = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("tipoAnimal")
        val transitoUrgenteData = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("transitoUrgente")
        val edad = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("edad")
        val descripcion = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("descripcion")
        val userIDRF = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("userIDowner")
        val animalKey = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("animalKey")
        val sexoAnimalDB = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("sexo")
        val provinciaDB = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("provincia")
        val paisDB = FirebaseDatabase.getInstance().reference.child("Users").child("Animales").child(userId).child(key).child("pais")
        userIDRF.setValue(userId)
        nombre.setValue(nombreAnimal)
        tipo.setValue(tipoAnimal)
        transitoUrgenteData.setValue(transitoUrgente)
        edad.setValue(edadAnimal)
        descripcion.setValue(descripcionAnimal)
        animalKey.setValue(key.toString())
        sexoAnimalDB.setValue(sexoAnimal)
        provinciaDB.setValue(provincia)
        paisDB.setValue(pais)
        //Start images upload
        storeImagesOnDataBase(key, currentUser)

    }

    private fun storeImagesOnDataBase(key: String, currentUser: FirebaseUser) {
       mStorageReference = FirebaseStorage.getInstance().getReference("UploadsAnimals" + "/" + mFirebaseAuth.currentUser!!.uid)
       uploadFiles(key, currentUser)

    }

    private fun getFileExtension(uri: Uri): String? {
        val cR: ContentResolver = this.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFiles(key: String, currentUser: FirebaseUser) {

        settingImagesInNullDB(currentUser, key)
        for(item in arrayUrisNulls.indices){
            if(arrayUrisNulls[item] == null){

            }else{
                arrayUris.add(arrayUrisNulls[item]!!)
            }
        }
        total = arrayUris.size
        for (i in arrayUris.indices) {
                val actualImage: Uri = arrayUris[i]
                val fileReference: StorageReference = mStorageReference.child(System.currentTimeMillis()
                        .toString() + "." + getFileExtension(actualImage))
                currentImageURL = fileReference.downloadUrl.toString()
                mUploadTask = fileReference.putFile(actualImage)
                        .addOnSuccessListener {
                            fileReference.downloadUrl.addOnSuccessListener { uri ->
                                val model = Model(uri.toString())
                                saveDefaultData(model.imageURL, key, currentUser)
                            }
                        }
                        .addOnFailureListener { e -> Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun saveDefaultData(imageURL: String, key: String, currentUser: FirebaseUser) {

        cont ++
        when(cont){
            1 -> {
                val imagen1 = FirebaseDatabase.getInstance()
                        .reference.child("Users").child("Animales").child(currentUser.uid).child(key).child("imagen1")
                imagen1.setValue(imageURL)
                if (cont == total) {
                    esIgual = true
                }
            }

            2 -> {
                val imagen2 = FirebaseDatabase.getInstance()
                        .reference.child("Users").child("Animales").child(currentUser.uid).child(key).child("imagen2")
                imagen2.setValue(imageURL)
                if (cont == total) {
                    esIgual = true
                }
            }

            3 -> {
                val imagen3 = FirebaseDatabase.getInstance()
                        .reference.child("Users").child("Animales").child(currentUser.uid).child(key).child("imagen3")
                imagen3.setValue(imageURL)
                if (cont == total) {
                    esIgual = true
                }
            }

            4 -> {

                val imagen4 = FirebaseDatabase.getInstance()
                        .reference.child("Users").child("Animales").child(currentUser.uid).child(key).child("imagen4")
                imagen4.setValue(imageURL)
                if (cont == total) {
                    esIgual = true
                }
            }

        }
        if(esIgual){
            progressDialog.dismiss()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun settingImagesInNullDB(currentUser: FirebaseUser, key: String) {
        val setImage1 = FirebaseDatabase.getInstance()
                .reference.child("Users").child("Animales").child(currentUser.uid).child(key).child("imagen1")
        setImage1.setValue("null")
        val setImage2 = FirebaseDatabase.getInstance()
                .reference.child("Users").child("Animales").child(currentUser.uid).child(key).child("imagen2")
        setImage2.setValue("null")
        val setImage3 = FirebaseDatabase.getInstance()
                .reference.child("Users").child("Animales").child(currentUser.uid).child(key).child("imagen3")
        setImage3.setValue("null")
        val setImage4 = FirebaseDatabase.getInstance()
                .reference.child("Users").child("Animales").child(currentUser.uid).child(key).child("imagen4")
        setImage4.setValue("null")
    }

    fun back(view: View) {
        finish()
    }

}