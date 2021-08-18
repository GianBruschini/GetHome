package com.gian.gethome.Activities.imagenesanimal.model

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.net.toFile
import com.facebook.FacebookSdk.getCacheDir
import com.gian.gethome.Activities.elegirfotodeperfil.model.Model
import com.gian.gethome.Activities.imagenesanimal.view.ImagenesAnimalActivity
import com.gian.gethome.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class ImagenesAnimalInteractor {
    private  lateinit var formatted:String
    private lateinit var currentImageURL:String
    private var mUploadTask: StorageTask<*>? = null
    private var imageList:MutableList<ImageView> = mutableListOf()
    private var imageUri: Uri? = null
    private val arrayUrisNulls = arrayOfNulls<Uri>(4)
    private val arrayUris:MutableList<Uri> = mutableListOf()
    private lateinit var mFirebaseAuth:FirebaseAuth
    private lateinit var mStorageReference: StorageReference
    private var total:Int = 0
    private var cont: Int = 0
    private var esIgual: Boolean = false
    private var numberImg by Delegates.notNull<Int>()

    private val SAMPLE_CROPPED_IMG_NAME:String = "Sample"


    interface onImagenesAnimalListener{
        fun setImageSelected(imageUri: Uri?, imageView: ImageView)
        fun onAddAtLeastOneImage()
        fun onShowProgressDialog()
        fun onHideProgressDialog()
        fun navigateToHome()
    }


    @SuppressLint("SimpleDateFormat")
    fun setLocalDate(listener: onImagenesAnimalListener) {
        val df = SimpleDateFormat("dd-MM-yyyy")
        formatted = df.format(Date())
    }

    fun setMutableList(imagen1: ImageView, imagen2: ImageView, imagen3: ImageView, imagen4: ImageView, imagenesAnimalPresenter: onImagenesAnimalListener) {
        imageList.add(imagen1)
        imageList.add(imagen2)
        imageList.add(imagen3)
        imageList.add(imagen4)

    }

    fun setImageViewResult(resultCode: Int, data: Intent, requestCode: Int,
                           listener: onImagenesAnimalListener, context: ImagenesAnimalActivity) {

        if(requestCode in 0..4){
            numberImg= requestCode
        }

        when (requestCode) {
            1 -> setImageViewWith(0, data, listener, context)
            2 -> setImageViewWith(1, data, listener, context)
            3 -> setImageViewWith(2, data, listener, context)
            4 -> setImageViewWith(3, data, listener, context)

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                when(numberImg){
                    1 ->{
                        imageList[0].setImageURI(result.uri)
                        arrayUrisNulls[0]=result.uri
                    }
                    2 ->{
                        imageList[1].setImageURI(result.uri)
                        arrayUrisNulls[1]=result.uri
                    }
                    3 ->{
                        imageList[2].setImageURI(result.uri)
                        arrayUrisNulls[2]=result.uri
                    }
                    4 -> {
                        imageList[3].setImageURI(result.uri)
                        arrayUrisNulls[3]=result.uri
                    }
                }
                if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                }
            }
        }
    }

    fun startCrop(uri: Uri, context: ImagenesAnimalActivity){
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(context)
    }

    private  fun setImageViewWith(numberImageView: Int, data: Intent, listener: onImagenesAnimalListener, context: ImagenesAnimalActivity) {
        imageUri = data.data
        if(imageUri!=null){
            startCrop(imageUri!!, context)

        }
        //arrayUris.add(imageUri!!)
        //listener.setImageSelected(imageUri, imageList[numberImageView])
    }



    fun setImageInNull(posImage: Int) {
        imageList[posImage].setImageDrawable(null)
        arrayUrisNulls[posImage] = null
    }


    fun publicarAnimalDB(nombreAnimal: String,
                         tipoAnimal: String,
                         transitoUrgente: String,
                         edadAnimal: String,
                         descripcionAnimal: String,
                         sexoAnimal: String,
                         provincia: String,
                         pais: String,
                         whatsapp: String,
                         phone: String,
                         mail: String, listener: onImagenesAnimalListener, context: ImagenesAnimalActivity,
                         facebook: String, instagram: String, cantAnimales: String, latitude: String, longitude: String) {
        var todosNull:Boolean = false;
        var cont:Int = 0;
        for (item in arrayUrisNulls.indices){
            if(arrayUrisNulls[item] == null){
                cont++;
                if(cont == 4){
                    todosNull = true;
                }
            }
        }
        if(!todosNull){
            listener.onShowProgressDialog()
            storeValuesOnDatabase(nombreAnimal, tipoAnimal, transitoUrgente,
                    edadAnimal, descripcionAnimal, sexoAnimal,
                    provincia, pais, whatsapp, phone,
                    mail, listener, context,facebook,instagram,cantAnimales,latitude,longitude)
        }else{
            listener.onAddAtLeastOneImage()
        }
    }


    private fun storeValuesOnDatabase(nombreAnimal: String,
                                      tipoAnimal: String,
                                      transitoUrgente: String,
                                      edadAnimal: String,
                                      descripcionAnimal: String,
                                      sexoAnimal: String,
                                      provincia: String,
                                      pais: String,
                                      whatsapp: String,
                                      phone: String,
                                      mail: String, listener: onImagenesAnimalListener,
                                      context: ImagenesAnimalActivity,
                                      facebook: String, instagram: String,
                                      cantAnimales: String,
                                      latitude: String,
                                      longitude: String) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser = mFirebaseAuth.currentUser!!
        val userId = currentUser.uid
        val key = FirebaseDatabase.getInstance().getReference("Animales").push().key

        val crearUser: HashMap<String, Any> = HashMap()
        crearUser["nombre"] =nombreAnimal
        crearUser["tipoAnimal"] = tipoAnimal
        crearUser["transitoUrgente"] = transitoUrgente
        crearUser["edad"] = edadAnimal
        crearUser["descripcion"] = descripcionAnimal
        crearUser["userIDowner"] = userId
        crearUser["animalKey"] = key.toString()
        crearUser["sexo"] = sexoAnimal
        crearUser["provincia"] = provincia
        crearUser["pais"] = pais
        crearUser["fechaDePublicacion"] = formatted
        crearUser["whatsapp"] = whatsapp
        crearUser["phone"] = phone
        crearUser["mail"] = mail
        crearUser["facebook"] = facebook
        crearUser["instagram"] = instagram
        crearUser["cantAnimales"] = cantAnimales
        crearUser["latitude"] = latitude
        crearUser["longitude"] = longitude
        FirebaseDatabase.getInstance().reference.
        child("Users").
        child("Animales").
        child(userId).
        child(key!!).updateChildren(crearUser)
        storeImagesOnDataBase(key, currentUser, context, listener)
    }

    private fun storeImagesOnDataBase(key: String, currentUser: FirebaseUser, context: ImagenesAnimalActivity, listener: onImagenesAnimalListener) {
        mStorageReference = FirebaseStorage.getInstance().getReference("UploadsAnimals" + "/" + mFirebaseAuth.currentUser!!.uid)
        uploadFiles(key, currentUser, context, listener)

    }

    private fun getFileExtension(uri: Uri, context: ImagenesAnimalActivity): String? {
        val cR: ContentResolver = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFiles(key: String, currentUser: FirebaseUser, context: ImagenesAnimalActivity, listener: onImagenesAnimalListener) {
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
                    .toString() + "." + getFileExtension(actualImage, context))
            currentImageURL = fileReference.downloadUrl.toString()
            mUploadTask = fileReference.putFile(actualImage)
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            val model = Model(uri.toString())
                            saveDefaultData(model.imageURL, key, currentUser, listener)
                        }
                    }
                    .addOnFailureListener { e ->  }
        }
    }


    private fun saveDefaultData(imageURL: String, key: String, currentUser: FirebaseUser, listener: onImagenesAnimalListener) {

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
            listener.onHideProgressDialog()
            listener.navigateToHome()
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
}